package Tests;

import clueGame.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void testPlayers(){

    }
}
