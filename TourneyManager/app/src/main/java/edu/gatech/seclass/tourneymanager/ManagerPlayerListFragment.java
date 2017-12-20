package edu.gatech.seclass.tourneymanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerPlayerListFragment extends Fragment implements AdapterView.OnItemClickListener {

	private View view;
	private ListView availablePlayers, tournamentPlayers;
	private SharedPreferences prefs;

	private String[] PLAYER_COLUMNS = {
			TourneyManagerContract.PlayersEntry._ID,
			TourneyManagerContract.PlayersEntry.COLUMN_USERNAME};

	private String[] TOURNAMENT_PLAYER_COLUMNS = {
			TourneyManagerContract.TournamentPlayersEntry._ID,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID
	};

	private String selectionTournamentByID = TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID + " = ?";

	private String selectionPlayerByID = TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER + " = ? AND " +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID + " = ?";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_manager_player_list, container, false);

		availablePlayers = (ListView) view.findViewById(R.id.availablePlayers);
		tournamentPlayers = (ListView) view.findViewById(R.id.tournamentPlayers);

		availablePlayers.setOnItemClickListener(this);
		tournamentPlayers.setOnItemClickListener(this);

		Cursor availableCursor = getActivity().getContentResolver().query(TourneyManagerContract.PlayersEntry.CONTENT_URI,
				PLAYER_COLUMNS, null, null, null);

		Cursor tournamentCursor = getActivity().getContentResolver().query(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
				TOURNAMENT_PLAYER_COLUMNS, selectionTournamentByID, new String[]{prefs.getInt("tourney_id", 0) + ""}, null);

		availablePlayers.setAdapter(new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.list_item_player,
				availableCursor, new String[]{TourneyManagerContract.PlayersEntry.COLUMN_USERNAME},
				new int[]{R.id.add_to_tournament_player_name}, 0));

		tournamentPlayers.setAdapter(new SimpleCursorAdapter(getActivity().getApplicationContext(), R.layout.list_item_player,
				tournamentCursor, new String[] {TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER},
				new int[]{R.id.add_to_tournament_player_name}, 0));

		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch(parent.getId()) {
			case R.id.availablePlayers: {
				LinearLayout linear = (LinearLayout) parent.getChildAt(position);
				TextView user = (TextView) linear.getChildAt(0);
				ContentValues values = new ContentValues();
				values.put(TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID, prefs.getInt("tourney_id", 0));
				values.put(TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER, user.getText().toString());
				values.put(TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER_WINS, 0);

				if (getActivity().getContentResolver().query(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
						TOURNAMENT_PLAYER_COLUMNS, selectionPlayerByID, new String[] {user.getText().toString(), prefs.getInt("tourney_id", 0) + ""}, null).getCount() != 0) {
					Toast.makeText(getActivity().getApplicationContext(), "User is already in the tournament.", Toast.LENGTH_LONG).show();
				} else {
					getActivity().getContentResolver().insert(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI, values);
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.detach(this).attach(this).commit();
				}
				break;
			}
			case R.id.tournamentPlayers: {
				LinearLayout linear = (LinearLayout) parent.getChildAt(position);
				TextView user = (TextView) linear.getChildAt(0);
				getActivity().getContentResolver().delete(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
						selectionPlayerByID, new String[] {user.getText().toString(), prefs.getInt("tourney_id", 0) + ""});
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.detach(this).attach(this).commit();
				break;
			}
		}

	}
}
