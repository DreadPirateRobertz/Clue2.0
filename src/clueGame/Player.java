package clueGame;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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


    public Card disproveSuggestion(Suggestion s){
        ArrayList<Card> temp = new ArrayList<>();
        for(var card : cards){
            if(card.equals(s.getPerson())) {
                temp.add(card);
                continue;
            }
           if(card.equals(s.getRoom())) {
                temp.add(card);
                continue;
            }
           if(card.equals(s.getWeapon())) {
                temp.add(card);
            }
        }
        if(temp.size() == 1){
            return temp.get(0);
        }
        else if (temp.size() > 1){
            Random randy = new Random();
            Collections.shuffle(temp);
            return temp.get(randy.nextInt(temp.size()));
        }
        return null;
    }

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
