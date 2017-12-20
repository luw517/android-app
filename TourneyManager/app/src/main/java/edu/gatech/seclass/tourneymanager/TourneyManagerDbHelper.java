package edu.gatech.seclass.tourneymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TourneyManagerDbHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 9;
	private static final String DATABASE_NAME = "tourneyManager.db";

	public TourneyManagerDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String SQL_CREATE_PLAYERS_TABLE = "CREATE TABLE " +
				TourneyManagerContract.PlayersEntry.TABLE_NAME + " (" +
				TourneyManagerContract.PlayersEntry._ID + " INTEGER AUTO INCREMENT PRIMARY KEY, " +
				TourneyManagerContract.PlayersEntry.COLUMN_NAME + " STRING NOT NULL, " +
				TourneyManagerContract.PlayersEntry.COLUMN_USERNAME + " STRING NOT NULL, " +
				TourneyManagerContract.PlayersEntry.COLUMN_DECK + " STRING NOT NULL, " +
				TourneyManagerContract.PlayersEntry.COLUMN_WINNINGS + " INTEGER NOT NULL " + ")";

		final String SQL_CREATE_TOURNAMENT_PLAYERS_TABLE = "CREATE TABLE " +
				TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME + " (" +
				TourneyManagerContract.TournamentPlayersEntry._ID + " INTEGER AUTO INCREMENT PRIMARY KEY, " +
				TourneyManagerContract.TournamentPlayersEntry.COLUMN_TOURNAMENT_ID + " INTEGER NOT NULL, " +
				TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER_WINS + " INTEGER NOT NULL, " +
				TourneyManagerContract.TournamentPlayersEntry.COLUMN_PLAYER + " STRING NOT NULL" + ")";

		final String SQL_CREATE_TOURNAMENTS_TABLE = "CREATE TABLE " +
				TourneyManagerContract.TournamentsEntry.TABLE_NAME + " (" +
				TourneyManagerContract.TournamentsEntry._ID + " INTEGER AUTO INCREMENT PRIMARY KEY, " +
				TourneyManagerContract.TournamentsEntry.COLUMN_TOURNAMENT_ID + " INTEGER NOT NULL, " +
				TourneyManagerContract.TournamentsEntry.COLUMN_HOUSE_PROFIT + " INTEGER NOT NULL, " +
				TourneyManagerContract.TournamentsEntry.COLUMN_FIRST_PRIZE + " INTEGER NOT NULL, " +
				TourneyManagerContract.TournamentsEntry.COLUMN_SECOND_PRIZE + " INTEGER NOT NULL, " +
				TourneyManagerContract.TournamentsEntry.COLUMN_THIRD_PRIZE + " INTEGER NOT NULL, " +
				TourneyManagerContract.TournamentsEntry.COLUMN_COMPLETED + " INTEGER NOT NULL" + ")";

		final String SQL_CREATE_MATCHES_TABLE = "CREATE TABLE " +
				TourneyManagerContract.MatchesEntry.TABLE_NAME + " (" +
				TourneyManagerContract.MatchesEntry._ID + " INTEGER AUTO INCREMENT PRIMARY KEY, " +
				TourneyManagerContract.MatchesEntry.COLUMN_TOURNAMENT_ID + " INTEGER NOT NULL, " +
				TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_1 + " STRING NOT NULL, " +
				TourneyManagerContract.MatchesEntry.COLUMN_PLAYER_2 + " STRING NOT NULL" + ")";

		db.execSQL(SQL_CREATE_PLAYERS_TABLE);
		db.execSQL(SQL_CREATE_TOURNAMENT_PLAYERS_TABLE);
		db.execSQL(SQL_CREATE_TOURNAMENTS_TABLE);
		db.execSQL(SQL_CREATE_MATCHES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TourneyManagerContract.PlayersEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TourneyManagerContract.TournamentsEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TourneyManagerContract.MatchesEntry.TABLE_NAME);
		onCreate(db);
	}
}

