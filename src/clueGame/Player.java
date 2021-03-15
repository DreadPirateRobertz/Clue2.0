package clueGame;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Player {
    public String getName() {
        return name;
    }

    private String name;

    public Color getColor() {
        return color;
    }

    private Color color;

    public String getStartLocation() {
        return startLocation;
    }

    private String startLocation;
    private ArrayList<Card> myCards;
    protected int row, col;

    public Player(String name, Color color, String startLocation) {
        this.name = name;
        this.color = color;
        this.startLocation = startLocation;
    }

    public void setMyCards(ArrayList<Card> myCards) {
        this.myCards = myCards;
    }
    private void setRowCol(int row, int col){

    }
    public void updateHand(Card card){
        //STUB
    }

}
