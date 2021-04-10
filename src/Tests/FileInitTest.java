package Tests;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

public class FileInitTest {
    // Constants that I will use to test whether the file was loaded correctly
    public static final int LEGEND_SIZE = 11;
    public static final int NUM_ROWS = 30;
    public static final int NUM_COLUMNS = 29;

    // NOTE: I made Board static because I only want to set it up one
    // time (using @BeforeAll), no need to do setup before each test.
    private static Board board;

    @BeforeAll
    public static void setUp() throws FileNotFoundException {
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load BOTH config files
        board.initialize();
    }

    @Test
    public void testRoomLabels() {
        // To ensure data is correctly loaded, test retrieving a few rooms
        // from the hash, including the first and last in the file and a few others
        Assertions.assertEquals("Brig", board.getRoom('B').getName() );
        Assertions.assertEquals("Galley", board.getRoom('G').getName() );
        Assertions.assertEquals("Engine", board.getRoom('E').getName() );
        Assertions.assertEquals("Medical", board.getRoom('M').getName() );
        Assertions.assertEquals("Walkway", board.getRoom('W').getName() );
        Assertions.assertEquals("Unused", board.getRoom('X').getName() );
        Assertions.assertEquals("Ordnance", board.getRoom('O').getName() );
        Assertions.assertEquals("Laboratory", board.getRoom('L').getName() );
        Assertions.assertEquals("Therapy", board.getRoom('T').getName() );
        Assertions.assertEquals("Airlock", board.getRoom('A').getName() );
        Assertions.assertEquals("ImmersiveVR", board.getRoom('I').getName() );
    }

    @Test
    public void testBoardDimensions() {
        // Ensure we have the proper number of rows and columns
        Assertions.assertEquals(NUM_ROWS, board.getNumRows());
        Assertions.assertEquals(NUM_COLUMNS, board.getNumColumns());
    }

    // Test a doorway in each direction (RIGHT/LEFT/UP/DOWN), plus
    // two cells that are not a doorway.
    // These cells are white on the planning spreadsheet
    @Test
    public void FourDoorDirections() {
        BoardCell cell = board.getCell(5, 8);
        Assertions.assertTrue(cell.isDoorway());
        Assertions.assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
        cell = board.getCell(19, 14);
        Assertions.assertTrue(cell.isDoorway());
        Assertions.assertEquals(DoorDirection.UP, cell.getDoorDirection());
        cell = board.getCell(5, 20);
        Assertions.assertTrue(cell.isDoorway());
        Assertions.assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());

        //Testing Adjacent Hallway Doors
        cell = board.getCell(20, 4);
        Assertions.assertTrue(cell.isDoorway());
        Assertions.assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
        cell = board.getCell(19, 2);
        Assertions.assertTrue(cell.isDoorway());
        Assertions.assertEquals(DoorDirection.UP, cell.getDoorDirection());

        // Test that hallways are not doors
        cell = board.getCell(21, 7);
        Assertions.assertFalse(cell.isDoorway());
    }

    // Test that we have the correct number of doors
    @Test
    public void testNumberOfDoorways() {
        int numDoors = 0;
        for (int row = 0; row < board.getNumRows(); row++)
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                if (cell.isDoorway())
                    numDoors++;
            }
        Assertions.assertEquals(26, numDoors);
    }

    // Test a few room cells to ensure the room initial is correct.
    @Test
    public void testRooms() {
        // just test a standard room location
        BoardCell cell = board.getCell( 6, 6);
        Room room = board.getRoom( cell ) ;
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Therapy" ) ;
        Assertions.assertFalse( cell.isLabel() );
        Assertions.assertFalse( cell.isRoomCenter() ) ;
        Assertions.assertFalse( cell.isDoorway()) ;

        // just test a standard room location
        cell = board.getCell( 25, 23);
        room = board.getRoom( cell ) ;
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Airlock" ) ;
        Assertions.assertFalse( cell.isLabel() );
        Assertions.assertFalse( cell.isRoomCenter() ) ;
        Assertions.assertFalse( cell.isDoorway()) ;

        // this is a label cell to test
        cell = board.getCell(15, 14);
        room = board.getRoom( cell ) ;
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Engine" ) ;
        Assertions.assertTrue( cell.isLabel() );
        Assertions.assertSame(room.getLabelCell(), cell);

        // this is a room center cell to test
        cell = board.getCell(2, 13);
        room = board.getRoom( cell ) ;
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Medical" ) ;
        Assertions.assertFalse( cell.isLabel());
        Assertions.assertTrue(cell.isRoomCenter());
        Assertions.assertSame(room.getCenterCell(), cell);

        // this is a room center cell to test
        cell = board.getCell(6, 23);
        room = board.getRoom( cell ) ;
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Galley" ) ;
        Assertions.assertTrue( cell.isRoomCenter() );
        Assertions.assertSame(room.getCenterCell(), cell);

        // this is a secret passage test
        cell = board.getCell(25, 14);
        room = board.getRoom( cell ) ;
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Ordnance" ) ;
        Assertions.assertEquals(cell.getSecretPassage(), 'B');
        Assertions.assertSame(room.getSecretCell(), cell); //Added this to default testing so room knows of the secret passage as well

        // this is a secret passage test
        cell = board.getCell(27, 7);
        room = board.getRoom( cell ) ;
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Laboratory" ) ;
        Assertions.assertEquals(cell.getSecretPassage(), 'M');
        Assertions.assertSame(room.getSecretCell(), cell); //Added this to default testing so room knows of the secret passage as well

        // test a Hallway
        cell = board.getCell(8, 10);
        room = board.getRoom( cell );
        // Note for our purposes, hallways and closets are rooms
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Walkway" );
        Assertions.assertFalse( cell.isRoomCenter() );
        Assertions.assertFalse( cell.isLabel() );

        // test an unused spaceship area
        cell = board.getCell(2, 2);
        room = board.getRoom( cell ) ;
        Assertions.assertNotNull(room);
        Assertions.assertEquals( room.getName(), "Unused" ) ;
        Assertions.assertFalse( cell.isRoomCenter() );
        Assertions.assertFalse( cell.isLabel() );

    }

}
