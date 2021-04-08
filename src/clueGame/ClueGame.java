package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class ClueGame extends JFrame {
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

        ArrayList<Card> inHand = null;
        ArrayList<Card> seen = null;

        Board.getInstance().setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        Board.getInstance().initialize();//This is a LinkedHashMap and should preserve insertion order of Players
        Map<Player, ArrayList<Card>> playerMap = Board.getInstance().getPlayerMap();

        for (Player player : playerMap.keySet()){//Find the Human
            if (player.getClass().equals(Human.class)){
                inHand = player.getPlayerHand();
                seen = player.getSeenList();
                break;
            }
        }
        GameControlPanel gameControlPanel = new GameControlPanel();
        GameCardsPanel gameCardsPanel = new GameCardsPanel(inHand, seen);
        /*Object[] options = {"Yes, please",
                "No way!"};
int n = JOptionPane.showOptionDialog(frame,
"Would you like green eggs and ham?",
"A Silly Question",
JOptionPane.YES_NO_OPTION,
JOptionPane.QUESTION_MESSAGE,
null,     //do not use a custom Icon
options,  //the titles of buttons
options[0]); //default button title/*

         */
//        Object[] options = {"Hell Yeah!"};
//        JOptionPane.showOptionDialog(clueGame, "You are Prisoner Shifty eyes.  Can you find the solution before the Computer players?",
//                "Welcome to Clue", JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        clueGame.add(gameControlPanel, BorderLayout.SOUTH);
        clueGame.add(gameCardsPanel, BorderLayout.EAST);
        clueGame.add(Board.getInstance(), BorderLayout.CENTER);
        clueGame.setVisible(true);
    }
}
