package Tests;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.DoorDirection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BoardAdjTargetTest {
    // We make the Board static because we can load it one time and
    // then do all the tests.
    private static Board board;

    @BeforeAll
    static void setUp() {
        // Board is singleton, get the only instance
        board = Board.getInstance();
        // set the file names to use my config files
        board.setConfigFiles("ClueLayout.csv", "ClueSetup.txt");
        // Initialize will load config files
        board.initialize();
    }

    // Ensure that player does not move around within room
    // These cells are LIGHT ORANGE on the planning spreadsheet
    @Test
    public void testAdjacenciesRooms() {
        // we want to test a couple of different rooms.
        // First, let's check out Medical that has two doors and a Secret PASSAGE:)
        Set<BoardCell> testList = board.getAdjList(2, 14);
        assertEquals(3, testList.size());
        assertTrue(testList.contains(board.getCell(23, 5)));
        assertTrue(testList.contains(board.getCell(3, 10)));
        assertTrue(testList.contains(board.getCell(3, 18)));

        // now test the Therapy Room
        testList = board.getAdjList(6, 5);
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(9, 2)));
        assertTrue(testList.contains(board.getCell(5, 8)));

        // one more room, the ImmersiveVR Room :)
        testList = board.getAdjList(15, 3);
        assertEquals(4, testList.size());
        assertTrue(testList.contains(board.getCell(19, 2)));
        assertTrue(testList.contains(board.getCell(15, 6)));
        assertTrue(testList.contains(board.getCell(14, 6)));
        assertTrue(testList.contains(board.getCell(10, 4)));
    }


    // Ensure door locations include their rooms and also additional walkways
    // These cells are LIGHT ORANGE on the planning spreadsheet
    @Test
    public void testAdjacencyDoor() {
        //Spaceship design of our game map causes some interesting occurrences so
        //Testing opposing Doors
        Set<BoardCell> testList = board.getAdjList(20, 4);
        assertEquals(DoorDirection.DOWN, board.getCell(20, 4).getDoorDirection());
        assertEquals(4, testList.size());
        assertTrue(testList.contains(board.getCell(20, 5)));
        assertTrue(testList.contains(board.getCell(20, 3)));
        assertTrue(testList.contains(board.getCell(19, 4)));
        assertTrue(testList.contains(board.getCell(23, 5)));//Center
        assertTrue(board.getCell(23, 5).isRoomCenter());
        assertTrue(board.getCell(19, 2).isDoorway());
        assertEquals(DoorDirection.UP, board.getCell(19, 2).getDoorDirection());

        //Uno mas :)
        testList = board.getAdjList(14, 18);
        assertEquals(DoorDirection.LEFT, board.getCell(14, 18).getDoorDirection());
        assertEquals(4, testList.size());
        assertTrue(testList.contains(board.getCell(13, 18)));
        assertTrue(testList.contains(board.getCell(14, 19)));
        assertTrue(testList.contains(board.getCell(15, 18)));
        assertTrue(testList.contains(board.getCell(15, 14)));
        assertTrue(board.getCell(15, 14).isRoomCenter());
        assertTrue(board.getCell(15, 18).isDoorway());
        assertEquals(DoorDirection.LEFT, board.getCell(15, 18).getDoorDirection());


    }

    // Test a variety of walkway scenarios
    // These tests are Light Yellow on the Test spreadsheet (ClueLayOutTest.xlsx)
    @Test
    public void testAdjacencyWalkways() {
        // Test general hallway, not adjacent to doors
        Set<BoardCell> testList = board.getAdjList(23, 17);
        assertEquals(4, testList.size());
        assertTrue(testList.contains(board.getCell(23, 18)));

        // Test adjacent to door
        testList = board.getAdjList(19, 13);
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(20, 13)));
        assertTrue(testList.contains(board.getCell(19, 14)));


        // Test adjacent to Walkways
        testList = board.getAdjList(19, 3);
        assertEquals(3, testList.size());
        assertTrue(testList.contains(board.getCell(19, 2)));
        assertTrue(testList.contains(board.getCell(19, 4)));
        assertTrue(testList.contains(board.getCell(20, 3)));

        // Test next to unused spaceship area and near door but not adjacent
        testList = board.getAdjList(10, 1);
        assertEquals(2, testList.size());
        assertTrue(testList.contains(board.getCell(9, 1)));
        assertTrue(testList.contains(board.getCell(10, 2)));

        //Test Secret passage adjacency
        testList = board.getAdjList(29, 17);
        assertEquals(0, testList.size());
        assertEquals('B',board.getCell(29, 17).getSecretPassage());

    }


    // Tests out of room center, 1, 3 and 4      //Dark Purple
    @Test
    public void testTargetsInEngineRoom() {
        // test a roll of 1 //
        board.getCell(6, 23).setOccupied(false);
        board.calcTargets(board.getCell(15, 14), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(14, 10)));
        assertTrue(targets.contains(board.getCell(15, 18)));

        // test a roll of 3
        board.calcTargets(board.getCell(15, 14), 3);
        targets = board.getTargets();
        assertEquals(22, targets.size()); //NEED TO CHECK THIS
        assertTrue(targets.contains(board.getCell(16, 19)));
        assertTrue(targets.contains(board.getCell(8, 14)));
        assertTrue(targets.contains(board.getCell(13, 19)));
        assertTrue(targets.contains(board.getCell(20, 15)));

        // test a roll of 4
        board.calcTargets(board.getCell(15, 14), 4);
        targets = board.getTargets();
        assertEquals(40, targets.size());//A lot I will have to calculate this !!!!!CHECK!!!!!!!!!!
        assertTrue(targets.contains(board.getCell(13, 20)));
        assertTrue(targets.contains(board.getCell(21, 15)));
        assertTrue(targets.contains(board.getCell(16, 20)));
        assertTrue(targets.contains(board.getCell(15, 20)));
    }

    @Test
    public void testTargetsInOrdnanceAndLabRooms() {
        // test a roll of 1
        board.calcTargets(board.getCell(27, 14), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(3, targets.size());
        assertTrue(targets.contains(board.getCell(26, 10)));
        assertTrue(targets.contains(board.getCell(26, 18)));
        //Secret Passage test
        assertTrue(targets.contains(board.getCell(15, 25)));

        // test a roll of 3
        board.calcTargets(board.getCell(27, 14), 3);
        targets = board.getTargets();
        assertEquals(11, targets.size()); //NEED TO CHECK THIS
        assertTrue(targets.contains(board.getCell(25, 9)));
        assertTrue(targets.contains(board.getCell(27, 19)));
        assertTrue(targets.contains(board.getCell(28, 18)));
        assertTrue(targets.contains(board.getCell(15, 25)));

        // test a roll of 4
        board.calcTargets(board.getCell(27, 14), 4);
        targets = board.getTargets();
        assertEquals(19, targets.size());
        assertTrue(targets.contains(board.getCell(28, 9)));
        assertTrue(targets.contains(board.getCell(27, 8)));
        assertTrue(targets.contains(board.getCell(27, 18)));
        assertTrue(targets.contains(board.getCell(25, 8)));

        //test a roll of 1 to secret passageway
        board.calcTargets(board.getCell(23, 5), 1);
        targets = board.getTargets();
        assertEquals('M',board.getCell(27, 6).getSecretPassage());
        assertEquals(3, targets.size());
        assertTrue(targets.contains(board.getCell(20, 4)));
        //Secret Passage Test
        assertTrue(targets.contains(board.getCell(2, 14)));

        //test secret passageway
        board.calcTargets(board.getCell(27, 6), 1);
        targets = board.getTargets();
        assertEquals(0, targets.size());


    }

    // Tests out of room center, 1, 3 and 4
    // These are Purple on ClueLayoutTest
    @Test
    public void testTargetsAtDoor() {
        // test a roll of 1, at door
        board.calcTargets(board.getCell(26, 10), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(27, 14)));
        assertTrue(targets.contains(board.getCell(27, 10)));
        assertTrue(targets.contains(board.getCell(25, 10)));

        // test a roll of 3
        board.calcTargets(board.getCell(26, 10), 3);
        targets = board.getTargets();
        assertEquals(10, targets.size());//CHECK !!!!!!!!!!!!!
        assertTrue(targets.contains(board.getCell(25, 8)));
        assertTrue(targets.contains(board.getCell(28, 9)));
        assertTrue(targets.contains(board.getCell(27, 14)));
        assertTrue(targets.contains(board.getCell(23, 10)));
        assertTrue(targets.contains(board.getCell(24, 11)));

        // test a roll of 4
        board.calcTargets(board.getCell(26, 10), 4);
        targets = board.getTargets();
        assertEquals(12, targets.size());//CHECK !!!!!!!!!!!!!
        assertTrue(targets.contains(board.getCell(22, 10)));
        assertTrue(targets.contains(board.getCell(27, 14)));
        assertTrue(targets.contains(board.getCell(24, 12)));
        assertTrue(targets.contains(board.getCell(23, 9)));
        assertTrue(targets.contains(board.getCell(28, 8)));
    }

    @Test
    public void testTargetsInWalkway1() {
        // test a roll of 1
        board.calcTargets(board.getCell(14, 20), 1);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(13, 20)));
        assertTrue(targets.contains(board.getCell(15, 20)));

        // test a roll of 3
        board.calcTargets(board.getCell(14, 20), 3);
        targets = board.getTargets();
        assertEquals(13, targets.size()); //CHECK!!!!!!!!!!!!!!!!!!!
        assertTrue(targets.contains(board.getCell(15, 14)));
        assertTrue(targets.contains(board.getCell(16, 21)));
        assertTrue(targets.contains(board.getCell(12, 21)));
        assertTrue(targets.contains(board.getCell(15, 25)));

        // test a roll of 4
        board.calcTargets(board.getCell(14, 20), 4);
        targets = board.getTargets();
        assertEquals(14, targets.size());//I'm getting 23 what the.....
        assertTrue(targets.contains(board.getCell(14, 18)));
        assertTrue(targets.contains(board.getCell(12, 22)));
        assertTrue(targets.contains(board.getCell(13, 19)));
        assertTrue(targets.contains(board.getCell(15, 14)));
    }

    @Test  //These are red
    // test to make sure occupied locations do not cause problems
    public void testTargetsOccupied() {
        // test a roll of 4 blocked 2 down
        board.getCell(22, 14).setOccupied(true);
        board.calcTargets(board.getCell(22, 13), 4);
        board.getCell(22, 14).setOccupied(false);
        Set<BoardCell> targets = board.getTargets();
        assertEquals(15, targets.size());//CHECK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        assertTrue(targets.contains(board.getCell(22, 15)));
        assertTrue(targets.contains(board.getCell(23, 12)));
        assertTrue(targets.contains(board.getCell(24, 15)));
        assertFalse( targets.contains( board.getCell(15, 14))) ;
        assertFalse( targets.contains( board.getCell(22, 10))) ;

        // we want to make sure we can get into a room, even if flagged as occupied
        board.getCell(6, 23).setOccupied(true);
        board.getCell(9, 26).setOccupied(true);
        board.calcTargets(board.getCell(5, 20), 2);
        board.getCell(6, 23).setOccupied(false);
        board.getCell(9, 26).setOccupied(false);
        targets= board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(6, 23)));
        assertTrue(targets.contains(board.getCell(3, 20)));

        // check leaving a room with a blocked doorway
        board.getCell(10, 14).setOccupied(true);
        board.calcTargets(board.getCell(15, 14), 3);
        board.getCell(10, 14).setOccupied(false);
        targets= board.getTargets();
        assertEquals(19, targets.size());//CHECK
        assertTrue(targets.contains(board.getCell(15, 20)));
        assertTrue(targets.contains(board.getCell(13, 10)));
        assertTrue(targets.contains(board.getCell(14, 19)));

    }

    @Test
    public void testOccupied() {
        int player = 0; //I realized I had two players being flagged as True because I assert them as true in a later target test
        ArrayList<BoardCell> players = new ArrayList<>(); //I didn't set them back to False...Critical in these Tests it seems!
        for (int row = 0; row < board.getNumRows(); row++)
            for (int col = 0; col < board.getNumColumns(); col++) {
                BoardCell cell = board.getCell(row, col);
                if (cell.getOccupied()) {
                    player++;
                    players.add(cell);
                }
            }
        Assertions.assertEquals(0, player);
    }

    //Bronze
    @Test
    public void testRooms(){
        assertEquals('I', board.getCell(12,3).getInitial());
        assertEquals("ImmersiveVR", board.getRoom(board.getCell(12,3)).getName());
        assertEquals(board.getCell(15,3), board.getRoom(board.getCell(12,3)).getCenterCell());
        assertEquals(board.getCell(14,2), board.getRoom(board.getCell(12,3)).getLabelCell());
    }

}
