package Tests;

/*
 * This program tests that config files are loaded properly.
 */

// Doing a static import allows me to write assertEquals rather than
// Assert.assertEquals

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import clueGame.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;



public class FileInitTests306 {
	// Constants that I will use to test whether the file was loaded correctly
	public static final int LEGEND_SIZE = 11;
	public static final int NUM_ROWS = 25;               //Remember to change these on the board as well!!!!
	public static final int NUM_COLUMNS = 24;

	// NOTE: I made Board static because I only want to set it up one
	// time (using @BeforeAll), no need to do setup before each test.
	private static Board board;

	@BeforeAll
	public static void setUp() throws FileNotFoundException {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// set the file names to use my config files
		board.setConfigFiles("ClueLayout306.csv", "ClueSetup306.txt");
		// Initialize will load BOTH config files
		board.initialize();
	}

	@Test
	public void testRoomLabels() {
		// To ensure data is correctly loaded, test retrieving a few rooms
		// from the hash, including the first and last in the file and a few others
		Assertions.assertEquals("Conservatory", board.getRoom('C').getName() );
		Assertions.assertEquals("Ballroom", board.getRoom('B').getName() );
		Assertions.assertEquals("Billiard Room", board.getRoom('R').getName() );
		Assertions.assertEquals("Dining Room", board.getRoom('D').getName() );
		Assertions.assertEquals("Walkway", board.getRoom('W').getName() );
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
		BoardCell cell = board.getCell(8, 7);
		Assertions.assertTrue(cell.isDoorway());
		Assertions.assertEquals(DoorDirection.LEFT, cell.getDoorDirection());
		cell = board.getCell(7, 12);
		Assertions.assertTrue(cell.isDoorway());
		Assertions.assertEquals(DoorDirection.UP, cell.getDoorDirection());
		cell = board.getCell(4, 8);
		Assertions.assertTrue(cell.isDoorway());
		Assertions.assertEquals(DoorDirection.RIGHT, cell.getDoorDirection());
		cell = board.getCell(16, 9);
		Assertions.assertTrue(cell.isDoorway());
		Assertions.assertEquals(DoorDirection.DOWN, cell.getDoorDirection());
		// Test that walkways are not doors
		cell = board.getCell(12, 14);
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
		Assertions.assertEquals(17, numDoors);
	}

	// Test a few room cells to ensure the room initial is correct.
	@Test
	public void testRooms() {
		// just test a standard room location
		BoardCell cell = board.getCell( 23, 23);
		Room room = board.getRoom( cell ) ;
		Assertions.assertTrue( room != null );
		Assertions.assertEquals( room.getName(), "Kitchen" ) ;
		Assertions.assertFalse( cell.isLabel() );
		Assertions.assertFalse( cell.isRoomCenter() ) ;
		Assertions.assertFalse( cell.isDoorway()) ;

		// this is a label cell to test
		cell = board.getCell(2, 19);
		room = board.getRoom( cell ) ;
		Assertions.assertTrue( room != null );
		Assertions.assertEquals( room.getName(), "Lounge" ) ;
		Assertions.assertTrue( cell.isLabel() );
		Assertions.assertTrue( room.getLabelCell() == cell );
		
		// this is a room center cell to test
		cell = board.getCell(20, 11);
		room = board.getRoom( cell ) ;
		Assertions.assertTrue( room != null );
		Assertions.assertEquals( room.getName(), "Ballroom" ) ;
		Assertions.assertTrue( cell.isRoomCenter() );
		Assertions.assertTrue( room.getCenterCell() == cell );
		
		// this is a secret passage test
		cell = board.getCell(3, 0);
		room = board.getRoom( cell ) ;
		Assertions.assertTrue( room != null );
		Assertions.assertEquals( room.getName(), "Study" ) ;
		Assertions.assertTrue( cell.getSecretPassage() == 'K' );
		
		// test a walkway
		cell = board.getCell(5, 0);
		room = board.getRoom( cell ) ;
		// Note for our purposes, walkways and closets are rooms
		Assertions.assertTrue( room != null );
		Assertions.assertEquals( room.getName(), "Walkway" ) ;
		Assertions.assertFalse( cell.isRoomCenter() );
		Assertions.assertFalse( cell.isLabel() );
		
		// test a closet
		cell = board.getCell(24, 18);
		room = board.getRoom( cell ) ;
		Assertions.assertTrue( room != null );
		Assertions.assertEquals( room.getName(), "Unused" ) ;
		Assertions.assertFalse( cell.isRoomCenter() );
		Assertions.assertFalse( cell.isLabel() );
		
	}

}
