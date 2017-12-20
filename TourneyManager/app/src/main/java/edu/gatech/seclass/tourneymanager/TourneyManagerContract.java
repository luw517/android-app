package edu.gatech.seclass.tourneymanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class TourneyManagerContract {

	public static final String CONTENT_AUTHORITY = "edu.gatech.seclass.tourneymanager.app";
	private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_PLAYERS = "players";
	public static final String PATH_TOURNAMENT_PLAYERS = "tournamentPlayers";
	public static final String PATH_TOURNAMENTS = "tournaments";
	public static final String PATH_MATCHES = "matches";

	public static final class PlayersEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_PLAYERS).build();

		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
				CONTENT_AUTHORITY + "/" + PATH_PLAYERS;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
				CONTENT_AUTHORITY + "/" + PATH_PLAYERS;

		public static final String TABLE_NAME = "players";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_USERNAME = "username";
		public static final String COLUMN_DECK = "deck";
		public static final String COLUMN_WINNINGS = "winnings";

		public static Uri buildPlayersUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}

	public static final class TournamentPlayersEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_TOURNAMENT_PLAYERS).build();

		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
				CONTENT_AUTHORITY + "/" + PATH_TOURNAMENT_PLAYERS;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
				CONTENT_AUTHORITY + "/" + PATH_TOURNAMENT_PLAYERS;

		public static final String TABLE_NAME = "tournamentPlayers";
		public static final String COLUMN_TOURNAMENT_ID = "tournament_id";
		public static final String COLUMN_PLAYER = "player";
		public static final String COLUMN_PLAYER_WINS = "player_wins";

		public static Uri buildTournamentPlayersUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}

	public static final class TournamentsEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_TOURNAMENTS).build();

		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
				CONTENT_AUTHORITY + "/" + PATH_TOURNAMENTS;
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
				CONTENT_AUTHORITY + "/" + PATH_TOURNAMENTS;

		public static final String TABLE_NAME = "tournaments";
		public static final String COLUMN_TOURNAMENT_ID = "tournament_id";
		public static final String COLUMN_HOUSE_PROFIT = "house_profit";
		public static final String COLUMN_FIRST_PRIZE = "first_prize";
		public static final String COLUMN_SECOND_PRIZE = "second_prize";
		public static final String COLUMN_THIRD_PRIZE = "third_prize";
		public static final String COLUMN_COMPLETED = "completed";

		public static Uri buildTournamentsUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}

	protected static final class MatchesEntry implements BaseColumns {
		protected static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
				.appendPath(PATH_MATCHES).build();

		protected static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
				CONTENT_AUTHORITY + "/" + PATH_MATCHES;

		public static final String TABLE_NAME = "matches";
		public static final String COLUMN_TOURNAMENT_ID = "tournament_id";
		public static final String COLUMN_PLAYER_1 = "player_1";
		public static final String COLUMN_PLAYER_2 = "player_2";

		public static Uri buildMatchesUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}



}