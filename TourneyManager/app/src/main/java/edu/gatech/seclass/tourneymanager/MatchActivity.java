package edu.gatech.seclass.tourneymanager;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MatchActivity extends AppCompatActivity implements View.OnClickListener {

	private Button cancelMatch, startMatch;
	private TextView matchInstructions, player1, player2;
	private SharedPreferences prefs;
	private String match_name = "";

	private String[] MATCH_COLUMNS = {
			TourneyManagerContract.MatchesEntry._ID,
			TourneyManagerContract.MatchesEntry.COLUMN_TOURNAMENT_ID,
			TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_1,
			TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_2
	};

	private String[] TOURNAMENT_PLAYER_COLUMNS = {
			TourneyManagerContract.TournamentPlayersEntry._ID,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID,
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER_WINS
	};

	private String selectionPlayerByTourneyID = TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID + " = ? AND " +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER + " = ?";

	private String selectionPlayerWins = TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME + "." +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID + " = ? AND " +
			TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER + " = ?";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Bundle bundle = getIntent().getExtras();
		setContentView(R.layout.activity_match);
		match_name = bundle.getString("match_name");

		cancelMatch = (Button) findViewById(R.id.match_cancel_match);
		startMatch = (Button) findViewById(R.id.match_start_match);
		matchInstructions = (TextView) findViewById(R.id.match_instructions);
		player1 = (TextView) findViewById(R.id.match_player_1);
		player2 = (TextView) findViewById(R.id.match_player_2);

		player1.setText((String) bundle.get("player_1"));
		player2.setText((String) bundle.get("player_2"));

		cancelMatch.setOnClickListener(this);
		startMatch.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.match_cancel_match: {
				this.finish();
				break;
			}
			case R.id.match_start_match: {
				startMatch.setVisibility(View.GONE);
				matchInstructions.setText("Click the winning player to complete the match!");
				player1.setOnClickListener(this);
				player2.setOnClickListener(this);
				player1.setBackgroundColor(Color.LTGRAY);
				player2.setBackgroundColor(Color.LTGRAY);
				break;
			}
			case R.id.match_player_1: {
				String winningPlayer = player1.getText().toString();
				String losingPlayer = player2.getText().toString();
				if (match_name.equals("first_round") || match_name.equals("second_round")) {
					incrementWins(winningPlayer, 50);
					incrementWins(losingPlayer, 25);
				} else if (match_name.equals("championship")) {
					prefs.edit().putString("winner", winningPlayer).apply();
					prefs.edit().putString("second_place", losingPlayer).apply();
					deleteLoser(losingPlayer);
					deleteLoser(winningPlayer);
				} else if (match_name.equals("third_place")) {
					prefs.edit().putString("third_place", winningPlayer).apply();
					deleteLoser(losingPlayer);
					deleteLoser(winningPlayer);
				}
				else {
					deleteLoser(losingPlayer);
					incrementWins(winningPlayer, 1);
				}
				this.finish();
				break;
			}
			case R.id.match_player_2: {
				String winningPlayer = player2.getText().toString();
				String losingPlayer = player1.getText().toString();
				if (match_name.equals("first_round") || match_name.equals("second_round")) {
					incrementWins(winningPlayer, 50);
					incrementWins(losingPlayer, 25);
				} else if (match_name.equals("championship")) {
					prefs.edit().putString("winner", winningPlayer).apply();
					prefs.edit().putString("second_place", losingPlayer).apply();
					deleteLoser(losingPlayer);
					deleteLoser(winningPlayer);
				} else if (match_name.equals("third_place")) {
					prefs.edit().putString("third_place", winningPlayer).apply();
					deleteLoser(losingPlayer);
					deleteLoser(winningPlayer);
				}
				else {
					deleteLoser(losingPlayer);
					incrementWins(winningPlayer, 1);
				}
				this.finish();
				break;
			}
		}
	}

	/**
	 * Deletes the losing player from the tournament (Tested and functional)
	 * @param losingPlayer
	 */
	private void deleteLoser(String losingPlayer) {
		getContentResolver().delete(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
				selectionPlayerByTourneyID,
				new String[] {prefs.getInt("tourney_id", 0) + "", losingPlayer});
	}

	/**
	 * Increments the tournament wins of the winning player (Tested and functional)
	 * @param winningPlayer
	 */
	private void incrementWins(String winningPlayer, int wins) {
		ContentValues values = new ContentValues();
		Cursor playerWins = getContentResolver().query(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
				TOURNAMENT_PLAYER_COLUMNS,
				selectionPlayerWins,
				new String[] {prefs.getInt("tourney_id", 0) + "", winningPlayer},
				null);
		if (playerWins != null) {
			playerWins.moveToFirst();
			values.put(TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER_WINS, playerWins.getInt(3) + wins);
			getContentResolver().update(TourneyManagerContract.TournamentPlayersEntry.CONTENT_URI,
					values,
					selectionPlayerWins,
					new String[] {prefs.getInt("tourney_id", 0) + "", winningPlayer});
		}
		playerWins.close();
	}
}