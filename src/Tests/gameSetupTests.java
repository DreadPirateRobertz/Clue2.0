package Tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

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
        assertEquals(18, board.getTotalCardsDealtToPlayers());
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

    //TODO: NEED TEST FOR RANDOM BEHAVIOR

}