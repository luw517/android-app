package edu.gatech.seclass.tourneymanager;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by luwang on 2/26/17.
 */

public class Match {
    private String player1;
    private String player2;

    public Match (String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }
    public String getPlayer1() {
        return player1;
    }
    public String getPlayer2() {
        return player2;
    }
    public String getResult(String result) {
        return result;
    }

    @Override
    public String toString() {
        return player1 + " v. " + player2;
    }
}
