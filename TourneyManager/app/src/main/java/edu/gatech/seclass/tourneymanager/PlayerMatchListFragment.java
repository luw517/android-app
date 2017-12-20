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

import edu.gatech.seclass.tourneymanager.R;


/**
 * Created by ezj182 on 2/25/17.
 */

public class PlayerMatchListFragment extends Fragment {

	private static View view;

	private String[] MATCH_COLUMNS = {
			TourneyManagerContract.MatchesEntry._ID,
			TourneyManagerContract.MatchesEntry.COLUMN_TOURNAMENT_ID,
			TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_1,
			TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_2
	};

	private String selectionMatchesByID = TourneyManagerContract.MatchesEntry.TABLE_NAME + "." +
			TourneyManagerContract.MatchesEntry.COLUMN_TOURNAMENT_ID + " = ?";

	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		view = inflater.inflate(R.layout.fragment_player_match_list, container, false);

		ListView matchList = (ListView) view.findViewById(R.id.player_match_list);

		Cursor matchCursor = getActivity().getContentResolver().query(TourneyManagerContract.MatchesEntry.CONTENT_URI,
				MATCH_COLUMNS,
				selectionMatchesByID,
				new String[] {prefs.getInt("tourney_id", 0) + ""},
				null);
		matchList.setAdapter(new PlayerMatchAdapter(getActivity().getApplicationContext(), matchCursor));

		return view;
	}
}
