package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GameControlPanel extends JPanel {
    public GameControlPanel()  {
        setLayout(new BorderLayout());
        createLayout();
    }

    public static void main(String[] args) {
        GameControlPanel controlPanel = new GameControlPanel();
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(controlPanel); // put the panel in the frame
        frame.setSize(750, 120);  // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
        frame.setVisible(true); // make it visible
        //frame.add(controlPanel, BorderLayout.SOUTH);
        // test filling in the data
//        panel.setTurn(new ComputerPlayer( "Col. Mustard", 0, 0, "orange"), 5);
//        panel.setGuess( "I have no guess!");
//        panel.setGuessResult( "So you have nothing?");;
    }
    private void createLayout(){
        playerInformation playerInformation = new playerInformation();

        add(playerInformation, BorderLayout.NORTH);
        guessInformation guessInformation = new guessInformation();
        add(guessInformation, BorderLayout.SOUTH);

    }

    private class guessInformation extends JPanel {
        guessInformation() {
            JPanel guessBox = new JPanel();
            guessBox.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
            JTextField guessField = new JTextField("I have no guess", 20);

            guessBox.add(guessField);
            setLayout(new BorderLayout());
            add(guessBox, BorderLayout.WEST);


            JPanel guessResultBox = new JPanel();
            guessResultBox.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
            JTextField guessResultField = new JTextField("So you have nothing", 20);
            guessResultBox.add(guessResultField);
            add(guessResultBox);

        }
    }
    private class playerInformation extends JPanel {
        playerInformation() {
            JPanel whoseTurnBox = new JPanel();
            JLabel whoseTurn = new JLabel("Whose turn?");
            JTextField field = new JTextField(10);
            whoseTurnBox.setLayout(new BorderLayout());
            whoseTurnBox.add(whoseTurn, BorderLayout.NORTH);
            whoseTurnBox.add(field, BorderLayout.SOUTH);
            setLayout(new GridLayout());
            add(whoseTurnBox, new GridLayout(0,1));


            JPanel rollBox = new JPanel();
            JLabel roll = new JLabel("Roll:");
            JTextField dieNumber = new JTextField(5);
//            rollBox.setLayout(new BorderLayout());
            rollBox.add(roll);
            rollBox.add(dieNumber);
            add(rollBox, new GridLayout(0, 2));

            JButton accuseButton = new JButton("Make Accusation");
            JButton nextButton = new JButton("NEXT");
            add(accuseButton, new GridLayout(0,3));
            add(nextButton, new GridLayout(0,4));
        }
    }

}

