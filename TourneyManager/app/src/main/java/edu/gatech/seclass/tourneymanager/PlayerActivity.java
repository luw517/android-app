package edu.gatech.seclass.tourneymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class PlayerActivity extends AppCompatActivity {

	private SharedPreferences prefs;
	private boolean isTournamentActive;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_base);

		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		isTournamentActive = prefs.getBoolean(getString(R.string.isTournamentActive), false);

		if (isTournamentActive) {
			getFragmentManager().beginTransaction()
					.add(R.id.player_container, new PlayerMatchListFragment()).commit();
		} else {
			getFragmentManager().beginTransaction()
					.add(R.id.player_container, new PlayerWinningsFragment()).commit();
		}
	}
}
