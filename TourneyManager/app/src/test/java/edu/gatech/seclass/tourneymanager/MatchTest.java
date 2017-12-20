package edu.gatech.seclass.tourneymanager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by luwang on 2/26/17.
 */
public class MatchTest {

    private Match match = new Match( "player1", "player2");



    @Test
    public void getPlayer1() throws Exception {
        assertEquals("player1", match.getPlayer1());
    }

    @Test
    public void getPlayer2() throws Exception {
        assertEquals("player2", match.getPlayer2());
    }

    @Test
    public void getResult() throws Exception {
        assertEquals("player1 win", match.getResult("player1 win"));
    }

}