package clueGame;

import javax.swing.*;
import java.awt.*;

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
        setVisible(true);
    }

    public static void main(String[] args) {
        ClueGame clueGame = new ClueGame();
    }
}
