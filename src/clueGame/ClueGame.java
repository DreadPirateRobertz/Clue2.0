package clueGame;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ClueGame extends JFrame {
    private static GameCardsPanel gcp = null;

    public ClueGame() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(dim.width, dim.height);
        setTitle("ClueGame");
    }

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
    public static GameCardsPanel getGameCardsPanel() {
        return gcp;
    }
    public static void updateCardsPanel() {
        gcp.updatePanels();
    }
    /////OFFICIAL MAIN FOR CLUEGAME\\\\\
    public static void main(String[] args) {
        ClueGame clueGame = new ClueGame();
        Board board = Board.getInstance();

        class SplashScreen extends Thread{
            public void run(){
                try {
                    TimeUnit.MILLISECONDS.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object[] theresOnlyOneAnswer = {"Hell Yeah!"};
                JOptionPane.showOptionDialog(clueGame, "                 You are Prisoner Shifty Eyes.\n Can you find the solution before the Computer players?",
                        "Welcome to Clue...in SPACE", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, theresOnlyOneAnswer, theresOnlyOneAnswer[0]);
                class SimpleAudioPlayer { //Not sure how to link my sound card/mixer with Ubuntu and IntelliJ but is playing fine on Windows
                    //Fix^
                    Clip clip;
                    AudioInputStream audioInputStream;
                    // constructor to initialize streams and clip
                    public SimpleAudioPlayer(String filePath) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
                        clip = AudioSystem.getClip();
                        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
                        clip.open(audioInputStream);
                    }
                    // Method to play the audio
                    public void play()
                    {
                        //start the clip
                        clip.start();
                    }
                }
                SimpleAudioPlayer sap = null;
                try {
                    sap = new SimpleAudioPlayer("data/r2d2.wav");
                } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                    e.printStackTrace();
                }
                assert sap != null;
                sap.play();
            }

        }
        SplashScreen splashy = new SplashScreen();
        splashy.start();

        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        board.initialize();
        ArrayList<Player> players = board.getPlayers();
        Player human = players.get(0);
        GameControlPanel gameControlPanel = new GameControlPanel();
        GameCardsPanel gameCardsPanel = new GameCardsPanel(human.getPlayerHand(), human.getSeenList());
        gcp = gameCardsPanel;
        board.calcTargets(board.getCell(human), gameControlPanel.getRoll()); //Calculating Targets for the Human Player
        Set<BoardCell> targets = board.getTargets();

        for (BoardCell target : targets) {//Sets the initial targets for the Human player
            target.setTarget(true);
        }
        clueGame.add(gameControlPanel, BorderLayout.SOUTH);//Add the panels to the frame
        clueGame.add(gameCardsPanel, BorderLayout.EAST);
        clueGame.add(board, BorderLayout.CENTER);
        clueGame.setVisible(true);
        }
}
