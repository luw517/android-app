package edu.gatech.seclass.tourneymanager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by luwang on 2/25/17.
 */
public class PlayerTest {

    private Player player = new Player("Anna", "A", "1234567", "Buzz") ;

    @Test
    public void getName() throws Exception {
        assertEquals("Anna", player.getName());
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals("A", player.getUsername());
    }

    @Test
    public void getPhone() throws Exception {
        assertEquals("1234567", player.getPhone());
    }

    @Test
    public void deck() throws Exception {
        assertEquals("Buzz", player.getDeck());
    }

}