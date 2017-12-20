package edu.gatech.seclass.tourneymanager;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;


public class TourneyManagerProvider extends ContentProvider {

	private static UriMatcher uriMatcher = buildUriMatcher();
	private TourneyManagerDbHelper dbHelper;
	public static final int PLAYERS = 100;
	public static final int TOURNAMENT_PLAYERS = 200;
	public static final int TOURNAMENTS = 300;
	public static final int MATCHES = 400;

	@Override
	public boolean onCreate() {
		dbHelper = new TourneyManagerDbHelper(getContext());
		return true;
	}

	public static UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String AUTHORITY = TourneyManagerContract.CONTENT_AUTHORITY;
		matcher.addURI(AUTHORITY, TourneyManagerContract.PATH_PLAYERS, PLAYERS);
		matcher.addURI(AUTHORITY, TourneyManagerContract.PATH_TOURNAMENT_PLAYERS, TOURNAMENT_PLAYERS);
		matcher.addURI(AUTHORITY, TourneyManagerContract.PATH_TOURNAMENTS, TOURNAMENTS);
		matcher.addURI(AUTHORITY, TourneyManagerContract.PATH_MATCHES, MATCHES);
		return matcher;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = uriMatcher.match(uri);
		int rowsUpdated;

		switch(match) {
			case PLAYERS: {
				rowsUpdated = db.update(TourneyManagerContract.PlayersEntry.TABLE_NAME, values, selection,
						selectionArgs);
				break;
			}
			case TOURNAMENT_PLAYERS: {
				rowsUpdated = db.update(TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME, values, selection,
						selectionArgs);
				break;
			}
			case TOURNAMENTS: {
				rowsUpdated = db.update(TourneyManagerContract.TournamentsEntry.TABLE_NAME, values, selection,
						selectionArgs);
				break;
			}
			case MATCHES: {
				rowsUpdated = db.update(TourneyManagerContract.MatchesEntry.TABLE_NAME, values, selection,
						selectionArgs);
				break;
			}
			default:
				throw new UnsupportedOperationException("Unknown URI: " + uri);
		}
		if (rowsUpdated != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsUpdated;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = uriMatcher.match(uri);

		switch (match) {
			case PLAYERS: {
				db.beginTransaction();
				int returnCount = 0;
				try {
					for (ContentValues value : values) {
						long _id = db.insert(TourneyManagerContract.PlayersEntry.TABLE_NAME, null, value);
						if (_id != -1) {
							returnCount++;
						}
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				getContext().getContentResolver().notifyChange(uri, null);
				return returnCount;
			}
			default:
				return super.bulkInsert(uri, values);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = uriMatcher.match(uri);
		int rowsDeleted;

		switch (match) {
			case PLAYERS: {
				rowsDeleted = db.delete(TourneyManagerContract.PlayersEntry.TABLE_NAME, selection, selectionArgs);
				break;
			}
			case TOURNAMENT_PLAYERS: {
				rowsDeleted = db.delete(TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME, selection, selectionArgs);
				break;
			}
			case MATCHES: {
				rowsDeleted = db.delete(TourneyManagerContract.MatchesEntry.TABLE_NAME, selection, selectionArgs);
				break;
			}
			default:
				throw new UnsupportedOperationException("Unknown URI: " + uri);
		}
		if (rowsDeleted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsDeleted;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int match = uriMatcher.match(uri);
		Uri returnUri;

		switch (match) {
			case PLAYERS: {
				long _id = db.insert(TourneyManagerContract.PlayersEntry.TABLE_NAME, null, values);
				if (_id > 0) {
					returnUri = TourneyManagerContract.PlayersEntry.buildPlayersUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into " + uri);
				}
				break;
			}
			case TOURNAMENT_PLAYERS: {
				long _id = db.insert(TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME, null, values);
				if (_id > 0) {
					returnUri = TourneyManagerContract.TournamentPlayersEntry.buildTournamentPlayersUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into " + uri);
				}
				break;
			}
			case TOURNAMENTS: {
				long _id = db.insert(TourneyManagerContract.TournamentsEntry.TABLE_NAME, null, values);
				if (_id > 0) {
					returnUri = TourneyManagerContract.TournamentsEntry.buildTournamentsUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into " + uri);
				}
				break;
			}
			case MATCHES: {
				long _id = db.insert(TourneyManagerContract.MatchesEntry.TABLE_NAME, null, values);
				if (_id > 0) {
					returnUri = TourneyManagerContract.MatchesEntry.buildMatchesUri(_id);
				} else {
					throw new android.database.SQLException("Failed to insert row into " + uri);
				}
				break;
			}
			default:
				Log.e("Provider", "URI Matcher: " + match);
				throw new UnsupportedOperationException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
						String sortOrder) {
		Cursor retCursor;
		final int match = uriMatcher.match(uri);
		final SQLiteDatabase db = dbHelper.getReadableDatabase();

		switch(match) {
			case PLAYERS: {
				retCursor = db.query(
						TourneyManagerContract.PlayersEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder
				);
				break;
			}
			case TOURNAMENT_PLAYERS: {
				retCursor = db.query(
						TourneyManagerContract.TournamentPlayersEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder
				);
				break;
			}
			case TOURNAMENTS: {
				retCursor = db.query(
						TourneyManagerContract.TournamentsEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder
				);
				break;
			}
			case MATCHES: {
				retCursor = db.query(
						TourneyManagerContract.MatchesEntry.TABLE_NAME,
						projection,
						selection,
						selectionArgs,
						null,
						null,
						sortOrder
				);
				break;
			}
			default:
				throw new UnsupportedOperationException("Unknown URI: " + uri);
		}
		retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		return retCursor;
	}

	@Override
	public String getType(Uri uri) {
		final int match = uriMatcher.match(uri);

		switch(match) {
			case PLAYERS: {
				return TourneyManagerContract.PlayersEntry.CONTENT_TYPE;
			}
			case TOURNAMENT_PLAYERS: {
				return TourneyManagerContract.TournamentPlayersEntry.CONTENT_TYPE;
			}
			case TOURNAMENTS: {
				return TourneyManagerContract.TournamentsEntry.CONTENT_TYPE;
			}
			case MATCHES: {
				return TourneyManagerContract.MatchesEntry.CONTENT_TYPE;
			}
			default:
				throw new UnsupportedOperationException("Unknown URI: " + uri);
		}
	}
}