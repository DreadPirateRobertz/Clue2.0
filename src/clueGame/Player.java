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
    protected ArrayList<Card> cards;
    protected int row, col;

    public Player(String name, Color color, String startLocation) {
        cards = new ArrayList<>();
        this.name = name;
        this.color = color;
        this.startLocation = startLocation;
    }
    public abstract Suggestion createSuggestion();
    public abstract BoardCell selectTargets();

    public Card disproveSuggestion(Suggestion s){
        ArrayList<Card> temp = new ArrayList<>();
        for(Card card : cards){
            if(card.equals(s.getPersonCard())) {
                temp.add(card);
            }
           else if(card.equals(s.getRoomCard())) {
                temp.add(card);
            }
           else if(card.equals(s.getWeaponCard())) {
                temp.add(card);
            }
        }
        if(temp.size() == 1){
            return temp.get(0);
        }
        else if (temp.size() > 1){
            Random randomize = new Random();
            Collections.shuffle(temp);
            return temp.get(randomize.nextInt(temp.size())); //Random Card is selected from list
        }
        return null;
    }

    public void updateHand(Card card){  //Update Hand with SEEN Cards :)
        cards.add(card);
    }

    //Setters
    public void setMyCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    //Getters
    public void setPlayer_RowCol() { //Sets row/col logic for players since I put startLocation in as a String to ClueSetup
        row = Board.getInstance().getRoom(startLocation.charAt(0)).getCenterCell().getRow();
        col = Board.getInstance().getRoom(startLocation.charAt(0)).getCenterCell().getCol();
    }
    public ArrayList<Card> getMyCards() {
        return cards;
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
