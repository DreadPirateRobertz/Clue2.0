package Tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class gameSetupTests {
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
    @Test   //I kept the Solution static because as of right now I don't see the point to creating an instance
    public void testSolution(){
        assertNotNull(Solution.room);
        assertNotNull(Solution.person);
        assertNotNull(Solution.weapon);
        assertEquals(Card.class, Solution.room.getClass());
        assertEquals(Card.class, Solution.person.getClass());
        assertEquals(Card.class, Solution.weapon.getClass());
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
        assertFalse(board.getPlayer("Prisoner Shifty Eyes").getMyCards().equals(board.getPlayer("Doctor Petunia").getMyCards())); //Testing if any cards dealt twice
        assertFalse(board.getPlayer("Mad Scientist Mikey").getMyCards().equals(board.getPlayer("Whipping Boy Todd").getMyCards()));
        assertFalse(board.getPlayer("Prisoner Shifty Eyes").getMyCards().equals(board.getPlayer("Ensign Larry").getMyCards()));
        assertFalse(board.getPlayer("Ensign Larry").getMyCards().equals(board.getPlayer("Commander Sassafras").getMyCards()));
        assertFalse(board.getPlayer("Mad Scientist Mikey").getMyCards().equals(board.getPlayer("Commander Sassafras").getMyCards()));
        assertFalse(board.getPlayer("Ensign Larry").getMyCards().equals(board.getPlayer("Whipping Boy Todd").getMyCards()));
    }

    @Test
    public void testRandomDeal() {
        Set<ArrayList<Card>> lottaDeals = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            board.deal();
            lottaDeals.add(board.getPlayerMapValues());
        }
        assertTrue(lottaDeals.size() >= 999);
    }
}
