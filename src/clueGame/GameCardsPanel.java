package clueGame;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;

public class GameCardsPanel extends JPanel {
    ArrayList<Card> inHandPersonCards = new ArrayList<>();
    Set<Card> seenPersonCards = new HashSet<>();
    ArrayList<Card> inHandRoomCards = new ArrayList<>();
    Set<Card> seenRoomCards = new HashSet<>();
    ArrayList<Card> inHandWeaponCards = new ArrayList<>();
    Set<Card> seenWeaponCards = new HashSet<>();
    ArrayList<Card> inHand, seen;

    public GameCardsPanel(ArrayList<Card> inHand, ArrayList<Card> seen) {
        this.inHand = inHand;
        this.seen = seen;
        setBackground(Color.BLACK);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Known Cards");
        titledBorder.setTitleColor(Color.GREEN);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        setBorder(titledBorder);
        setLayout(new GridLayout(3,0));
        updatePanels();
    }

    private void sortCards(ArrayList<Card> inHand, ArrayList<Card> seen) {
        inHandPersonCards.clear();
        inHandRoomCards.clear();
        inHandWeaponCards.clear();

        for (Card card : inHand){
            if (card.getCardType().equals(CardType.PERSON)){
                inHandPersonCards.add(card);
            }
            else if (card.getCardType().equals(CardType.ROOM)){
                inHandRoomCards.add(card);
            }
            else{
                inHandWeaponCards.add(card);
            }
        }
        for (Card card : seen){
            if (card.getCardType().equals(CardType.PERSON)){
                seenPersonCards.add(card);
            }
            else if (card.getCardType().equals(CardType.ROOM)){
                seenRoomCards.add(card);
            }
            else{
                seenWeaponCards.add(card);
            }
        }
    }

    public void updatePanels(){
        removeAll();
        roomCardsPanel().removeAll();
        peopleCardsPanel().removeAll();
        weaponCardsPanel().removeAll();
        revalidate();
        createLayout();
    }

    private void createLayout() { //Adds all the panels to the main panel
        sortCards(inHand, seen);
        add(peopleCardsPanel());
        add(roomCardsPanel());
        add(weaponCardsPanel());
    }

    private JPanel peopleCardsPanel(){
        JPanel panel = new JPanel();
        int rows = setRows(inHandPersonCards, seenPersonCards);
        rows = emptyDecksCheck(rows, inHandPersonCards, seenPersonCards);
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(rows, 0));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("People");
        titledBorder.setTitleColor(Color.GREEN);
        panel.setBorder(titledBorder);
        JLabel inHandLabel = new JLabel("In Hand:");
        inHandLabel.setForeground(Color.GREEN);
        panel.add(inHandLabel);
        setFieldsInHand(panel, inHandPersonCards);
        setNoneHand(panel, inHandPersonCards);
        JLabel seenLabel = new JLabel("Seen:");
        seenLabel.setForeground(Color.GREEN);
        panel.add(seenLabel);
        setFieldsSeen(panel, seenPersonCards);
        setNoneSeen(panel, seenPersonCards);
        return panel;
    }

    private JPanel roomCardsPanel(){
        JPanel panel = new JPanel();
        int rows = setRows(inHandRoomCards, seenRoomCards);
        rows = emptyDecksCheck(rows, inHandRoomCards, seenRoomCards);
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(rows, 0));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Rooms");
        titledBorder.setTitleColor(Color.GREEN);
        panel.setBorder(titledBorder);
        JLabel inHandLabel = new JLabel("In Hand:");
        inHandLabel.setForeground(Color.GREEN);
        panel.add(inHandLabel);
        setFieldsInHand(panel, inHandRoomCards);
        setNoneHand(panel, inHandRoomCards);
        JLabel seenLabel = new JLabel("Seen:");
        seenLabel.setForeground(Color.GREEN);
        panel.add(seenLabel);
        setFieldsSeen(panel, seenRoomCards);
        setNoneSeen(panel, seenRoomCards);
        return panel;
    }

    private JPanel weaponCardsPanel(){
        JPanel panel = new JPanel();
        int rows = setRows(inHandWeaponCards, seenWeaponCards); //Will determine how many rows in GridLayout
        rows = emptyDecksCheck(rows, inHandWeaponCards, seenWeaponCards);
        panel.setBackground(Color.BLACK);
        panel.setLayout(new GridLayout(rows, 0));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Weapons");
        titledBorder.setTitleColor(Color.GREEN);
        panel.setBorder(titledBorder);
        JLabel inHandLabel = new JLabel("In Hand:");
        inHandLabel.setForeground(Color.GREEN);
        panel.add(inHandLabel);
        setFieldsInHand(panel, inHandWeaponCards);
        setNoneHand(panel, inHandWeaponCards);
        JLabel seenLabel = new JLabel("Seen:");
        seenLabel.setForeground(Color.GREEN);
        panel.add(seenLabel);
        setFieldsSeen(panel, seenWeaponCards);
        setNoneSeen(panel, seenWeaponCards);
        return panel;
    }

    //Setters
    private void setFieldsSeen(JPanel panel, Set<Card> seen) {
        for (Card card : seen) {
            JTextField field = new JTextField(card.getCardName().toUpperCase(Locale.ROOT));
            field.setBackground(card.getColor());//This will be set in handleSuggestion() method of Board
            field.setFont(new Font("Arial Bold", Font.BOLD, 12));
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setForeground(Color.WHITE);
            field.setEditable(false);
            panel.add(field);
        }
    }
    private void setFieldsInHand(JPanel panel, ArrayList<Card> inHand) {
        for (Card card : inHand) {
            JTextField field = new JTextField(card.getCardName().toUpperCase(Locale.ROOT));
            Map<Player, ArrayList<Card>> playerMap = Board.getInstance().getPlayerMap();
            for (Player player : playerMap.keySet()){
                if(player.getClass().equals(Human.class)){
                    field.setBackground(player.getColor());
                    break;//This way it's not hard coded in and can be dynamic if a new ClueSetup is implemented
                }
            }
            field.setForeground(Color.WHITE);
            field.setFont(new Font("Arial Bold", Font.BOLD, 12));
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setEditable(false);
            panel.add(field);
        }
    }

    private void setNoneSeen(JPanel panel, Set<Card> seen) {
        if (seen.size() == 0) {
            JTextField field = new JTextField("None".toUpperCase(Locale.ROOT));
            field.setForeground(Color.WHITE);
            field.setFont(new Font("Arial Bold", Font.BOLD, 12));
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setBackground(Color.BLACK);
            field.setEditable(false);
            panel.add(field);
        }
    }

    private void setNoneHand(JPanel panel, ArrayList<Card> seen) {
        if (seen.size() == 0) {
            JTextField field = new JTextField("None".toUpperCase(Locale.ROOT));
            field.setForeground(Color.WHITE);
            field.setFont(new Font("Arial Bold", Font.BOLD, 12));
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setBackground(Color.BLACK);
            field.setEditable(false);
            panel.add(field);
        }
    }
    private int emptyDecksCheck(int rows, ArrayList<Card> inHand, Set<Card> seen) {
        if (inHand.size() == 0) {
            rows++;
        }
        if (seen.size() == 0) {
            rows++;
        }
        return rows;
    }

    private int setRows(ArrayList<Card> inHand, Set<Card> seen){
        return inHand.size() + seen.size() + 2;
    }

}
