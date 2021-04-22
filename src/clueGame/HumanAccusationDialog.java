package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;


public class HumanAccusationDialog extends JDialog {
    Board board = Board.getInstance();
    JComboBox roomComboBox;
    Suggestion humanAccusation = new Suggestion();
    ArrayList<Card> playerCards = board.getPlayerCards();
    ArrayList<Card> weaponCards = board.getWeaponCards();
    ArrayList<Card> roomCards = board.getRoomCards();
    JComboBox personComboBox;
    JComboBox  weaponComboBox;
    Room room;
    ArrayList<Card> allCards;
    HumanAccusationDialog(Room room, ArrayList<Card> allCards){
        this.room = room;
        this.allCards = allCards;
        setTitle("Make An Accusation");
        createLayout();
    }

    private void createLayout(){
        setLayout(new GridLayout(4,2));
        JLabel roomLabel = new JLabel("Rooms:");
        JLabel personLabel = new JLabel("Person:");
        JLabel weaponLabel = new JLabel("Weapon:");
        roomComboBox = new JComboBox();
        personComboBox = new JComboBox();
        weaponComboBox = new JComboBox();
        add(roomLabel);
        add(roomComboBox);
        add(personLabel);
        add(personComboBox);
        add(weaponLabel);
        add(weaponComboBox);
        add(submitButton());
        add(cancelButton());
        updateLayout();
        pack();
        center(this);
        setModal(true);
        setVisible(true);
    }

    public static void center(JDialog dialog) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = dialog.getSize().width;
        int h = dialog.getSize().height;

        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        dialog.setLocation(x-60, y-80);
    }

    public void updateLayout(){ //If I go back and change both Player classes I could just pass the weapon and people cards
        for(Card card : roomCards) {
            roomComboBox.addItem(card.getCardName().toUpperCase(Locale.ROOT));
        }
        for(Card card : playerCards ) {
            personComboBox.addItem(card.getCardName().toUpperCase(Locale.ROOT));
        }
        for(Card card : weaponCards ){
            weaponComboBox.addItem(card.getCardName().toUpperCase(Locale.ROOT));
        }
    }

    public JButton submitButton(){
        JButton button = new JButton("Submit");
        button.addActionListener(e ->{
            setVisible(false);
            humanAccusation.setPersonCard(playerCards.get(personComboBox.getSelectedIndex()));
            humanAccusation.setWeaponCard(weaponCards.get(weaponComboBox.getSelectedIndex()));
            humanAccusation.setRoomCard(roomCards.get(roomComboBox.getSelectedIndex()));
            board.setPlayerFlag(true);
        });
        return button;
    }

    public JButton cancelButton(){
        JButton button = new JButton("Cancel");
        button.addActionListener(e -> setVisible(false));
        board.getCell(board.getPlayer(board.getWhoseTurn().getName())).setTarget(true);
        return button;
    }

    public Suggestion getHumanAccusation() {
        return humanAccusation;
    }
}

