package edu.gatech.seclass.tourneymanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseModeActivity extends AppCompatActivity implements View.OnClickListener {

	private Button chooseManager, choosePlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_mode);

		chooseManager = (Button) findViewById(R.id.choose_manager);
		choosePlayer = (Button) findViewById(R.id.choose_player);

		chooseManager.setOnClickListener(this);
		choosePlayer.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// Navigate to either the ManagerActivity or the PlayerActivity, depending on the mode
		// selected
		switch (v.getId()) {
			case R.id.choose_manager: {
				Intent startManagerMode = new Intent(this, ManagerActivity.class);
				startActivity(startManagerMode);
				break;
			}
			case R.id.choose_player: {
				Intent startPlayerMode = new Intent(this, PlayerActivity.class);
				startActivity(startPlayerMode);
				break;
			}
		}
	}
}
