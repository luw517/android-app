package edu.gatech.seclass.tourneymanager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by luwang on 2/25/17.
 */
public class ManagerTest {

    //Create Manager object
    private Manager manager ;

    @Before
    public void setUp() {
        manager = new Manager();
    }

    @After
    public void tearDown() {
        manager = null;
    }

    @Test
    //Check if the manager is able to add players
    public void getPlayer() throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Keyur");
        list.add("Austin");
        list.add("Yuntao");
        list.add("Lu");
        manager.addPlayer("Keyur");
        manager.addPlayer("Austin");
        manager.addPlayer("Yuntao");
        manager.addPlayer("Lu");
        assertEquals(list , manager.getPlayer());

    }

    @Test
    //Check if the manager is able to add and remove players
    public void removePlayer() throws Exception {
        ArrayList<String> list = new ArrayList<String>();
        list.add("Keyur");
        list.add("Austin");
        list.add("Yuntao");
        manager.addPlayer("Keyur");
        manager.addPlayer("Austin");
        manager.addPlayer("Yuntao");
        manager.addPlayer("Lu");
        manager.removePlayer("Lu");
        assertEquals(list , manager.getPlayer());

    }

    @Test
    //Check if the manager is able to start a tournament
    public void getTournamentStatus1() throws Exception {
        manager.startTournament();
        assertTrue(manager.getTournamentStatus());
    }

    @Test
    //Check if the manager is able to start and end a tournament
    public void getTournamentStatus2() throws Exception {
        manager.startTournament();
        manager.endTournament();
        assertFalse(manager.getTournamentStatus());
    }

    @Test
    //Check if the manager is able to start a match.
    public void getMatchStatus() throws Exception {
        manager.startMatch();
        assertTrue(manager.getMatchStatus());
    }

    @Test
    //Check if the manager is able to manage a match
    public void endMatch() throws Exception {
        manager.startMatch();
        String res = manager.endMatch("player1 win");
        assertEquals("player1 win", res);
    }

    @Test
    //Verify if the totalPrize is caculated correctly
    public void getTotalPrize() throws Exception {
        manager.addPlayer("Keyur");
        manager.addPlayer("Austin");
        manager.addPlayer("Yuntao");
        manager.addPlayer("Lu");
        int result = manager.getTotalPrize(10, 10);
        assertEquals(36 , result);
    }

    @Test
    //Verify if the houseProfit is caculated correctly
    public void getHouseProfits() throws Exception {
        manager.addPlayer("Keyur");
        manager.addPlayer("Austin");
        manager.addPlayer("Yuntao");
        manager.addPlayer("Lu");
        int result = manager.getHouseProfits(10, 10);
        assertEquals(4, result);
    }



}