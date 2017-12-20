package edu.gatech.seclass.tourneymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;


public class ManagerActivity extends AppCompatActivity {

	private SharedPreferences prefs;
	private boolean isTournamentActive;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager_base);

		// Load the Application SharedPreferences
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

		// Check SharedPreferences for ongoing tournament status
		isTournamentActive = prefs.getBoolean(getString(R.string.isTournamentActive), false);

		// If a tournament is active, show the matches, otherwise show the menu
		if (isTournamentActive) {
			getFragmentManager().beginTransaction()
					.add(R.id.manager_container, new ManagerMatchListFragment()).commit();
		} else {
			getFragmentManager().beginTransaction()
					.add(R.id.manager_container, new ManagerMenuFragment()).commit();
		}
	}
}
