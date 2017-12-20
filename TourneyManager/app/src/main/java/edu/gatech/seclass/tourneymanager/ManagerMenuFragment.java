package edu.gatech.seclass.tourneymanager;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ManagerMenuFragment extends Fragment implements View.OnClickListener {

	private static View view;
	private Button startTournament, pastProfits, playerTotals, createPlayer;
	private SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_manager_menu, container, false);

		// Initialize buttons
		startTournament = (Button) view.findViewById(R.id.start_tournament);
		pastProfits = (Button) view.findViewById(R.id.view_past_profits);
		playerTotals = (Button) view.findViewById(R.id.view_player_totals);
		createPlayer = (Button) view.findViewById(R.id.create_players);

		// Add OnClickListener
		startTournament.setOnClickListener(this);
		pastProfits.setOnClickListener(this);
		playerTotals.setOnClickListener(this);
		createPlayer.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.start_tournament: {
				prefs.edit().putInt("tourney_id", prefs.getInt("tourney_id", 0) + 1).apply();
				getActivity().getFragmentManager().beginTransaction()
						.addToBackStack(null)
						.replace(R.id.manager_container, new ManagerSetupTournamentFragment()).commit();
				break;
			}
			case R.id.view_past_profits: {
				getActivity().getFragmentManager().beginTransaction()
						.addToBackStack(null)
						.replace(R.id.manager_container, new ManagerHouseProfitFragment()).commit();
				break;
			}
			case R.id.view_player_totals: {
				getActivity().getFragmentManager().beginTransaction()
						.addToBackStack(null)
						.replace(R.id.manager_container, new PlayerWinningsFragment()).commit();
				break;
			}
			case R.id.create_players: {
				getActivity().getFragmentManager().beginTransaction()
						.addToBackStack(null)
						.replace(R.id.manager_container, new ManagerCreatePlayerFragment()).commit();
				break;
			}
		}
	}

}
