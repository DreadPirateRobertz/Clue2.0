package clueGame;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Player {

    private String name;
    private Color color;
    private String startLocation;
    private ArrayList<Card> cards;
    protected int row, col; //TODO: Functionality

    public Player(String name, Color color, String startLocation) {
        this.name = name;
        this.color = color;
        this.startLocation = startLocation;
    }

    public abstract void updateHand(Card card);


    //Setters
    public void setMyCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    //Getters
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
