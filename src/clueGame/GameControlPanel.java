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
        dieNumber = new JTextField(3);
        setLayout(new GridLayout(2, 0));
        createLayout();
    }

    private void createLayout(){   //This will add the two inner classes (which consists of nested panels) that were created below to the GameControlPanel
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(0, 4));
        upperPanel.add(whoseTurnPanel());
        upperPanel.add(rollBoxPanel());
        upperPanel.add(accuseButton());
        upperPanel.add(nextButton());
        add(upperPanel);
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new GridLayout(0,2));
        lowerPanel.add(guessPanel());
        lowerPanel.add(guessResultPanel());
        add(lowerPanel);
    }

    private JPanel whoseTurnPanel(){
        JPanel panel = new JPanel();
        JLabel whoseTurn = new JLabel("Whose turn?");
        whoseTurn.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(2, 0));
        panel.add(whoseTurn);
        whoseTurnField.setEditable(false);
        panel.add(whoseTurnField);
        whoseTurnField.getText();
        return panel;
    }

    private JPanel rollBoxPanel(){
        JPanel panel = new JPanel();
        JLabel roll = new JLabel("Roll:");
        panel.add(roll);
        dieNumber.setEditable(false);
        panel.add(dieNumber);
        return panel;
    }

    private JButton accuseButton(){
        JButton button = new JButton("J'Accuse");
        return button;
    }

    private JButton nextButton(){
        JButton button = new JButton("NEXT");
        return button;
    }

    private JPanel guessPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
        panel.setLayout(new GridLayout(1,0));
        guessField.setEditable(false);
        panel.add(guessField);
        guessField.getText();
        return panel;
    }

    private JPanel guessResultPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
        panel.setLayout(new GridLayout(1,0));
        guessResultField.setEditable(false);
        panel.add(guessResultField);
        guessResultField.getText();
        return panel;
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
        frame.setSize(750, 180);  // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
        frame.setVisible(true); // make it visible

        // test filling in the data
        controlPanel.setWhoseTurn(new Computer("Larry", Color.green, "Galley"));
        controlPanel.setGuess( ":)");
        controlPanel.setGuessResult( ":):):)");
        controlPanel.setDie(3);
    }
}

