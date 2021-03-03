package Tests;

/*
 * This program tests that, when loading config files, exceptions
 * are thrown appropriately.
 */

import clueGame.BadConfigFormatException;
import clueGame.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExceptionTests306 {

    // Test that an exception is thrown for a config file that does not
    // have the same number of columns for each row
    @Test
    public void testBadColumns() {
        Assertions.assertThrows(BadConfigFormatException.class, () -> {
            // Note that we are using a LOCAL Board variable, because each
            // test will load different files
            Board board = Board.getInstance();
            board.setConfigFiles("ClueLayoutBadColumns306.csv", "ClueSetup306.txt");
            // Instead of initialize, we call the two load functions directly.
            // This is necessary because initialize contains a try-catch.
            board.loadSetupConfig();
            // This one should throw an exception
            board.loadLayoutConfig();
        });
    }

    // Test that an exception is thrown for a config file that specifies
    // a room that is not in the legend. See first test for other important
    // comments.
    @Test
    public void testBadRoom() {
        Assertions.assertThrows(BadConfigFormatException.class, () -> {
            Board board = Board.getInstance();
            board.setConfigFiles("ClueLayoutBadRoom306.csv", "ClueSetup306.txt");
            board.loadSetupConfig();
            board.loadLayoutConfig();
        });
    }

    // Test that an exception is thrown for a config file with a room type
    // that is not Card or Other
    @Test
    public void testBadRoomFormat() {
        Assertions.assertThrows(BadConfigFormatException.class, () -> {
            Board board = Board.getInstance();
            board.setConfigFiles("ClueLayout306.csv", "ClueSetupBadFormat306.txt");
            board.loadSetupConfig();
            board.loadLayoutConfig();
        });
    }
}
