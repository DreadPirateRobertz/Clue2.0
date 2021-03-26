package Tests;

import clueGame.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/*
//Rooms
Room, Brig, B
Room, Galley, G
Room, Engine, E
Room, Medical, M
Room, Airlock, A
Room, ImmersiveVR, I
Room, Therapy, T
Room, Laboratory, L
Room, Ordnance, O
//Other Spaces
Space, Unused, X
Space, Walkway, W
//People
Person, Whipping Boy Todd, Orange, Computer, Galley
Person, Commander Sassafras, Blue, Computer, Engine
Person, Ensign Larry, Red, Computer, ImmersiveVR
Person, Doctor Petunia, Green, Computer, Medical
Person, Mad Scientist Mikey, Magenta, Computer, Laboratory
Person, Prisoner Shifty Eyes, Cyan, Human, Brig
//Weapons
Weapon, Rusty Shank
Weapon, Targeted Thermonuclear Device
Weapon, Plasma Rifle
Weapon, Garrote
Weapon, Meat Hook
Weapon, Syringe of Eternal Midnight
 */

public class ComputerAITest {
    private static Board board;
    private static Card rustyShankCard, targetedThermoCard, plasmaRifleCard, garroteCard, meatHookCard, syringeCard, wbtCard, csCard, elCard, dpCard, msmCard, pseCard, brig, galley, engine, medical, airlock, vr, therapy, lab, ordnance;
    private static Player player1, player2, player3;
    private static ArrayList<Card> cardLoader;
    private static Suggestion s;
    private static BoardCell celly1, celly2, celly3;
    private static Room room1, room2, room3;
    private static ArrayList<BoardCell> targets1, targets2, targets3;
    @BeforeAll
    public static void setUp() {
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load BOTH config files
        board.initialize();

        cardLoader = new ArrayList<>();
        targets1 = new ArrayList<>();
        targets2 = new ArrayList<>();
        targets3 = new ArrayList<>();

        cardLoader.add(rustyShankCard = new Card(CardType.WEAPON, "Rusty Shank"));
        cardLoader.add(targetedThermoCard = new Card(CardType.WEAPON, "Targeted Thermonuclear Device"));
        cardLoader.add(plasmaRifleCard = new Card(CardType.WEAPON, "Plasma Rifle"));
        cardLoader.add(garroteCard = new Card(CardType.WEAPON, "Garrote"));
        cardLoader.add(meatHookCard = new Card(CardType.WEAPON, "Meat Hook"));
        cardLoader.add(syringeCard = new Card(CardType.WEAPON, "Syringe of Eternal Midnight"));
        cardLoader.add(wbtCard = new Card(CardType.PERSON, "Whipping Boy Todd"));
        cardLoader.add(csCard = new Card(CardType.PERSON, "Commander Sassafras"));
        cardLoader.add(elCard = new Card(CardType.PERSON, "Ensign Larry"));
        cardLoader.add(dpCard = new Card(CardType.PERSON, "Doctor Petunia"));
        cardLoader.add(msmCard = new Card(CardType.PERSON, "Mad Scientist Mikey"));
        cardLoader.add(pseCard = new Card(CardType.PERSON, "Prisoner Shifty Eyes"));
        cardLoader.add(brig = new Card(CardType.ROOM, "Brig"));
        cardLoader.add(galley = new Card(CardType.ROOM, "Galley"));
        cardLoader.add(engine = new Card(CardType.ROOM, "Engine"));
        cardLoader.add(medical = new Card(CardType.ROOM, "Medical"));
        cardLoader.add(vr = new Card(CardType.ROOM, "ImmersiveVR"));
        cardLoader.add(lab = new Card(CardType.ROOM, "Laboratory"));
        cardLoader.add(airlock = new Card(CardType.ROOM, "Airlock"));
        cardLoader.add(ordnance = new Card(CardType.ROOM, "Ordnance"));
        cardLoader.add(therapy = new Card(CardType.ROOM, "Therapy"));

        player1 = new Computer("P1", Color.cyan, "Ordnance");
        player2 = new Computer("P2", Color.green, "Airlock");
        player3 = new Computer("P3", Color.pink, "Medical");

        player1.setPlayer_RowCol();
        player2.setPlayer_RowCol();
        player3.setPlayer_RowCol();

        celly1 = Board.getInstance().getCell(player1.getRow(), player1.getCol());
        room1 = Board.getInstance().getRoom(celly1);
        celly2 = Board.getInstance().getCell(player2.getRow(), player2.getCol());
        room2 = Board.getInstance().getRoom(celly2);
        celly3 = Board.getInstance().getCell(player3.getRow(), player3.getCol());
        room3 = Board.getInstance().getRoom(celly3);
        //Player 1 targets
        targets1.add(Board.getInstance().getCell(26, 10));
        targets1.add(Board.getInstance().getCell(23, 6));
        targets1.add(Board.getInstance().getCell(19, 14));
        targets1.add(Board.getInstance().getCell(27, 10));
        targets1.add(Board.getInstance().getCell(15, 25));
        //Player 2 targets
        targets2.add(Board.getInstance().getCell(15, 25));
        targets2.add(Board.getInstance().getCell(10, 24));
        targets2.add(Board.getInstance().getCell(27, 14));
        targets2.add(Board.getInstance().getCell(15, 22));
        targets2.add(Board.getInstance().getCell(20, 24));
        //Player 3 targets
        targets3.add(Board.getInstance().getCell(23, 5));
        targets3.add(Board.getInstance().getCell(3, 10));
        targets3.add(Board.getInstance().getCell(6, 5));
        targets3.add(Board.getInstance().getCell(3, 18));
        targets3.add(Board.getInstance().getCell(6, 23));
    }
    @Test
    public void testCreateSuggestion1(){ //Next 3 tests will ensure that createSuggestion is running properly
        player1.updateHand(rustyShankCard);
        player1.updateHand(medical);
        player1.updateHand(csCard);
        s = player1.createSuggestion(room1, cardLoader);
        assertNotEquals(s.getPersonCard(), csCard);
        assertNotEquals(s.getRoomCard(), medical);
        assertNotEquals(s.getWeaponCard(), rustyShankCard);
        assertEquals(s.getRoomCard().getCardName(), player1.getStartLocation()); //Chooses the room card it's in
        Card card = board.handleSuggestion(player1, s);
        assertTrue(player1.getSeenList().contains(card)); //The card that disproves suggestion is added to the seenList for that player
    }
    @Test
    public void testCreateSuggestion2(){
        player2.updateHand(syringeCard);
        player2.updateHand(therapy);
        player2.updateHand(ordnance);
        s = player2.createSuggestion(room2, cardLoader);
        assertNotEquals(s.getRoomCard(), ordnance);
        assertNotEquals(s.getRoomCard(), therapy);
        assertNotEquals(s.getWeaponCard(), syringeCard);
        assertEquals(s.getRoomCard().getCardName(), player2.getStartLocation());
        Card card = board.handleSuggestion(player2, s);
        assertTrue(player2.getSeenList().contains(card));
    }
    @Test
    public void testCreateSuggestion3(){
        player3.updateHand(meatHookCard);
        player3.updateHand(wbtCard);
        player3.updateHand(vr);
        s = player3.createSuggestion(room3, cardLoader);
        assertNotEquals(s.getPersonCard(), wbtCard);
        assertNotEquals(s.getRoomCard(), vr);
        assertNotEquals(s.getWeaponCard(), meatHookCard);
        assertEquals(s.getRoomCard().getCardName(), player3.getStartLocation());
        Card card = board.handleSuggestion(player3, s);
        assertTrue(player3.getSeenList().contains(card));
    }
    @Test
    public void testCreateSuggestion4(){ //Select Last weapon
        player1.getPlayerHand().clear();
        player1.updateHand(rustyShankCard);
        player1.updateHand(meatHookCard);
        player1.updateHand(garroteCard);
        player1.updateHand(syringeCard);
        player1.updateHand(plasmaRifleCard);
        s = player1.createSuggestion(room1, cardLoader);
        assertTrue(s.getWeaponCard().equals(targetedThermoCard));
    }
    @Test
    public void testCreateSuggestion5(){ //Select Last Person
        player1.getPlayerHand().clear();
        player1.updateHand(wbtCard);
        player1.updateHand(csCard);
        player1.updateHand(elCard);
        player1.updateHand(dpCard);
        player1.updateHand(pseCard);
        s = player1.createSuggestion(room1, cardLoader);
        assertTrue(s.getPersonCard().equals(msmCard));
    }
    @Test
    public void testCreateSuggestion6() { //Testing random selection of weapon and random selection of people
        Set<Card> weaponsList = new HashSet<>();
        Set<Card> personList = new HashSet<>();
        Card card;
        player1.getPlayerHand().clear();
        player1.updateHand(rustyShankCard);
        player1.updateHand(medical);
        player1.updateHand(csCard);
        for(int i = 0; i < 100 ; i++){
            s = player1.createSuggestion(room1, cardLoader);
            weaponsList.add(s.getWeaponCard());
            personList.add(s.getPersonCard());
        }
        assertEquals(5, weaponsList.size()); //Should be 5 weapons
        assertFalse(weaponsList.contains(rustyShankCard)); //Should not have this card
        assertEquals(5, personList.size());
        assertFalse(personList.contains(csCard));
    }
    @Test
    public void testComputerTargets1(){ //Select Last Room
        player1.getPlayerHand().clear();
        player1.updateHand(vr);
        player1.updateHand(therapy);
        player1.updateHand(medical);
        player1.updateHand(lab);
        player1.updateHand(meatHookCard); //Making sure these get edited out of rotation
        player1.updateHand(rustyShankCard);
        player1.updateHand(ordnance);
        player1.updateHand(engine);
        player1.updateHand(galley);
        player1.updateHand(airlock);
        BoardCell cell = player1.selectTargets(targets1);
        assertEquals('B', cell.getInitial());
        player1.getSeenList().clear();
    }
    @Test
    public void testComputerTargets2(){ //Last Room Selected
        player3.getPlayerHand().clear();
        player3.updateHand(syringeCard); //Making sure it skips over non-Room Cards
        player3.updateHand(targetedThermoCard);
        player3.updateHand(vr);
        player3.updateHand(therapy);
        player3.updateHand(medical);
        player3.updateHand(ordnance);
        player3.updateHand(engine);
        player3.updateHand(galley);
        player3.updateHand(airlock);
        player3.updateHand(brig);
        BoardCell celly = player3.selectTargets(targets3);
        assertEquals('L', celly.getInitial());
    }
    @Test
    public void testComputerTargets3(){ //Random selection on no seen cards
        Set<BoardCell> setty = new HashSet<>();
        player2.setPlayer_RowCol();
        player2.getSeenList().clear();
        for (int i = 0; i < 100; i++){
            setty.add(player2.selectTargets(targets2));
        }
        BoardCell brig= board.getRoom('B').getCenterCell(); //These rooms should both be contained in here as well
        BoardCell ordnance = board.getRoom('O').getCenterCell();
        assertTrue(setty.size() == 5);
        assertTrue(setty.contains(brig));
        assertTrue(setty.contains(ordnance));
    }
    @Test
    public void testComputerTargets4(){//All rooms have been seen
        player3.updateHand(lab); //Test chooses all rooms except one I'm sitting in (medical)
        //^player3 has all room cards in Hand
        Set<BoardCell> setty = new HashSet<>();
        player3.setPlayer_RowCol();
        for (int i = 0; i < 100; i++){
            setty.add(player3.selectTargets(targets3));
        }
        BoardCell therapy = board.getRoom('T').getCenterCell();
        BoardCell lab = board.getRoom('L').getCenterCell();
        BoardCell galley = board.getRoom('G').getCenterCell();
        assertEquals(5, setty.size());
        assertTrue(setty.contains(therapy));
        assertTrue(setty.contains(lab));
        assertTrue(setty.contains(galley));
        player3.getSeenList().clear();
    }
}