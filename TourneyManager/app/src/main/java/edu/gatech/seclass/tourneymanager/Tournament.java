package edu.gatech.seclass.tourneymanager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jameszhang on 3/1/17.
 */

public class Tournament {

    private int tournamentID;
    private int house_cut;
    private int entry_price;
    private Player[] players;
    private int mode_selected;
    private Match[] matches;
    private int matches_remaining;
    private Player first_place;
    private Player second_place;
    private Player third_place;
    private int house_profit;

    public ArrayList<Double> displayPrizes()throws ClassCastException, SQLException{
        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/stock;instance=SQLEXPRESS;user=sa;password=password"); // Here, needs sring from Austin...

        Statement stm = conn.createStatement();

        String prizes = "select * from prizes"; // Here, needs a true table

        ResultSet rst = stm.executeQuery(prizes);

        ArrayList<Double> prize_list = new ArrayList<>();

        while(rst.next()){
            Double prize = rst.getDouble("prize");
            prize_list.add(prize);
        }

        return prize_list;
    }

    public ArrayList<Player> displayPlayers() throws ClassCastException, SQLException{
        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/stock;instance=SQLEXPRESS;user=sa;password=password"); // Here, needs sring from Austin...

        Statement stm = conn.createStatement();

        String players = "select * from players"; // Here, needs a true table

        ResultSet rst = stm.executeQuery(players);

        ArrayList<Player> player_list = new ArrayList<>();

        while(rst.next()){
            Player player = new Player(rst.getString("name"),rst.getString("username"),rst.getString("phone"),rst.getString("deck"));
            player_list.add(player);
        }

        return player_list;
    }


    public ArrayList<Match> displayMatchList(int mode) throws ClassCastException, SQLException{ // Here, does the mode matter?
        Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://localhost:1433/stock;instance=SQLEXPRESS;user=sa;password=password"); // Here, needs sring from Austin...

        Statement stm = conn.createStatement();

        String matches = "select * from matches"; // Here, needs a true table

        ResultSet rst = stm.executeQuery(matches);

        ArrayList<Match> match_list = new ArrayList<>();

        while(rst.next()){
            //Match match = new Match(rst.getString("matchId"),rst.getString("player1"),rst.getString("player2"));
            //match.getResult();                      // How can I get result?
            //match_list.add(match);
        }
        return match_list;
    }



    public List<String[]> createMatches(Player[] players){

        int player_number = players.length;

        if(player_number % 2 !=0)
        {
            throw new IllegalArgumentException("The number of players for matches should be even!");
        }

        int matches_per_round = player_number/2;
        List<String[]> match_list = new ArrayList<String[]>();


        for(int i = 0; i < matches_per_round; i++)
        {
            String[] match = new String[2];
            match[0] = players[i].getUsername();
            match[1] = players[player_number-1-i].getUsername();
            match_list.add(match);

        }

        return match_list;

    }


    public int[] calculatePrizes(int entranceFee, int entrantsNumber, int housePercentage) {
        int result[] = {0, 0, 0, 0};

        int totalFee = entranceFee * entrantsNumber;
        int houseCut = (int) (totalFee * (housePercentage / 100.0));
        int totalPrize = totalFee - houseCut;
        int firstPrize = (int) (totalPrize * 0.5);
        int secondPrize = (int) (totalPrize * 0.3);
        int thirdPrize = (int) (totalPrize * 0.2);

        result[0] = houseCut;
        result[1] = firstPrize;
        result[2] = secondPrize;
        result[3] = thirdPrize;

        return result;

    }



}
