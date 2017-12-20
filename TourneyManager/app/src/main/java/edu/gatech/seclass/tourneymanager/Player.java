package edu.gatech.seclass.tourneymanager;

/**
 * Created by luwang on 2/25/17.
 */

public class Player {
    String name;
    String username;
    String phone;
    String deck;

    public Player(String name, String username, String phone, String deck) {
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.deck = deck;
    }

    public String getName(){
        return name;
    }
    public String getUsername() {
        return username;
    }
    public String getPhone() {
        return phone;
    }
    public String getDeck() {
        return deck;
    }




}
