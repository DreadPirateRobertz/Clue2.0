package clueGame;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public abstract class Player {

    private String name;
    private Color color;
    protected String startLocation;
    protected ArrayList<Card> playerHand, seenList;
    protected int row, col;

    public Player(String name, Color color, String startLocation) {
        this.name = name;
        this.color = color;
        this.startLocation = startLocation;
        playerHand = new ArrayList<>();
        seenList = new ArrayList<>();
    }
    public abstract Suggestion createSuggestion(Room room, ArrayList<Card> allCards);
    public abstract BoardCell selectTargets(ArrayList<BoardCell> targets);

    public Card disproveSuggestion(Suggestion s){
        ArrayList<Card> disproveList = new ArrayList<>();
        for (Card card : playerHand){
            if (card.equals(s.getPersonCard())) {
                disproveList.add(card);
            }
           else if (card.equals(s.getRoomCard())) {
                disproveList.add(card);
            }
           else if (card.equals(s.getWeaponCard())) {
                disproveList.add(card);
            }
        }
        if (disproveList.size() == 1){
            return disproveList.get(0);
        }
        else if (disproveList.size() > 1){
            Random randomize = new Random();
            Collections.shuffle(disproveList);
            return disproveList.get(randomize.nextInt(disproveList.size())); //Random Card is selected from list
        }
        return null;
    }


    //Setters
    public void updateHand(Card card){ playerHand.add(card); }
    public void updateSeenList(Card card){ seenList.add(card); }
    public void setPlayerHand(ArrayList<Card> cards) {
        this.playerHand = cards;
    }
    public void setPlayer_RowCol() { //Sets row/col logic for players since I put startLocation in as a String to ClueSetup
        row = Board.getInstance().getRoom(startLocation.charAt(0)).getCenterCell().getRow();
        col = Board.getInstance().getRoom(startLocation.charAt(0)).getCenterCell().getCol();
    }
    //Getters
    public int getRow() { return row; }
    public int getCol() { return col; }
    public ArrayList<Card> getSeenList() { return seenList; }
    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }
    public String getName() {
        return name;
    }
    public Color getColor() {
        return color;
    }
    public String getStartLocation() {
        return startLocation;
    }

}
