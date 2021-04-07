package clueGame;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameCardsPanel extends JPanel {
    private static Card rustyShankCard, targetedThermoCard, plasmaRifleCard, garroteCard, meatHookCard, syringeCard, wbtCard, csCard, elCard, dpCard, msmCard, pseCard, brig, galley, engine, medical, airlock, vr, therapy, lab, ordnance;
    ArrayList<Card> inHandPersonCards = new ArrayList<>();
    ArrayList<Card> seenPersonCards = new ArrayList<>();
    ArrayList<Card> inHandRoomCards = new ArrayList<>();
    ArrayList<Card> seenRoomCards = new ArrayList<>();
    ArrayList<Card> inHandWeaponCards = new ArrayList<>();
    ArrayList<Card> seenWeaponCards = new ArrayList<>();

    public GameCardsPanel(ArrayList<Card> inHand, ArrayList<Card> seen) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Known Cards");
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        setBorder(titledBorder);
        setLayout(new GridLayout(3,0));
        sortCards(inHand, seen);
        createLayout();
    }

    private void sortCards(ArrayList<Card> inHand, ArrayList<Card> seen) {
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

    private void createLayout() { //Adds all the panels to the main panel
        add(peopleCardsPanel());
        add(roomCardsPanel());
        add(weaponCardsPanel());
    }


    private JPanel peopleCardsPanel(){
        JPanel panel = new JPanel();
        int rows = setRows(inHandPersonCards, seenPersonCards);
        rows = emptyDecksCheck(rows, inHandPersonCards, seenPersonCards);
        panel.setLayout(new GridLayout(rows, 0));
        panel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
        JLabel inHandLabel = new JLabel("In Hand:");
        panel.add(inHandLabel);
        setFields(panel, inHandPersonCards);
        setNone(panel, inHandPersonCards);
        JLabel seenLabel = new JLabel("Seen:");
        panel.add(seenLabel);
        setFields(panel, seenPersonCards);
        setNone(panel, seenPersonCards);
        return panel;
    }

    private JPanel roomCardsPanel(){
        JPanel panel = new JPanel();
        int rows = setRows(inHandRoomCards, seenRoomCards);
        rows = emptyDecksCheck(rows, inHandRoomCards, seenRoomCards);
        panel.setLayout(new GridLayout(rows, 0));
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
        JLabel inHandLabel = new JLabel("In Hand:");
        panel.add(inHandLabel);
        setFields(panel, inHandRoomCards);
        setNone(panel, inHandRoomCards);
        JLabel seenLabel = new JLabel("Seen:");
        panel.add(seenLabel);
        setFields(panel, seenRoomCards);
        setNone(panel, seenRoomCards);
        return panel;
    }

    private JPanel weaponCardsPanel(){
        JPanel panel = new JPanel();
        int rows = setRows(inHandWeaponCards, seenWeaponCards); //Will determine how many rows in GridLayout
        rows = emptyDecksCheck(rows, inHandWeaponCards, seenWeaponCards);
        panel.setLayout(new GridLayout(rows, 0));
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
        JLabel inHandLabel = new JLabel("In Hand:");
        panel.add(inHandLabel);
        setFields(panel, inHandWeaponCards);
        setNone(panel, inHandWeaponCards);
        JLabel seenLabel = new JLabel("Seen:");
        panel.add(seenLabel);
        setFields(panel, seenWeaponCards);
        setNone(panel, seenWeaponCards);
        return panel;
    }
    //Main\\
    public static void main(String[] args) {
        ArrayList<Card> inHand = new ArrayList<>();
        ArrayList<Card> seen = new ArrayList<>();
        //Cards for Testing
        rustyShankCard = new Card(CardType.WEAPON, "Rusty Shank");
        targetedThermoCard = new Card(CardType.WEAPON, "Targeted Thermonuclear Device");
        plasmaRifleCard = new Card(CardType.WEAPON, "Plasma Rifle");
        garroteCard = new Card(CardType.WEAPON, "Garrote");
        meatHookCard = new Card(CardType.WEAPON, "Meat Hook");
        syringeCard = new Card(CardType.WEAPON, "Syringe of Eternal Midnight");
        wbtCard = new Card(CardType.PERSON, "Whipping Boy Todd");
        csCard = new Card(CardType.PERSON, "Commander Sassafras");
        elCard = new Card(CardType.PERSON, "Ensign Larry");
        dpCard = new Card(CardType.PERSON, "Doctor Petunia");
        msmCard = new Card(CardType.PERSON, "Mad Scientist Mikey");
        pseCard = new Card(CardType.PERSON, "Prisoner Shifty Eyes");
        brig = new Card(CardType.ROOM, "Brig");
        galley = new Card(CardType.ROOM, "Galley");
        engine = new Card(CardType.ROOM, "Engine");
        medical = new Card(CardType.ROOM, "Medical");
        vr = new Card(CardType.ROOM, "ImmersiveVR");
        lab = new Card(CardType.ROOM, "Laboratory");
        airlock = new Card(CardType.ROOM, "Airlock");
        ordnance = new Card(CardType.ROOM, "Ordnance");
        therapy = new Card(CardType.ROOM, "Therapy");
        //Stress Test
        inHand.add(airlock);
        seen.add(vr);
        seen.add(brig);
        seen.add(medical);
        seen.add(lab);
        seen.add(engine);
        seen.add(ordnance);
        seen.add(therapy);
        seen.add(galley);
        seen.add(elCard);
        seen.add(csCard);
        seen.add(pseCard);
        seen.add(dpCard);
        seen.add(msmCard);
        seen.add(wbtCard);
        inHand.add(rustyShankCard);
        seen.add(targetedThermoCard);
        seen.add(syringeCard);
        seen.add(garroteCard);
        seen.add(meatHookCard);
        seen.add(plasmaRifleCard);


        GameCardsPanel cardsPanel = new GameCardsPanel(inHand, seen);
        JFrame frame = new JFrame();  // create the frame
        frame.setContentPane(cardsPanel); // put the panel in the frame
        frame.setSize(230, 900);  // size the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // allow it to close




        cardsPanel.updatePanels();
        frame.setVisible(true); // make it visible
    }

    public void updatePanels(){
        removeAll();
        createLayout();
    }
    //Setters
    private void setFields(JPanel panel, ArrayList<Card> seen) {
        for (Card card : seen) {
            JTextField field = new JTextField(card.getCardName());
            field.setEditable(false);
            panel.add(field);
        }
    }

    private void setNone(JPanel panel, ArrayList<Card> seen) {
        if (seen.size() == 0) {
            JTextField field = new JTextField("None");
            field.setEditable(false);
            panel.add(field);
        }
    }
    private int emptyDecksCheck(int rows, ArrayList<Card> inHand, ArrayList<Card> seen) {
        if (inHand.size() == 0) {
            rows++;
        }
        if (seen.size() == 0) {
            rows++;
        }
        return rows;
    }

    private int setRows(ArrayList<Card> inHand, ArrayList<Card> seen){
        return inHand.size() + seen.size() + 2;
    }

}
