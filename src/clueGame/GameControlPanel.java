package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GameControlPanel extends JPanel {
    JTextField guessField;
    JTextField guessResultField;
    JTextField whoseTurnField;
    JTextField dieNumber;

    public GameControlPanel()  {
        guessField = new JTextField(20);
        guessResultField = new JTextField( 20);
        whoseTurnField = new JTextField(10);
        dieNumber = new JTextField(5);
        setLayout(new BorderLayout());
        createLayout();
    }

    private void createLayout(){   //This will add the two inner classes (which consists of nested panels) that were created below to the GameControlPanel
        playerInfo playerInfo = new playerInfo();
        add(playerInfo, BorderLayout.NORTH);
        guessInfo guessInfo = new guessInfo();
        add(guessInfo, BorderLayout.SOUTH);

    }
/* Creating two inner classes to hold all the guess & player information */
    private class guessInfo extends JPanel {
        guessInfo() {
            JPanel guessBox = new JPanel();
            guessBox.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
            guessBox.add(guessField);
            guessField.getText();
            setLayout(new BorderLayout());
            add(guessBox, BorderLayout.WEST);
            JPanel guessResultBox = new JPanel();
            guessResultBox.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
            guessResultBox.add(guessResultField);
            guessResultField.getText();
            add(guessResultBox, BorderLayout.CENTER);
        }
    }
    private class playerInfo extends JPanel { //Panels inside panels
        playerInfo() {      //For the life of me I could not figure out how he centered "Whose Turn?" Still trying figure out GridLayout...
            JPanel whoseTurnBox = new JPanel();
            JLabel whoseTurn = new JLabel("Whose turn?");
            whoseTurn.setHorizontalAlignment(JLabel.CENTER);

            whoseTurnBox.setLayout(new BorderLayout());
            whoseTurnBox.add(whoseTurn, BorderLayout.NORTH);
            whoseTurnBox.add(whoseTurnField, BorderLayout.SOUTH);
            whoseTurnField.getText();
            setLayout(new GridLayout(1, 4));
            add(whoseTurnBox);
            JPanel rollBox = new JPanel();
            JLabel roll = new JLabel("Roll:");
            rollBox.add(roll);
            rollBox.add(dieNumber);
            add(rollBox, new GridLayout(1, 2));
            JButton accuseButton = new JButton("J'Accuse");
            JButton nextButton = new JButton("NEXT");
            add(accuseButton, new GridLayout(1,3));
            add(nextButton, new GridLayout(1,4));
        }
    }
    //Setters for updating all the fields
    public void setWhoseTurn(Player playa){
        whoseTurnField.setText(playa.getName());
        whoseTurnField.setBackground(playa.getColor());
    }
    public void setGuess(String guess){ guessField.setText(guess); }
    public void setGuessResult(String guessResult){ guessResultField.setText(guessResult); }
    public void setDie(int roll){ dieNumber.setText(String.valueOf(roll)); }

    //Main\\
    public static void main(String[] args) {
        GameControlPanel controlPanel = new GameControlPanel();
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(controlPanel); // put the panel in the frame
        frame.setSize(750, 120);  // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
        frame.setVisible(true); // make it visible

        // test filling in the data
        controlPanel.setWhoseTurn(new Computer("Larry", Color.green, "Galley"));
        controlPanel.setGuess( ":)");
        controlPanel.setGuessResult( ":):):)");
        controlPanel.setDie(3);
    }
}

