package clueGame;

import java.awt.*;
import java.util.ArrayList;

public class Human extends Player {

    public Human(String name, Color color, String startLocation) {
        super(name, color, startLocation);
    }

    @Override
    public Suggestion createSuggestion(Room room, ArrayList<Card> allCards) {
        return null;
    }

    @Override
    public BoardCell selectTargets(ArrayList<BoardCell> targets) {
        return null;
    }

    @Override
    public void doAccusation() {

    }




}
