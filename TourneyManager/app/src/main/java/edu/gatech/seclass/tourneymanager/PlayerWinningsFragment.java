package edu.gatech.seclass.tourneymanager;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class PlayerWinningsFragment extends Fragment {

	private String[] PLAYER_COLUMNS = {
			TourneyManagerContract.PlayersEntry._ID,
			TourneyManagerContract.PlayersEntry.COLUMN_NAME,
			TourneyManagerContract.PlayersEntry.COLUMN_WINNINGS};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_player_winnings, container, false);

		ListView winningsList = (ListView) view.findViewById(R.id.player_winnings);

		Cursor allPlayers = getActivity().getContentResolver().query(TourneyManagerContract.PlayersEntry.CONTENT_URI,
				PLAYER_COLUMNS, null, null, TourneyManagerContract.PlayersEntry.COLUMN_WINNINGS + " DESC");

		PlayerWinningsAdapter adapter = new PlayerWinningsAdapter(getActivity(), allPlayers);

		winningsList.setAdapter(adapter);

		return view;
	}
}
