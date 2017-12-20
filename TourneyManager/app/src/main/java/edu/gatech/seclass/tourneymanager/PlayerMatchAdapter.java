package edu.gatech.seclass.tourneymanager;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class PlayerMatchAdapter extends CursorAdapter {

	public PlayerMatchAdapter(Context context, Cursor cursor) {
		super(context, cursor, 0);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(R.layout.list_item_player_match, parent, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView matchText = (TextView) view.findViewById(R.id.player_match_text);

		matchText.setText(cursor.getString(cursor.getColumnIndex(TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_1)) + " v. " +
				cursor.getString(cursor.getColumnIndex(TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_2)));
	}
}
