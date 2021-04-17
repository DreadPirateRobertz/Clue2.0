package clueGame;

import javax.swing.*;
import javax.swing.text.Caret;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;


public class HumanSuggestionDialog extends JDialog {
    Board board = Board.getInstance();
    JTextField roomField;
    Suggestion humanSuggestion = new Suggestion();
    ArrayList<Card> playerCards = board.getPlayerCards();
    ArrayList<Card> weaponCards = board.getWeaponCards();
    JComboBox personComboBox;
    JComboBox  weaponComboBox;
    Room room = Board.getInstance().getRoom(Board.getInstance().getCell(Board.getInstance().getWhoseTurn()));
    ArrayList<Card> allCards = Board.getInstance().getAllCards();
    HumanSuggestionDialog(Room room, ArrayList<Card> allCards){
        setTitle("Make A Suggestion");
        createLayout();
    }

    private void createLayout(){
        setLayout(new GridLayout(4,2));
        JLabel roomLabel = new JLabel("Current Room:");
        JLabel personLabel = new JLabel("Person:");
        JLabel weaponLabel = new JLabel("Weapon:");
        roomField = new JTextField();
        roomField.setEditable(false);
        personComboBox = new JComboBox();
        weaponComboBox = new JComboBox();
        add(roomLabel);
        add(roomField);
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
        roomField.setText(room.getName().toUpperCase(Locale.ROOT));
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
            Card roomCard = new Card(CardType.ROOM, room.getName()); //Producing the roomCard
            humanSuggestion.setPersonCard(playerCards.get(personComboBox.getSelectedIndex()));
            humanSuggestion.setWeaponCard(weaponCards.get(weaponComboBox.getSelectedIndex()));
            humanSuggestion.setRoomCard(roomCard);
            board.setPlayerFlag(true);
        });
        return button;
    }

    public JButton cancelButton(){
        JButton button = new JButton("Cancel");
        button.addActionListener(e -> setVisible(false));
        board.getCell(board.getPlayer("Prisoner Shifty Eyes")).setTarget(true); //Hard coding change name later to search for Human
        return button;
    }

    public Suggestion getHumanSuggestion() {
        return humanSuggestion;
    }
}

