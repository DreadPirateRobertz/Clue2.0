package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ClueGame extends JFrame {
    private static final int PLAYERS = 6;
    private static int playerCount = 0;
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(operation);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    public ClueGame() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1500, 1000);
        setTitle("ClueGame");
    }

    public static void main(String[] args) {
        ClueGame clueGame = new ClueGame();
        Board board = Board.getInstance();
        ArrayList<Card> inHand = null;
        ArrayList<Card> seen = null;

        Object[] theresOnlyOneAnswer = {"Hell Yeah!"};
        JOptionPane.showOptionDialog(clueGame, "                 You are Prisoner Shifty Eyes.\n Can you find the solution before the Computer players?",
                "Welcome to Clue", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, theresOnlyOneAnswer, theresOnlyOneAnswer[0]);


        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        ArrayList<Player> players = board.getPlayers();
        Player human = players.get(0);
        GameControlPanel gameControlPanel = new GameControlPanel();
        GameCardsPanel gameCardsPanel = new GameCardsPanel(human.getPlayerHand(), human.getSeenList());

        board.calcTargets(board.getCell(human), gameControlPanel.getRoll()); //Calculating Targets for the Human Player
        Set<BoardCell> targets = board.getTargets();

        for (BoardCell target : targets){//Sets the initial targets for the Human player
            target.setTarget(true);
        }
        clueGame.add(gameControlPanel, BorderLayout.SOUTH);//Add the panels to the frame
        clueGame.add(gameCardsPanel, BorderLayout.EAST);
        clueGame.add(board, BorderLayout.CENTER);
        clueGame.setVisible(true);
    }
}
