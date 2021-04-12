package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

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
    private ImageIcon dice = null;
    private JLabel dieRoll = null;
    ImageIcon[] diceIcons = {
            new ImageIcon(new ImageIcon("data/dice1.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)),
            new ImageIcon(new ImageIcon("data/dice2.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)),
            new ImageIcon(new ImageIcon("data/dice3.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)),
            new ImageIcon(new ImageIcon("data/dice4.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)),
            new ImageIcon(new ImageIcon("data/dice5.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)),
            new ImageIcon(new ImageIcon("data/dice6.png").getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT))
    };


    public GameControlPanel()  {
        guessPersonField = new JTextField(20);
        guessRoomField = new JTextField(20);
        guessWeaponField = new JTextField(20);
        guessResultField = new JTextField( 20);
        whoseTurnField = new JTextField(10);
        dieNumber = new JTextField(3);
        setLayout(new GridLayout(2, 0));
        board.setDie();
        roll = board.getDie();
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
        switch (board.getDie()){
            case 1 -> dice = diceIcons[0];
            case 2 -> dice = diceIcons[1];
            case 3 -> dice = diceIcons[2];
            case 4 -> dice = diceIcons[3];
            case 5 -> dice = diceIcons[4];
            case 6 -> dice = diceIcons[5];
        }
        dieRoll = new JLabel(dice);
        panel.add(dieRoll);
        return panel;
    }
public void updateDice(){
    switch (board.getDie()){
        case 1 -> dice = diceIcons[0];
        case 2 -> dice = diceIcons[1];
        case 3 -> dice = diceIcons[2];
        case 4 -> dice = diceIcons[3];
        case 5 -> dice = diceIcons[4];
        case 6 -> dice = diceIcons[5];
    }
    dieRoll.setIcon(dice);
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
            guessResultField.setForeground(Color.BLACK);
            int row, col;
            Player playa = null;
            Object[] option = {"I'll never do this again..."};
            Object[] optionWinner = {"G A M E   O V E R"};
            Object[] optionLoser = {"Another One Down"};
            if(!board.isPlayerFlag()){
                JOptionPane.showOptionDialog(null, "You Haven't Taken Your Turn", "Hold Your Horses",JOptionPane.OK_OPTION,
                        JOptionPane.ERROR_MESSAGE, null, option, option[0]);
            }
            else{

                ArrayList<BoardCell> targets = new ArrayList<>(board.getTargets());
                playa = board.getWhoseTurn();
                setWhoseTurn();
                board.setDie();
                updateDice();
                roll = board.getDie();
                playa = board.getWhoseTurn();
                row = board.getWhoseTurn().getRow();
                col = board.getWhoseTurn().getCol();

                board.calcTargets(board.getCell(row, col), getRoll());
                targets = new ArrayList<>(board.getTargets());
                if (playa.isStayInRoomFlag()){
                    targets.add(board.getCell(playa));
                }
                if(playa.getClass().equals(Human.class)) {
                    board.setPlayerFlag(false);
                    for (BoardCell target : targets){
                        target.setTarget(true);
                    }
                    board.repaint();
                }
                else{//If Player is Computer do all this

                    ArrayList allCards = board.getAllCards();
                    if(playa.isAccusationFlag()) {
                        if(playa.doAccusation(playa.getSuggestion())){
                            JOptionPane.showOptionDialog(null,  playa.getName() + " has WON\n   You've Lost to a Machine",
                                    "G A M E   O V E R", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionWinner, optionWinner[0]);
                            ;
                            guessResultField.setBackground(playa.getColor());
                            setGuessResult("W I N N E R!!!");
                            setPersonGuessField(board.getTheAnswer_Person());
                            setRoomGuessField(board.getTheAnswer_Room());
                            setWeaponGuessField(board.getTheAnswer_Weapon());
                            System.out.println(board.getTheAnswer_Person().getCardName());//Testing Statements
                            System.out.println(board.getTheAnswer_Room().getCardName());
                            System.out.println(board.getTheAnswer_Weapon().getCardName());
                            updateDisplay();
                            return;
                        }
                        else{
                            JOptionPane.showOptionDialog(null, "         " + playa.getName() + "\nYour Poor Choices Lead to Failure",
                                    "L O S E R", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionLoser, optionLoser[0]);

                            guessResultField.setBackground(playa.getColor());
                            Suggestion s = playa.getSuggestion();
                            setPersonGuessField(s.getPersonCard());
                            setRoomGuessField(s.getRoomCard());
                            setWeaponGuessField(s.getWeaponCard());
                            setGuessResult("L O S E R!!!");
                            board.getCell(playa).setOccupied(false);
                            players.remove(playa);
                            if(index != 0) {
                                index--;
                                board.setIndex(index);
                            }
                            else{
                                index++;
                                board.setIndex(index);
                            }
                            board.repaint();
                            updateDisplay();
                            return;
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
                        int lastPlayerRow = suggestedPlaya.getRow();
                        int lastPlayerCol = suggestedPlaya.getCol();
                        if (suggestedPlaya != null) {//If a player is removed as a LOSER then you don't want to do this
                            board.getCell(suggestedPlaya).setOccupied(false); //Critical had this missing for a while and couldn't figure out he BUG :)
                            suggestedPlaya.setRow(playa.getRow());
                            suggestedPlaya.setCol(playa.getCol());//Calls the suggestedPlaya to the Room
                        }
                        if(!(lastPlayerRow == suggestedPlaya.getRow()) && !(lastPlayerCol == suggestedPlaya.getCol())){
                            suggestedPlaya.setStayInRoomFlag(true);
                        }

                        System.out.println(suggestedPlaya.getName());//Testing who's called
                        setPersonGuessField(s.getPersonCard());
                        setRoomGuessField(s.getRoomCard());
                        setWeaponGuessField(s.getWeaponCard());
                        Card disproveCard = board.handleSuggestion(playa, s);
                        if(disproveCard != null){
                            guessResultField.setBackground(board.getAccuserColor());
                            setGuessResult("This Guess Has Been Disproven by " + board.getAccuserPlayer());
                        }
                        else{
                            guessResultField.setBackground(Color.BLACK);
                            guessResultField.setForeground(Color.RED);
                            setGuessResult("U N A B L E  T O  D I S P R O V E...?");

                        }
                    }
                    board.repaint();
                }
            }
            updateDisplay();
            if(playa != null) {
                playa.setStayInRoomFlag(false);
            }
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
        dieNumber.setText(String.valueOf(board.getDie()));
        whoseTurnField.getText();
        guessPersonField.getText();
        guessRoomField.getText();
        guessWeaponField.getText();
        guessResultField.getText();
        repaint();
    }

    //Getters
    public int getRoll() { return roll; }
    //Setters
    public void setWhoseTurn(){
        Player playa = board.setWhoseTurn();
        whoseTurnField.setText(playa.getName().toUpperCase(Locale.ROOT));
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

