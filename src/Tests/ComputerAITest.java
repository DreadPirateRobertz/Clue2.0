package Tests;

import clueGame.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
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
    @BeforeAll
    public static void setUp(){
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load BOTH config files
        board.initialize();
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
        cardLoader = new ArrayList<>();
        player1 = new Computer("P1", Color.cyan, "Ordnance");
        player2 = new Computer("P2", Color.green, "Airlock");
        player3 = new Computer("P3", Color.pink, "Medical");

        player1.updateHand(rustyShankCard);
        player1.updateHand(medical);
        player1.updateHand(csCard);
        player2.updateHand(syringeCard);
        player2.updateHand(therapy);
        player2.updateHand(ordnance);
        player3.updateHand(meatHookCard);
        player3.updateHand(wbtCard);
        player3.updateHand(vr);
        player1.setPlayer_RowCol();
        player2.setPlayer_RowCol();
        player3.setPlayer_RowCol();
    }
    @Test
    public void testCreateSuggestion1(){ //Next 3 tests will ensure that createSuggestion is running properly
        s = player1.createSuggestion();
        assertNotEquals(s.getPersonCard(), csCard);
        assertNotEquals(s.getRoomCard(), medical);
        assertNotEquals(s.getWeaponCard(), rustyShankCard);
        assertEquals(s.getRoomCard().getCardName(), player1.getStartLocation()); //Chooses the room card it's in
        Card card = board.handleSuggestion(player1, s);
        assertTrue(player1.getMyCards().contains(card)); //The card that disproves suggestion is added to the "seen" list or my deck of cards
    }
    @Test
    public void testCreateSuggestion2(){
        s = player2.createSuggestion();
        assertNotEquals(s.getRoomCard(), ordnance);
        assertNotEquals(s.getRoomCard(), therapy);
        assertNotEquals(s.getWeaponCard(), syringeCard);
        assertEquals(s.getRoomCard().getCardName(), player2.getStartLocation());
        Card card = board.handleSuggestion(player2, s);
        assertTrue(player2.getMyCards().contains(card));
    }
    @Test
    public void testCreateSuggestion3(){
        s = player3.createSuggestion();
        assertNotEquals(s.getPersonCard(), wbtCard);
        assertNotEquals(s.getRoomCard(), vr);
        assertNotEquals(s.getWeaponCard(), meatHookCard);
        assertEquals(s.getRoomCard().getCardName(), player3.getStartLocation());
        Card card = board.handleSuggestion(player3, s);
        assertTrue(player3.getMyCards().contains(card));
    }
    @Test
    public void testCreateSuggestion4(){ //Select Last weapon
        player1.getMyCards().clear();
        player1.updateHand(rustyShankCard);
        player1.updateHand(meatHookCard);
        player1.updateHand(garroteCard);
        player1.updateHand(syringeCard);
        player1.updateHand(plasmaRifleCard);
        s = player1.createSuggestion();
        assertTrue(s.getWeaponCard().equals(targetedThermoCard));
    }
    @Test
    public void testCreateSuggestion5(){ //Select Last Person
        player1.getMyCards().clear();
        player1.updateHand(wbtCard);
        player1.updateHand(csCard);
        player1.updateHand(elCard);
        player1.updateHand(dpCard);
        player1.updateHand(pseCard);
        s = player1.createSuggestion();
        assertTrue(s.getPersonCard().equals(msmCard));
    }
    @Test
    public void testCreateSuggestion6() { //Testing random selection of weapon and random selection of people
        Set<Card> weaponsList = new HashSet<>();
        Set<Card> personList = new HashSet<>();
        Card card;
        player1.getMyCards().clear();
        player1.updateHand(rustyShankCard);
        player1.updateHand(medical);
        player1.updateHand(csCard);
        for(int i = 0; i < 100 ; i++){
            s = player1.createSuggestion();
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
        player1.getMyCards().clear();
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
        BoardCell cell = player1.selectTargets();
        assertEquals('B', cell.getInitial());
        player1.getMyCards().clear();
    }
    @Test
    public void testComputerTargets2(){ //Last Room Selected
        player3.getMyCards().clear();
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
        BoardCell celly = player3.selectTargets();
        assertEquals('L', celly.getInitial());
    }
    @Test
    public void testComputerTargets3(){ //Random selection on no seen cards
        Set<BoardCell> setty = new HashSet<>();
        player2.setPlayer_RowCol();
        player2.getMyCards().clear();
        for (int i = 0; i < 2000; i++){
            setty.add(player2.selectTargets());
        }
        BoardCell brig= board.getRoom('B').getCenterCell(); //These rooms should both be contained in here as well
        BoardCell ordnance = board.getRoom('O').getCenterCell();
        assertTrue(setty.size() >= 45);
        assertTrue(setty.contains(brig));
        assertTrue(setty.contains(ordnance));
    }
    @Test
    public void testComputerTargets4(){//All rooms have been seen
        player3.updateHand(lab); //Test chooses all rooms except one I'm sitting in (medical)
        //^player3 has all room cards in Hand
        Set<BoardCell> setty = new HashSet<>();
        player3.setPlayer_RowCol();
        for (int i = 0; i < 1000; i++){
            setty.add(player3.selectTargets());
        }
        BoardCell therapy = board.getRoom('T').getCenterCell();
        BoardCell lab = board.getRoom('L').getCenterCell();
        BoardCell galley = board.getRoom('G').getCenterCell();
        assertTrue(setty.contains(therapy));
        assertTrue(setty.contains(lab));
        assertTrue(setty.contains(galley));
        player3.getMyCards().clear();
    }
}