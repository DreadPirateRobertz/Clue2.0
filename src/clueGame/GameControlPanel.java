package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameControlPanel extends JPanel {
    private JTextField guessPersonField, guessRoomField, guessWeaponField;
    private JTextField guessResultField;
    private JTextField whoseTurnField;
    private JTextField dieNumber;
    private int index = 0;
    private ArrayList<Player> players = Board.getPlayers();
    private static int roll = 0;
    private Board board = Board.getInstance();
    private Suggestion suggestion;


    public GameControlPanel()  {
        guessPersonField = new JTextField(20);
        guessRoomField = new JTextField(20);
        guessWeaponField = new JTextField(20);
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
            setGuessResult("");
            guessPersonField.setText("");
            guessPersonField.setBackground(Color.WHITE);
            guessRoomField.setText("");
            guessRoomField.setBackground(Color.WHITE);
            guessWeaponField.setText("");
            guessPersonField.setBackground(Color.WHITE);
            guessResultField.setBackground(Color.WHITE);
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
                    if(playa.isAccusationFlag()) {
                        if(playa.doAccusation(playa.getSuggestion())){
                            JOptionPane.showMessageDialog(null, playa.getName() + " you've shown them all how it's done, YOU WON!");
                            guessResultField.setBackground(playa.getColor());
                            setGuessResult("W I N N E R!!!");
                            setPersonGuessField(board.getTheAnswer_Person());
                            setRoomGuessField(board.getTheAnswer_Room());
                            setWeaponGuessField(board.getTheAnswer_Weapon());
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "You have chosen rather poorly" + playa.getName() + " ,you have lost!");
                            guessResultField.setBackground(playa.getColor());
                            setGuessResult("L O S E R!!!");
                            players.remove(playa);
                            setWhoseTurn();
                            board.repaint();
                        }
                    }
                    BoardCell target = playa.selectTargets(targets);
                    board.getCell(playa).setOccupied(false);
                    playa.setRow(target.getRow());
                    playa.setCol(target.getCol());
                    if(board.getCell(playa).isRoomCenter()) {
                        Suggestion s = playa.createSuggestion(board.getRoom(board.getCell(playa)), allCards);
                        playa.setSuggestion(s);
                        Player suggestedPlaya = board.getPlayer(s.getPersonCard().getCardName());
                        suggestedPlaya.setRow(playa.getRow());
                        suggestedPlaya.setCol(playa.getCol());
                        setPersonGuessField(s.getPersonCard());
                        setRoomGuessField(s.getRoomCard());
                        setWeaponGuessField(s.getWeaponCard());
                        Card disproveCard = board.handleSuggestion(playa, s);
                        if(disproveCard != null){
                            guessResultField.setBackground(board.getAccuserColor());
                            setGuessResult("This Guess Has Been Disproven by " + board.getAccuserPlayer());
                        }
                        else{
                            setBackground(Color.BLACK);
                            setForeground(Color.RED);
                            setGuessResult("U N A B L E  T O  D I S P R O V E...?" + board.getAccuserPlayer());
                        }
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
        panel.setLayout(new GridLayout(0,3));
        guessPersonField.setEditable(false);
        guessPersonField.setHorizontalAlignment(JLabel.CENTER);
        guessPersonField.setFont(new Font("Arial Bold", Font.BOLD, 12));
        guessRoomField.setEditable(false);
        guessRoomField.setHorizontalAlignment(JLabel.CENTER);
        guessRoomField.setFont(new Font("Arial Bold", Font.BOLD, 12));
        guessWeaponField.setEditable(false);
        guessWeaponField.setHorizontalAlignment(JLabel.CENTER);
        guessWeaponField.setFont(new Font("Arial Bold", Font.BOLD, 12));

        panel.add(guessPersonField);
        panel.add(guessRoomField);
        panel.add(guessWeaponField);
        return panel;
    }

    private JPanel guessResultPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
        panel.setLayout(new GridLayout(1,0));
        guessResultField.setEditable(false);
        guessResultField.setHorizontalAlignment(JLabel.CENTER);
        guessResultField.setFont(new Font("Arial Bold", Font.BOLD, 12));
        panel.add(guessResultField);
        return panel;
    }
    public void updateDisplay(){
        repaint();
        whoseTurnField.getText();
        guessPersonField.getText();
        guessRoomField.getText();
        guessWeaponField.getText();
        guessResultField.getText();
    }

    //Getters
    public int getRoll() { return roll; }
    //Setters
    public void setWhoseTurn(){
        Player playa = board.setWhoseTurn();
        whoseTurnField.setText(playa.getName());
        whoseTurnField.setHorizontalAlignment(JLabel.CENTER);
        whoseTurnField.setFont(new Font("Arial Bold", Font.BOLD, 12));
        whoseTurnField.setBackground(playa.getColor());
        if(index == players.size()-1){
            index = 0; //Reset Index to First Player
        }
        else{
            index++;
        }
    }
    public void setPersonGuessField(Card card){ guessPersonField.setText(card.getCardName());
    guessPersonField.setBackground(card.getColor());}
    public void setRoomGuessField(Card card){ guessRoomField.setText(card.getCardName());
        guessRoomField.setBackground(card.getColor());}

    public void setWeaponGuessField(Card card){ guessWeaponField.setText(card.getCardName());
        guessWeaponField.setBackground(card.getColor());}

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
//        controlPanel.setGuess( ":)");
//        controlPanel.setGuessResult( ":):):)");
    }
}

