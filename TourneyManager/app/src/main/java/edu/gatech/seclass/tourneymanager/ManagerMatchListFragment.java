package edu.gatech.seclass.tourneymanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static edu.gatech.seclass.tourneymanager.R.id.playerName;

public class ManagerMatchListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

	private View view;
	private SharedPreferences prefs;
	private Button cancelTournament;
	private TextView first, second, third;
	private ListView matchList;

	public String[] TOURNAMENT_COLUMNS = {
			TourneyManagerContract.TournamentsEntry._ID,
			TourneyManagerContract.TournamentsEntry.COLUMN_TOURNAMENT_ID,
			TourneyManagerContract.TournamentsEntry.COLUMN_FIRST_PRIZE,
			TourneyManagerContract.TournamentsEntry.COLUMN_SECOND_PRIZE,
			TourneyManagerContract.TournamentsEntry.COLUMN_THIRD_PRIZE
	};

	private String[] TOURNAMENT_PLAYER_COLUMNS = {
			TourneyManagerContract.TournamentPlayersEntry._ID,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER_WINS
	};

	private String[] MATCH_COLUMNS = {
			TourneyManagerContract.MatchesEntry._ID,
			TourneyManagerContract.MatchesEntry.COLUMN_TOURNAMENT_ID,
			TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_1,
			TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_2
	};

	private String[] PLAYER_COLUMNS = {
			TourneyManagerContract.PlayersEntry._ID,
			TourneyManagerContract.PlayersEntry.COLUMN_NAME,
			TourneyManagerContract.PlayersEntry.COLUMN_WINNINGS};

	private String selectionTournamentByID = TourneyManagerContract.TournamentsEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentsEntry.COLUMN_TOURNAMENT_ID + " = ?";

	private String selectionTournamentPlayersByID = TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID + " = ?";

	private String selectionMatchesByID = TourneyManagerContract.MatchesEntry.TABLE_NAME + "." +
			TourneyManagerContract.MatchesEntry.COLUMN_TOURNAMENT_ID + " = ?";

	private String selectionMatchesByPlayer = TourneyManagerContract.MatchesEntry.TABLE_NAME + "." +
			TourneyManagerContract.MatchesEntry.COLUMN_TOURNAMENT_ID + " = ? AND (" +
			TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_1 + " = ? OR " +
			TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_2 + " = ? )";

	private String selectionPlayerWins = TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID + " = ? AND " +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER + " = ?";

	private String selectionPlayers = TourneyManagerContract.PlayersEntry.TABLE_NAME + "." +
			TourneyManagerContract.PlayersEntry.COLUMN_USERNAME + " = ?";

	private ArrayList<String> playersRemaining = new ArrayList<>();
	private HashMap<String, Integer> playerWins = new HashMap<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		view = inflater.inflate(R.layout.fragment_manager_match_list, container, false);

		cancelTournament = (Button) view.findViewById(R.id.cancel_tournament);
		first = (TextView) view.findViewById(R.id.first_prize);
		second = (TextView) view.findViewById(R.id.second_prize);
		third = (TextView) view.findViewById(R.id.third_prize);
		matchList = (ListView) view.findViewById(R.id.manager_match_list);

		setPrizes();
		clearMatches();
		getPlayers();
		createMatches();
		printMatches();

		cancelTournament.setOnClickListener(this);
		matchList.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		clearMatches();
		getPlayers();
		createMatches();
		printMatches();

		if (playersRemaining.size() == 0) {
			completeTournament();
		}
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.cancel_tournament: {
				prefs.edit().putBoolean(getString(R.string.isTournamentActive), false).apply();
				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.manager_container, new ManagerMenuFragment()).commit();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()) {
			case R.id.manager_match_list: {
				FrameLayout linear = (FrameLayout) parent.getChildAt(position);
				TextView matchTextView = (TextView) linear.getChildAt(0);
				String[] matchTextArray = matchTextView.getText().toString().split(" ");
				String player1Name = matchTextArray[0];
				String player2Name = matchTextArray[2];
				Bundle bundle = new Bundle();
				bundle.putString("player_1", player1Name);
				bundle.putString("player_2", player2Name);
				bundle.putString("match_name", "");
				if (Math.pow(2, playerWins.get(player1Name) + 2) == prefs.getInt("tournament_players", 0)) {
						bundle.putString("match_name", "first_round");
				}
				if (playerWins.get(player1Name) >= 25) {
					if (playerWins.get(player1Name) >= 50) {
						bundle.putString("match_name", "championship");
					} else {
						bundle.putString("match_name", "third_place");
					}
				}
				if (prefs.getInt("tournament_players", 0) == 2) {
					bundle.putString("match_name", "championship");
				}
				Intent navMatch = new Intent(getActivity(), MatchActivity.class);
				navMatch.putExtras(bundle);
				startActivity(navMatch);
			}
		}
	}

	private void setPrizes() {
		Cursor tournamentCursor = getActivity().getContentResolver().query(TourneyManagerContract.TournamentsEntry.CONTENT_URI,
				TOURNAMENT_COLUMNS, selectionTournamentByID,
				new String[]{prefs.getInt("tourney_id", 0) + ""}, null);

		tournamentCursor.moveToFirst();

		first.setText("1st: $" + tournamentCursor.getString(2));
		second.setText("2nd: $" + tournamentCursor.getString(3));
		third.setText("3rd: $" + tournamentCursor.getString(4));
	}

	private void getPlayers() {
		playersRemaining.clear();
		Cursor players = getActivity().getContentResolver().query(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
				TOURNAMENT_PLAYER_COLUMNS, selectionTournamentPlayersByID, new String[]{prefs.getInt("tourney_id", 0) + ""}, null);
		players.moveToFirst();

		for (int i = 0; i < players.getCount(); i++) {
			playersRemaining.add(players.getString(2));
			playerWins.put(players.getString(2), 0);
			players.move(1);
		}
	}

	private void clearMatches() {
		getActivity().getContentResolver().delete(TourneyManagerContract.MatchesEntry.CONTENT_URI, null, null);
	}

	private int getPlayerWins(String player) {
		Cursor playerWins = getActivity().getContentResolver().query(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
				TOURNAMENT_PLAYER_COLUMNS,
				selectionPlayerWins,
				new String[] {prefs.getInt("tourney_id", 0) + "", player},
				null);
		playerWins.moveToFirst();
		return playerWins.getInt(3);
	}

	private boolean isPlayerMatched(String player) {
		Cursor playerCursor = getActivity().getContentResolver().query(TourneyManagerContract.MatchesEntry.CONTENT_URI,
				MATCH_COLUMNS,
				selectionMatchesByPlayer,
				new String[] {prefs.getInt("tourney_id", 0) + "", player, player},
				null);
		return playerCursor.getCount() != 0;
	}

	private void createMatches() {
		for (int i = 0; i < playersRemaining.size(); i++) {
			String player1 = playersRemaining.get(i);
			playerWins.put(player1, getPlayerWins(player1));
			for (int j = i + 1; j < playersRemaining.size(); j++) {
				String player2 = playersRemaining.get(j);
				playerWins.put(player2, getPlayerWins(player2));

				if ((playerWins.get(player1) == playerWins.get(player2)) && (!isPlayerMatched(player1)) && (!isPlayerMatched(player2))) {
					ContentValues values = new ContentValues();
					values.put(TourneyManagerContract.MatchesEntry.COLUMN_TOURNAMENT_ID, prefs.getInt("tourney_id", 0));
					values.put(TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_1, player1);
					values.put(TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_2, player2);
					getActivity().getContentResolver().insert(TourneyManagerContract.MatchesEntry.CONTENT_URI, values);
				}
			}
		}
	}

	private void printMatches() {
		Cursor matchCursor = getActivity().getContentResolver().query(TourneyManagerContract.MatchesEntry.CONTENT_URI,
				MATCH_COLUMNS,
				selectionMatchesByID,
				new String[] {prefs.getInt("tourney_id", 0) + ""},
				null);
		matchList.setAdapter(new ManagerMatchAdapter(getActivity().getApplicationContext(), matchCursor));
	}

	private boolean areWinsEqual() {
		HashSet<Integer> winTotals = new HashSet<>();
		for (int wins : playerWins.values()) {
			winTotals.add(wins);
		}
		return winTotals.size() == 1;
	}

	private void completeTournament() {

		Toast.makeText(getActivity().getApplicationContext(), "Tournament complete! " + prefs.getString("winner", "") + " is the winner!",
				Toast.LENGTH_LONG).show();

		Cursor tournamentCursor = getActivity().getContentResolver().query(TourneyManagerContract.TournamentsEntry.CONTENT_URI,
				TOURNAMENT_COLUMNS, selectionTournamentByID,
				new String[]{prefs.getInt("tourney_id", 0) + ""}, null);

		tournamentCursor.moveToFirst();

		addPrizes(prefs.getString("winner", ""), tournamentCursor.getInt(2));
		addPrizes(prefs.getString("second_place", ""), tournamentCursor.getInt(3));
		addPrizes(prefs.getString("third_place", ""), tournamentCursor.getInt(4));
		addHouseProfit();
		prefs.edit().putBoolean(getString(R.string.isTournamentActive), false).apply();
		getActivity().finish();
	}

	private void addPrizes(String player, int prizeMoney) {
		Cursor c = getActivity().getContentResolver().query(TourneyManagerContract.PlayersEntry.CONTENT_URI,
				PLAYER_COLUMNS, selectionPlayers, new String[] {player}, null);
		c.moveToFirst();
		int currentWinnings = c.getInt(2);
		ContentValues values = new ContentValues();
		values.put(TourneyManagerContract.PlayersEntry.COLUMN_WINNINGS, currentWinnings + prizeMoney);
		getActivity().getContentResolver().update(TourneyManagerContract.PlayersEntry.CONTENT_URI,
				values,
				selectionPlayers,
				new String[] {player});
	}

	private void addHouseProfit() {
		ContentValues values = new ContentValues();
		values.put(TourneyManagerContract.TournamentsEntry.COLUMN_COMPLETED, 1);
		getActivity().getContentResolver().update(TourneyManagerContract.TournamentsEntry.CONTENT_URI,
				values,
				selectionTournamentByID,
				new String[] {prefs.getInt("tourney_id", 0) + ""});
	}
}
