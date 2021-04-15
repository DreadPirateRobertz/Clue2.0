package clueGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.applet.Applet;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GameControlPanel extends JPanel {
    private static JTextField guessPersonField, guessRoomField, guessWeaponField;
    public static JTextField guessResultField;
    private JTextField whoseTurnField;
    private int index = 0;
    private ArrayList<Player> players = Board.getPlayers();
    private static int roll = 0;
    private Board board = Board.getInstance();
    private ImageIcon dice = null;
    private JLabel dieRoll1 = new JLabel() ;
    private JLabel dieRoll2 = new JLabel();
    private boolean gameOverFLag = false;

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
        setLayout(new GridLayout(2, 0));
        board.setDie();
        roll = board.getDie();
        setWhoseTurn();
        updateDiceIcons();
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
        panel.setBackground(Color.BLACK);
        JLabel whoseTurn = new JLabel("Whose turn?");
        whoseTurn.setForeground(Color.GREEN);
        whoseTurn.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(2, 0));
        panel.add(whoseTurn);
        whoseTurnField.setEditable(false);
        panel.add(whoseTurnField);
        return panel;
    }

    private JPanel rollBoxPanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.add(dieRoll1);
        panel.add(dieRoll2);
        return panel;
    }

    public void updateDiceIcons(){

        switch (board.getDie()){

            case 2 -> {
                dice = diceIcons[0];
                dieRoll1.setIcon(dice);
                dice = diceIcons[0];
                dieRoll2.setIcon(dice);
            }
            case 3 -> {
                dice = diceIcons[0];
                dieRoll1.setIcon(dice);
                dice = diceIcons[1];
                dieRoll2.setIcon(dice);
            }
            case 4 -> {
                dice = diceIcons[1];
                dieRoll1.setIcon(dice);
                dice = diceIcons[1];
                dieRoll2.setIcon(dice);
            }
            case 5 -> {
                dice = diceIcons[2];
                dieRoll2.setIcon(dice);
                dice = diceIcons[1];
                dieRoll2.setIcon(dice);
            }
            case 6 -> {
                dice = diceIcons[2];
                dieRoll1.setIcon(dice);
                dice = diceIcons[2];
                dieRoll2.setIcon(dice);
            }
            case 7 -> {
                dice = diceIcons[3];
                dieRoll1.setIcon(dice);
                dice = diceIcons[2];
                dieRoll2.setIcon(dice);
            }
            case 8 -> {
                dice = diceIcons[3];
                dieRoll1.setIcon(dice);
                dice = diceIcons[3];
                dieRoll2.setIcon(dice);
            }
            case 9 -> {
                dice = diceIcons[4];
                dieRoll1.setIcon(dice);
                dice = diceIcons[3];
                dieRoll2.setIcon(dice);
            }
            case 10 -> {
                dice = diceIcons[4];
                dieRoll1.setIcon(dice);
                dice = diceIcons[4];
                dieRoll2.setIcon(dice);
            }
            case 11 -> {
                dice = diceIcons[5];
                dieRoll1.setIcon(dice);
                dice = diceIcons[4];
                dieRoll2.setIcon(dice);
            }
            case 12 -> {
                dice = diceIcons[5];
                dieRoll1.setIcon(dice);
                dice = diceIcons[5];
                dieRoll2.setIcon(dice);
            }
        }
    }
    public class SoundEffect {

        Clip clip;

        public void setFile(String soundFileName){

            try{
                File file = new File(soundFileName);
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        public void play(){
            clip.start();

        }
    }


    private JButton accuseButton(){
        JButton button = new JButton("J'Accuse");
        Player playa = board.getWhoseTurn();
        button.addActionListener(e -> {
            if(board.isPlayerFlag()){
                Object[] option = {"I'll never do this again..."};
                JLabel message = new JLabel("It's not your turn!");
                message.setHorizontalAlignment(JLabel.LEFT);
                JOptionPane.showOptionDialog(null, message, "Hold Your Horses",JOptionPane.OK_OPTION,
                        JOptionPane.ERROR_MESSAGE, null, option, option[0]);
            }
            else if(!board.isPlayerFlag()){
                if(!board.getCell(playa).isRoomCenter()){
                    Object[] option = {"I'll never do this again..."};
                    JLabel message = new JLabel("You Must be in a Room");
                    message.setHorizontalAlignment(JLabel.LEFT);
                    JOptionPane.showOptionDialog(null, message, "Hold Your Horses",JOptionPane.OK_OPTION,
                            JOptionPane.ERROR_MESSAGE, null, option, option[0]);
                }
                else{
                    HumanAccusationDialog humanAccusationDialog = new HumanAccusationDialog(board.getRoom(board.getCell(playa)), board.getAllCards());
                    Suggestion humanAccusation = humanAccusationDialog.getHumanAccusation();

                    Object[] optionWinner = {"W I N N E R!!!"};
                    Object[] optionLoser = {"I Accept My Fate"};


                    if (playa.doAccusation(humanAccusation)) {
                        int rv = JOptionPane.showOptionDialog(null, playa.getName() + " has WON\n   You've Beaten all the Odds!",
                                "Y O U    W I N", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionWinner, optionWinner[0]);
                        guessResultField.setBackground(playa.getColor());
                        setGuessResult(" W I N N E R!!!");
                        setPersonGuessField(board.getTheAnswer_Person());
                        setRoomGuessField(board.getTheAnswer_Room());
                        setWeaponGuessField(board.getTheAnswer_Weapon());
                        board.repaint();
                        gameOverFLag = true;
                        updateDisplay();
                    } else {
//                       SoundEffect soundEffect = new SoundEffect();
//                       soundEffect.setFile("data/trumpet.wav");
//                       soundEffect.play();
                        JOptionPane.showOptionDialog(null, playa.getName() + "\nYour Poor Choices Lead to Failure",
                                "L O S E R", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionLoser, optionLoser[0]);

                        guessResultField.setBackground(playa.getColor());
                        setPersonGuessField(board.getTheAnswer_Person());
                        setRoomGuessField(board.getTheAnswer_Room());
                        setWeaponGuessField(board.getTheAnswer_Weapon());
                        setGuessResult("L O S E R!!!");
                        board.repaint();
                        gameOverFLag = true;
                        updateDisplay();
                    }
                }
            }
        });
        button.setForeground(Color.GREEN);
        button.setBackground(Color.BLACK);
        return button;
    }

    private JButton nextButton(){
        JButton button = new JButton("NEXT");
        button.addActionListener(e -> {
            setGuessResult("");
            guessPersonField.setText("");
            guessRoomField.setText("");
            guessWeaponField.setText("");

            int row, col;
            Player playa = null;
            Object[] option = {"I'll never do this again..."};
            Object[] optionWinner = {"G A M E   O V E R"};
            Object[] optionLoser = {"Another One Down"};
            if(!board.isPlayerFlag()){
                JOptionPane.showOptionDialog(null, "You Haven't Completed Your Turn", "Hold Your Horses",JOptionPane.OK_OPTION,
                        JOptionPane.ERROR_MESSAGE, null, option, option[0]);
            }
            else{
                ArrayList<BoardCell> targets = new ArrayList<>(board.getTargets());
                playa = board.getWhoseTurn();
                setWhoseTurn();
                board.setDie();
                updateDiceIcons(); //Setting the dice ICONS
                roll = board.getDie();
                playa = board.getWhoseTurn();
                row = board.getWhoseTurn().getRow();
                col = board.getWhoseTurn().getCol();

                board.calcTargets(board.getCell(row, col), getRoll());
                targets = new ArrayList<>(board.getTargets());
                if (playa.isStayInRoomFlag()){
                    targets.add(board.getCell(playa));
                }
                if(playa instanceof Human) {
                    board.setPlayerFlag(false);
                    for (BoardCell target : targets){
                        target.setTarget(true);
                    }
                    board.repaint();
                }
                else{//If Player is Computer do all this

                    ArrayList allCards = board.getAllCards();
                    if(playa.isAccusationFlag()) {
                        board.calcTargets(board.getCell(playa), board.getDie());
                        Set<BoardCell> targetsSet = board.getTargets();
                        for (BoardCell target : targetsSet) { //Logic to make sure computer has to be in a room before it can make an accuasation
                            if (target.isRoomCenter()) {
                                playa.setRow(target.getRow());
                                playa.setCol(target.getCol());
                            }
                        }
                        if (playa.isAccusationFlag() && board.getCell(playa).isRoomCenter()) {
                            JLabel playaName = new JLabel(playa.getName());
                            playaName.setHorizontalAlignment(JLabel.CENTER);
                            if (playa.doAccusation(playa.getSuggestion())) {
                                int rv = JOptionPane.showOptionDialog(null, playa.getName() + " has WON\n   You've Lost to a Machine",
                                        "G A M E   O V E R", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionWinner, optionWinner[0]);
                                setRoomGuessField(board.getTheAnswer_Room());
                                setWeaponGuessField(board.getTheAnswer_Weapon());
                                setPersonGuessField(board.getTheAnswer_Person());
                                setGuessResult("C O M P U T E R    W I N N E R!!!");

                                gameOverFLag = true;
                                updateDisplay();
                                return;
                            } else {
                                JOptionPane.showOptionDialog(null, playaName + "\nYour Poor Choices Lead to Failure",
                                        "L O S E R", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionLoser, optionLoser[0]);

                                guessResultField.setBackground(playa.getColor());
                                Suggestion s = playa.getSuggestion();
                                setPersonGuessField(s.getPersonCard());
                                setRoomGuessField(s.getRoomCard());
                                setWeaponGuessField(s.getWeaponCard());
                                setGuessResult("L O S E R!!!");
                                board.getCell(playa).setOccupied(false);
                                players.remove(playa);
                                if (index != 0) {
                                    index--;
                                    board.setIndex(index);
                                } else {
                                    index++;
                                    board.setIndex(index);
                                }
                                board.repaint();
                                updateDisplay();
                                return;
                            }
                        }
                    }
                    BoardCell target = playa.selectTargets(targets);
                    if (target == null){

                    }
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

                        setPersonGuessField(s.getPersonCard());
                        setRoomGuessField(s.getRoomCard());
                        setWeaponGuessField(s.getWeaponCard());
                        Card disproveCard = board.handleSuggestion(playa, s);
                        if(disproveCard != null){
                            guessResultField.setBackground(board.getDisproverColor());
                            setGuessResult("This Guess Has Been Disproven by " + board.getDisproverPlayer());
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
        button.setForeground(Color.GREEN);
        button.setBackground(Color.BLACK);
        return button;
    }

    public void updateDisplay(){
        updateDiceIcons();
        removeAll();
        rollBoxPanel().removeAll();
        whoseTurnPanel().removeAll();
        guessResultPanel().removeAll();
        guessPanel().removeAll();
        revalidate();
        createLayout();

        whoseTurnField.getText();
        guessPersonField.getText();
        guessRoomField.getText();
        guessWeaponField.getText();
        guessResultField.getText();



        if (gameOverFLag){ //The intentionality for running a separate thread was so when there was a winner the GameControlPanel
            class GameOver extends Thread{ //Would still update and show the winner and the winning cards before it exits
                public void run(){//Instead of slamming all the information into a dialog box. I thought this solution was more elegant.
                    try {
                        TimeUnit.MILLISECONDS.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
            GameOver thread = new GameOver();
            thread.start();
        }
    }

    private JPanel guessPanel(){
        JPanel panel = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Guess");
        titledBorder.setTitleColor(Color.GREEN);
        panel.setBackground(Color.BLACK);
        panel.setBorder(titledBorder);
        panel.setLayout(new GridLayout(0,3));
//        if (gameOverFLag){
            guessPersonField.setBackground(Color.BLACK);
            guessPersonField.setForeground(Color.RED);
            guessRoomField.setBackground(Color.BLACK);
            guessRoomField.setForeground(Color.RED);
            guessWeaponField.setBackground(Color.BLACK);
            guessWeaponField.setForeground(Color.RED);
            guessResultField.setBackground(board.getWhoseTurn().getColor());
//        }
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
        panel.setBackground(Color.BLACK);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Guess Result");
        titledBorder.setTitleColor(Color.GREEN);
        panel.setBorder(titledBorder);
        panel.setLayout(new GridLayout(1,0));
        guessResultField.setEditable(false);
        guessResultField.setForeground(Color.BLACK);
        guessResultField.setHorizontalAlignment(JLabel.CENTER);
        guessResultField.setFont(new Font("Arial Bold", Font.BOLD, 12));
        panel.add(guessResultField);
        return panel;
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
    public static void setPersonGuessField(Card card){ guessPersonField.setText(card.getCardName()); }
    public static void setRoomGuessField(Card card){ guessRoomField.setText(card.getCardName()); }
    public static void setWeaponGuessField(Card card){ guessWeaponField.setText(card.getCardName()); }
    public static void setGuessResult(String guessResult){ guessResultField.setText(guessResult); }


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

