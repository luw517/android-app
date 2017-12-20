package edu.gatech.seclass.tourneymanager;


import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Set;

/**
 * Created by luwang on 3/4/17.
 */

@RunWith(AndroidJUnit4.class)

public class TourneyManagerProviderTest extends ProviderTestCase2<TourneyManagerProvider> {
    private static final String TEST_PLAYER_NAME = "P1";

    final static ContentValues values = new ContentValues();

    public TourneyManagerProviderTest() {
        super(TourneyManagerProvider.class, TourneyManagerContract.CONTENT_AUTHORITY);
    }

    @Before
    @Override
    public void setUp() throws Exception{
        setContext(InstrumentationRegistry.getTargetContext());
        super.setUp();
    }

    @Test
    public void emptyQuery(){
        MockContentResolver contentResolver = getMockContentResolver();
        assertNotNull(contentResolver);//fail happens here
        Uri uri = TourneyManagerContract.PlayersEntry.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null,null,null,null);
    }

    @Test
    //Test getType() method
    public void testGetType(){
        String type = mContext.getContentResolver().getType(TourneyManagerContract.PlayersEntry.CONTENT_URI);
        assertEquals(TourneyManagerContract.PlayersEntry.CONTENT_TYPE, type);

    }

    @Test
    //Test delete method
    public void testDelete(){
        mContext.getContentResolver().delete(
                TourneyManagerContract.PlayersEntry.CONTENT_URI,
                null,
                null
        );


        // Ensure players were deleted
        Cursor cursor = mContext.getContentResolver().query(
                TourneyManagerContract.PlayersEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    @Test
    //test insert and query
    public void testInsert(){
        ContentValues playerContentValues = getPlayerContentValues();
        Uri playerInsertUri = mContext.getContentResolver().insert(TourneyManagerContract.PlayersEntry.CONTENT_URI, playerContentValues);
        long playerRowId = ContentUris.parseId(playerInsertUri);

        // Verify we inserted a row
        assertTrue(playerRowId > 0);

        // Query for all rows and validate cursor
        Cursor playerCursor = mContext.getContentResolver().query(
                TourneyManagerContract.PlayersEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        validateCursor(playerCursor, playerContentValues);
        playerCursor.close();

    }

    private ContentValues getPlayerContentValues(){
        ContentValues values = new ContentValues();
        values.put(TourneyManagerContract.PlayersEntry.COLUMN_NAME, TEST_PLAYER_NAME);
        values.put(TourneyManagerContract.PlayersEntry.COLUMN_USERNAME, "p01");
        values.put(TourneyManagerContract.PlayersEntry.COLUMN_DECK, "deck");
        values.put(TourneyManagerContract.PlayersEntry.COLUMN_WINNINGS, "1");
        return values;
    }

    private void validateCursor(Cursor valueCursor, ContentValues expectedValues){
        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for(Map.Entry<String, Object> entry : valueSet){
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            switch(valueCursor.getType(idx)){
                case Cursor.FIELD_TYPE_FLOAT:
                    assertEquals(entry.getValue(), valueCursor.getDouble(idx));
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    assertEquals(Integer.parseInt(entry.getValue().toString()), valueCursor.getInt(idx));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    assertEquals(entry.getValue(), valueCursor.getString(idx));
                    break;
                default:
                    assertEquals(entry.getValue().toString(), valueCursor.getString(idx));
                    break;
            }
        }
        valueCursor.close();
    }

}
