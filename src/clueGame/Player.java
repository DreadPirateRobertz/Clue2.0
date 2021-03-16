package clueGame;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Player {

    private String name;
    private Color color;
    private String startLocation;
    private ArrayList<Card> myCards;
//    protected int row, col;

    public Player(String name, Color color, String startLocation) {
        this.name = name;
        this.color = color;
        this.startLocation = startLocation;
    }

    public void updateHand(Card card){
        //STUB
    }


    //Setters
    public void setMyCards(ArrayList<Card> myCards) {
        this.myCards = myCards;
    }
    //Getters
    public ArrayList<Card> getMyCards() {
        return myCards;
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
