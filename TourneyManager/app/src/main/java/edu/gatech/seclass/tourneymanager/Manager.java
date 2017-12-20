package edu.gatech.seclass.tourneymanager;

/**
 * Created by luwang on 2/25/17.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by luwang on 2/25/17.
 */
public class Manager {
    private ArrayList<String> players = new ArrayList<String>();
    private boolean startT;
    private boolean startM;
    private int numPlayers;
    private HashMap<String, Integer> playerTotal = new HashMap<>();

    public void addPlayer(String player) {
        players.add(player);
        numPlayers++;
    }
    public void removePlayer(String player) {
        int index = players.indexOf(player);
        players.remove(index);
        numPlayers--;
    }
    public ArrayList<String> getPlayer() {
        return players;
    }
    public void startTournament() {

        if (startT == false)  startT = true;
    }
    public void endTournament() {
        if (startT == true)   startT = false;
    }
    public void startMatch() {
        if (startM == false)  startM = true;
    }
    public String endMatch(String result) {
        if (startM == true)   startM = false;
        return result;
    }
    public boolean getMatchStatus() {return startM;}
    public boolean getTournamentStatus () {
        return startT;
    }


    public int getTotalPrize(int houseCut, int entryPrice) {

        double result = entryPrice*numPlayers*(1-houseCut*0.01);
        return (int) Math.round(result);

    }
    public int getHouseProfits (int houseCut, int entryPrice) {
        double result = entryPrice*numPlayers*houseCut*0.01;
        return (int) Math.round(result);
    }
    public ArrayList<Double> viewIndividualPrize()throws ClassCastException, SQLException {
        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/stock;instance=SQLEXPRESS;user=sa;password=password"); // database

        Statement stm = conn.createStatement();

        String individual_prizes = "select * from individual prizes"; // database

        ResultSet rst = stm.executeQuery(individual_prizes);

        ArrayList<Double> individual_prize_list = new ArrayList<>();

        while(rst.next()){
            Double prize = rst.getDouble("prize");
            individual_prize_list.add(prize);
        }

        return individual_prize_list;
    }

    public ArrayList<Double> viewPlayerTotals()throws ClassCastException, SQLException {
        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/stock;instance=SQLEXPRESS;user=sa;password=password"); // need database

        Statement stm = conn.createStatement();

        String player_total_prizes = "select * from player prizes"; // get data from database

        ResultSet rst = stm.executeQuery(player_total_prizes);

        ArrayList<Double> total_prize_list = new ArrayList<>();

        while(rst.next()){
            Double prize = rst.getDouble("prize");
            total_prize_list.add(prize);
        }

        return total_prize_list;
    }

}
