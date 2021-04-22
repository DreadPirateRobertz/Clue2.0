package clueGame;

import java.awt.*;
import java.util.ArrayList;

public class Human extends Player {

    public Human(String name, Color color, String startLocation) {
        super(name, color, startLocation);
    }

    @Override
    public Suggestion createSuggestion(Room room, ArrayList<Card> allCards) {
        Board board = Board.getInstance();
        HumanSuggestionDialog hsp = new HumanSuggestionDialog(room, allCards);
        if(board.isPlayerFlag()) {
            Board.getInstance().getCell(this).setTarget(false);
        }
        Suggestion s = hsp.getHumanSuggestion();
        Player suggestedPlaya = board.getPlayer(s.getPersonCard().getCardName());
        board.getCell(suggestedPlaya).setOccupied(false);
        suggestedPlaya.setRow(this.getRow());
        suggestedPlaya.setCol(this.getCol());
        board.setPlayerFlag(true);
        return s;
    }

    @Override
    public BoardCell selectTargets(ArrayList<BoardCell> targets) {
        return null;
    }

}
