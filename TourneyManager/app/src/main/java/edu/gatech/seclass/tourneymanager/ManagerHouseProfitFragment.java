package edu.gatech.seclass.tourneymanager;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ManagerHouseProfitFragment extends Fragment {

	private SharedPreferences prefs;
	private ListView houseWinnings;

	private final String[] TOURNAMENT_COLUMNS = {
			TourneyManagerContract.TournamentsEntry._ID,
			TourneyManagerContract.TournamentsEntry.COLUMN_TOURNAMENT_ID,
			TourneyManagerContract.TournamentsEntry.COLUMN_HOUSE_PROFIT,
			TourneyManagerContract.TournamentsEntry.COLUMN_COMPLETED
	};

	private String selectionTournamentByCompleted = TourneyManagerContract.TournamentsEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentsEntry.COLUMN_COMPLETED + " = ?";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_house_profit, container, false);
		houseWinnings = (ListView) view.findViewById(R.id.house_profit);

		Cursor c = getActivity().getContentResolver().query(TourneyManagerContract.TournamentsEntry.CONTENT_URI,
				TOURNAMENT_COLUMNS,
				selectionTournamentByCompleted,
				new String[] {1 + ""},
				null);

		houseWinnings.setAdapter(new SimpleCursorAdapter(getActivity().getApplicationContext(),
				R.layout.list_item_player_winnings,
				c,
				new String[] {TourneyManagerContract.TournamentsEntry.COLUMN_TOURNAMENT_ID,
						TourneyManagerContract.TournamentsEntry.COLUMN_HOUSE_PROFIT},
				new int[] {R.id.winnings_player_name, R.id.winnings_amount},
				0));

		return view;
	}
}
