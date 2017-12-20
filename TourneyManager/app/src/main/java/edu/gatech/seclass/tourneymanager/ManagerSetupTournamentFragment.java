package edu.gatech.seclass.tourneymanager;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ManagerSetupTournamentFragment extends Fragment implements View.OnClickListener {

	private static View view;
	private Button startTournament, playerList;
	private EditText housecut, entryprice;
	private SharedPreferences prefs;
	private int houseProfit, firstPrize, secondPrize, thirdPrize;

	private String[] TOURNAMENT_PLAYER_COLUMNS = {
			TourneyManagerContract.TournamentPlayersEntry._ID,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER
	};

	private String selectionTournamentByID = TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID + " = ?";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.fragment_tournament_setup, container, false);

		startTournament = (Button) view.findViewById(R.id.start_tournament);
		playerList = (Button) view.findViewById(R.id.player_list);

		entryprice = (EditText) view.findViewById(R.id.entry_price);
		housecut = (EditText) view.findViewById(R.id.house_cut);
		
		startTournament.setOnClickListener(this);
		playerList.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.start_tournament: {
				if (entryprice.getText().toString().length() == 0 || Integer.parseInt(entryprice.getText().toString()) <1 ){
					entryprice.setError("Invalid Entry Price");
					break;
				}

				if (housecut.getText().toString().length() == 0) {
					housecut.setError("Invalid House Cut");
					break;
				}

				Cursor tourneyPlayers = getActivity().getContentResolver().query(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
						TOURNAMENT_PLAYER_COLUMNS, selectionTournamentByID, new String[] {prefs.getInt("tourney_id", 0) + ""}, null);

				int entryPrice = Integer.parseInt(entryprice.getText().toString());
				int numPlayers = tourneyPlayers.getCount();
				int totalMoney = entryPrice * numPlayers;

				houseProfit = Math.round(totalMoney * Integer.parseInt(housecut.getText().toString())/100);

				int remainingMoney = totalMoney - houseProfit;
				firstPrize = (int) Math.round((remainingMoney) * 0.5);
				secondPrize = (int) Math.round((remainingMoney) * 0.3);
				thirdPrize = (int) Math.round((remainingMoney) * 0.2);

				ContentValues values = new ContentValues();
				values.put(TourneyManagerContract.TournamentsEntry.COLUMN_TOURNAMENT_ID, prefs.getInt("tourney_id", 0));
				values.put(TourneyManagerContract.TournamentsEntry.COLUMN_HOUSE_PROFIT, houseProfit);
				values.put(TourneyManagerContract.TournamentsEntry.COLUMN_FIRST_PRIZE, firstPrize);
				values.put(TourneyManagerContract.TournamentsEntry.COLUMN_SECOND_PRIZE, secondPrize);
				values.put(TourneyManagerContract.TournamentsEntry.COLUMN_THIRD_PRIZE, thirdPrize);
				values.put(TourneyManagerContract.TournamentsEntry.COLUMN_COMPLETED, 0);

				getActivity().getContentResolver().insert(TourneyManagerContract.TournamentsEntry.CONTENT_URI,
						values);


				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.manager_container, new ManagerMatchListFragment()).commit();
				prefs.edit().putBoolean(getString(R.string.isTournamentActive), true).apply();
				prefs.edit().putInt("tournament_players", tourneyPlayers.getCount()).apply();
				break;
			}
			case R.id.player_list: {
				getActivity().getFragmentManager().beginTransaction()
						.addToBackStack(null)
						.replace(R.id.manager_container, new ManagerPlayerListFragment()).commit();
				break;
			}
		}
	}

}
