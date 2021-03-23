package clueGame;

import java.awt.*;

public class Human extends Player {


    public Human(String name, Color color, String startLocation) {
        super(name, color, startLocation);
    }

    @Override
    public Suggestion createSuggestion() {
        return null;
    }

    @Override
    public BoardCell selectTargets() {
        return null;
    }


}
