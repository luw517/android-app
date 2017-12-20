package edu.gatech.seclass.tourneymanager;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PlayerWinningsAdapter extends CursorAdapter {

	public PlayerWinningsAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.list_item_player_winnings, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView playerName = (TextView) view.findViewById(R.id.winnings_player_name);
		TextView playerWinnings = (TextView) view.findViewById(R.id.winnings_amount);

		playerName.setText(cursor.getString(cursor.getColumnIndexOrThrow(TourneyManagerContract.PlayersEntry.COLUMN_NAME)));
		playerWinnings.setText(cursor.getInt(cursor.getColumnIndexOrThrow(TourneyManagerContract.PlayersEntry.COLUMN_WINNINGS)) + "");
	}
}
