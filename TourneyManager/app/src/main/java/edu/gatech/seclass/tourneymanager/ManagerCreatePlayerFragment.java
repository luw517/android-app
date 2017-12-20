package edu.gatech.seclass.tourneymanager;

import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class ManagerCreatePlayerFragment extends Fragment implements View.OnClickListener {

	private EditText playerName, userName, phoneNumber;
	private Spinner deckSpinner;

	private String[] PLAYER_COLUMNS = {
			TourneyManagerContract.PlayersEntry._ID,
			TourneyManagerContract.PlayersEntry.COLUMN_USERNAME};

	private ContentResolver resolver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resolver = getActivity().getContentResolver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_manager_create_player, container, false);

		Button submit = (Button) view.findViewById(R.id.buttonSubmit);
		playerName = (EditText) view.findViewById(R.id.playerName);
		userName = (EditText) view.findViewById(R.id.userName);
		phoneNumber = (EditText) view.findViewById(R.id.phone_number);
		deckSpinner = (Spinner) view.findViewById(R.id.spinnerdeckOpt);

		submit.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case (R.id.buttonSubmit): {
				performValidation();

				String selectionPlayerByUsername = TourneyManagerContract.PlayersEntry.TABLE_NAME + "." +
						TourneyManagerContract.PlayersEntry.COLUMN_USERNAME + " = ?";

				// Query for all created players in the application
				Cursor currentPlayers = resolver.query(TourneyManagerContract.PlayersEntry.CONTENT_URI,
						PLAYER_COLUMNS,
						selectionPlayerByUsername,
						new String[]{userName.getText().toString()},
						null);

				// Make sure no players have the desired username. If not, add them to the database.
				if (currentPlayers != null && currentPlayers.getCount() != 0) {
					Toast.makeText(getActivity().getApplicationContext(), "Username is taken.",
							Toast.LENGTH_LONG).show();
				} else {
					ContentValues values = new ContentValues();
					values.put(TourneyManagerContract.PlayersEntry.COLUMN_NAME, playerName.getText().toString());
					values.put(TourneyManagerContract.PlayersEntry.COLUMN_USERNAME, userName.getText().toString());
					values.put(TourneyManagerContract.PlayersEntry.COLUMN_WINNINGS, 0);
					values.put(TourneyManagerContract.PlayersEntry.COLUMN_DECK, deckSpinner.toString());

					resolver.insert(TourneyManagerContract.PlayersEntry.CONTENT_URI, values);

					clearForm();
					if (currentPlayers != null) {
						currentPlayers.close();
					}
					break;
				}
			}
		}
	}

	private void clearForm() {
		playerName.setText("");
		userName.setText("");
		phoneNumber.setText("");
	}

	private void performValidation() {
		if (playerName.getText().toString().length() == 0) {
			playerName.setError("Invalid Player Name");
		}

		if (userName.getText().toString().length() == 0){
			userName.setError("Invalid Username");
		}
		if (phoneNumber.getText().toString().length() == 0) {
			phoneNumber.setError("Invalid Phone");
		}
	}
}
