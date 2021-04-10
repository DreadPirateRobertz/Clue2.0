package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameControlPanel extends JPanel {
    private JTextField guessField;
    private JTextField guessResultField;
    private JTextField whoseTurnField;
    private JTextField dieNumber;
    private static int index = 0;
    private ArrayList<Player> players = Board.getPlayers();
    private static int roll = 0;
    private Board board = Board.getInstance();


    public GameControlPanel()  {
        guessField = new JTextField(20);
        guessResultField = new JTextField( 20);
        whoseTurnField = new JTextField(10);
        dieNumber = new JTextField(3);
        setLayout(new GridLayout(2, 0));
        roll = setDie();
        setWhoseTurn();
        updateDisplay();
        createLayout();
    }

    private void createLayout(){
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
        button.addActionListener(e -> {
            int row, col;
            Player playa = null;
            Object[] options = {"I'll never do this again..."};
            if(!board.isPlayerFlag()){
                JOptionPane.showOptionDialog(null, "You Haven't Taken Your Turn", "Hold Your Horses",JOptionPane.OK_OPTION,
                        JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            }
            else{
                ArrayList<BoardCell> targets = new ArrayList<>(board.getTargets());
                setWhoseTurn();
                roll = setDie();
                for (BoardCell target : targets){
                    target.setTarget(false);
                }
                row = board.getWhoseTurn().getRow();
                col = board.getWhoseTurn().getCol();
                playa = board.getWhoseTurn();
                board.calcTargets(board.getCell(row, col), getRoll());
                targets = new ArrayList<>(board.getTargets());
                if(playa.getClass().equals(Human.class)) {
                    board.setPlayerFlag(false);
                    for (BoardCell target : targets){
                        target.setTarget(true);
                    }
                    board.repaint();
                }
                else{
                    ArrayList allCards = board.getAllCards();
                    playa.doAccusation();
                    BoardCell target = playa.selectTargets(targets);
                    board.getCell(playa).setOccupied(false);
                    playa.setRow(target.getRow());
                    playa.setCol(target.getCol());
                    //TODO: DO SOMETHING WITH THIS SUGGESTION
                    if(board.getCell(playa).isRoomCenter()) {
                        Suggestion s = playa.createSuggestion(board.getRoom(board.getCell(playa)), allCards);
                    }
                    board.repaint();
                }
            }
            updateDisplay();
        });
        return button;
    }

    private JPanel guessPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
        panel.setLayout(new GridLayout(1,0));
        guessField.setEditable(false);
        panel.add(guessField);
        return panel;
    }

    private JPanel guessResultPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
        panel.setLayout(new GridLayout(1,0));
        guessResultField.setEditable(false);
        panel.add(guessResultField);
        return panel;
    }
    public void updateDisplay(){
        whoseTurnField.getText();
        guessField.getText();
        guessResultField.getText();
    }

    //Getters
    public int getRoll() { return roll; }
    //Setters
    public void setWhoseTurn(){
        Player playa = board.setWhoseTurn();
        whoseTurnField.setText(playa.getName());
        whoseTurnField.setBackground(playa.getColor());
        if(index == players.size()-1){
            index = 0; //Reset Index to First Player
        }
        else{
            index++;
        }
    }
    public void setGuess(String guess){ guessField.setText(guess); }
    public void setGuessResult(String guessResult){ guessResultField.setText(guessResult); }
    public int setDie(){
        Random randomize = new Random();
        int random = randomize.nextInt(6)+1;
        dieNumber.setText(String.valueOf(random));
        return random;
    }

    //Main\\
    public static void main(String[] args) {
        GameControlPanel controlPanel = new GameControlPanel();
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(controlPanel); // put the panel in the frame
        frame.setSize(750, 180);  // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close
        frame.setVisible(true); // make it visible

        // test filling in the data
//        controlPanel.setWhoseTurn(new Computer("Larry", Color.green, "Galley"));
        controlPanel.setGuess( ":)");
        controlPanel.setGuessResult( ":):):)");
    }
}

