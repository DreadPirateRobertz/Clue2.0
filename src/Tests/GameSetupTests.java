package Tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameSetupTests {
    // NOTE: I made Board static because I only want to set it up one
    // time (using @BeforeAll), no need to do setup before each test.
    private static Board board;

    @BeforeAll
    public static void setUp() {
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load BOTH config files
        board.initialize();
    }
    @Test
    public void testPlayerNames() {
        assertEquals(6, board.getPlayerCount());//Counts players
        assertTrue(board.isValidPlayer("Prisoner Shifty Eyes"));//Testing names
        assertTrue(board.isValidPlayer("Doctor Petunia"));
        assertTrue(board.isValidPlayer("Ensign Larry"));
        assertTrue(board.isValidPlayer("Commander Sassafras"));
        assertTrue(board.isValidPlayer("Whipping Boy Todd"));
        assertTrue(board.isValidPlayer("Mad Scientist Mikey"));
        assertFalse(board.isValidPlayer("Rusty Shackleford"));
    }
    @Test
    public void testPlayerColors() {
        assertEquals(Color.cyan, board.getPlayer("Prisoner Shifty Eyes").getColor());//Test for colors
        assertEquals(Color.green, board.getPlayer("Doctor Petunia").getColor());
        assertEquals(Color.red, board.getPlayer("Ensign Larry").getColor());
        assertEquals(Color.blue, board.getPlayer("Commander Sassafras").getColor());
        assertEquals(Color.orange, board.getPlayer("Whipping Boy Todd").getColor());
        assertEquals(Color.magenta, board.getPlayer("Mad Scientist Mikey").getColor());
    }
    @Test
    public void testPlayerStartLocation() {
        assertEquals("Brig", board.getPlayer("Prisoner Shifty Eyes").getStartLocation());//Test for StartLocation
        assertEquals("Engine", board.getPlayer("Commander Sassafras").getStartLocation());
        assertEquals("Medical", board.getPlayer("Doctor Petunia").getStartLocation());
        assertEquals("ImmersiveVR", board.getPlayer("Ensign Larry").getStartLocation());
        assertEquals("Laboratory", board.getPlayer("Mad Scientist Mikey").getStartLocation());
        assertEquals("Galley", board.getPlayer("Whipping Boy Todd").getStartLocation());
    }
    @Test
    public void testPlayerHumanity() {
        assertEquals(Human.class, board.getPlayer("Prisoner Shifty Eyes").getClass());//Test for Human/Computer players
        assertEquals(Computer.class, board.getPlayer("Ensign Larry").getClass());
        assertEquals(Computer.class, board.getPlayer("Doctor Petunia").getClass());
        assertEquals(Computer.class, board.getPlayer("Mad Scientist Mikey").getClass());
        assertEquals(Computer.class, board.getPlayer("Commander Sassafras").getClass());
        assertEquals(Computer.class, board.getPlayer("Whipping Boy Todd").getClass());
    }
    @Test
    public void testCards() {
        assertEquals(21, board.getAllCardsSize());
        assertEquals(6, board.getPlayerCardTypeCount());
        assertEquals(9, board.getRoomCardTypeCount());
        assertEquals(6, board.getWeaponCardTypeCount());
    }
    @Test
    public void testSolution(){
        assertNotNull(Board.getTheAnswer_Room());
        assertNotNull(Board.getTheAnswer_Person());
        assertNotNull(Board.getTheAnswer_Weapon());
        assertEquals(Card.class, Board.getTheAnswer_Person().getClass());
        assertEquals(Card.class, Board.getTheAnswer_Room().getClass());
        assertEquals(Card.class, Board.getTheAnswer_Room().getClass());
        
        assertNotEquals(Board.getTheAnswer_Room(), board.getPlayerMapValues().contains(Board.getTheAnswer_Room()));//Checks that solution is not among the cards dealt to Players
        assertNotEquals(Board.getTheAnswer_Person(), board.getPlayerMapValues().contains(Board.getTheAnswer_Person()));
        assertNotEquals(Board.getTheAnswer_Weapon(), board.getPlayerMapValues().contains(Board.getTheAnswer_Weapon()));
    }
    @Test
    public void testPlayerDeal(){
        assertEquals(18, board.getTotalCardsDealtToPlayers());//Total number of cards held by players after deal
        assertEquals(3, board.getPlayer("Ensign Larry").getMyCards().size());//Assuring even number of cards dealt
        assertEquals(3, board.getPlayer("Doctor Petunia").getMyCards().size());
        assertEquals(3, board.getPlayer("Commander Sassafras").getMyCards().size());
        assertEquals(3, board.getPlayer("Mad Scientist Mikey").getMyCards().size());
        assertEquals(3, board.getPlayer("Whipping Boy Todd").getMyCards().size());
        assertEquals(3, board.getPlayer("Prisoner Shifty Eyes").getMyCards().size());
        assertNotEquals(board.getPlayer("Doctor Petunia").getMyCards(), board.getPlayer("Prisoner Shifty Eyes").getMyCards()); //Testing if any cards dealt twice
        assertNotEquals(board.getPlayer("Whipping Boy Todd").getMyCards(), board.getPlayer("Mad Scientist Mikey").getMyCards());
        assertNotEquals(board.getPlayer("Ensign Larry").getMyCards(), board.getPlayer("Prisoner Shifty Eyes").getMyCards());
        assertNotEquals(board.getPlayer("Commander Sassafras").getMyCards(), board.getPlayer("Ensign Larry").getMyCards());
        assertNotEquals(board.getPlayer("Commander Sassafras").getMyCards(), board.getPlayer("Mad Scientist Mikey").getMyCards());
        assertNotEquals(board.getPlayer("Whipping Boy Todd").getMyCards(), board.getPlayer("Ensign Larry").getMyCards());
    }
    @Test//Did a test with the loop at 1M and takes ~3m 8s and did a test with 10M and took 34 minutes
    public void testRandomDeal() {
        Set<ArrayList<Card>> lottaDeals = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            board.deal();
            lottaDeals.add(board.getPlayerMapValues());
        }
        assertEquals(lottaDeals.size(), 100);
    }
}
