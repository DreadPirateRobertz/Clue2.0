package Tests;

import Experiment.TestBoard;
import Experiment.TestBoardCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class BoardTestsExp {
    TestBoard board;

    @BeforeEach
    public void setUp(){
        board = new TestBoard();

    }
    @Test //4x4 Test Board and Testing the center & edges for expected adjacencies
    public void testAdjacency1() {
        TestBoardCell cell = board.getCell(0, 0);
        Set<TestBoardCell> testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(1, 0)));
        assertTrue(testList.contains(board.getCell(0, 1)));
        assertEquals(2, testList.size());
    }
    @Test //4x4 Test Board and Testing the center & edges for expected adjacencies
    public void testAdjacency2() {
        TestBoardCell cell = board.getCell(3, 3);
        Set<TestBoardCell> testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(3, 2)));
        assertTrue(testList.contains(board.getCell(2, 3)));
        assertEquals(2, testList.size());
    }
    @Test //4x4 Test Board and Testing the center & edges for expected adjacencies
    public void testAdjacency3() {
        TestBoardCell cell = board.getCell(1, 3);
        Set<TestBoardCell> testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(0, 3)));
        assertTrue(testList.contains(board.getCell(1, 2)));
        assertTrue(testList.contains(board.getCell(2, 3)));
        assertEquals(3, testList.size());
    }
    @Test //4x4 Test Board and Testing the center & edges for expected adjacencies
    public void testAdjacency4() {
        TestBoardCell cell = board.getCell(3, 0);
        Set<TestBoardCell> testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(2, 0)));
        assertTrue(testList.contains(board.getCell(3, 1)));
        assertEquals(2, testList.size());
    }
    @Test //4x4 Test Board and Testing the center & edges for expected adjacencies
    public void testAdjacency5(){
        TestBoardCell cell = board.getCell(2,2);
        Set<TestBoardCell> testList = cell.getAdjList();
        assertTrue(testList.contains(board.getCell(2,1)));
        assertTrue(testList.contains(board.getCell(1,2)));
        assertTrue(testList.contains(board.getCell(2,3)));
        assertTrue(testList.contains(board.getCell(3,2)));
        assertEquals(4, testList.size());
    }
    @Test  //Testing that calcTargets for potential moves without players or rooms is working correctly
    public void testTargetsNormal1() {
        TestBoardCell cell = board.getCell(0, 0);
        board.calcTargets(cell, 3);//Dice roll = 3
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(6, targets.size());
        assertTrue(targets.contains(board.getCell(3, 0)));
        assertTrue(targets.contains(board.getCell(2, 1)));
        assertTrue(targets.contains(board.getCell(0, 1)));
        assertTrue(targets.contains(board.getCell(1, 2)));
        assertTrue(targets.contains(board.getCell(0, 3)));
        assertTrue(targets.contains(board.getCell(1, 0)));
    }
    @Test
    public void testTargetsNormal2(){
        TestBoardCell cell = board.getCell(2,2);
        board.calcTargets( cell, 6); //Path length = dice roll = max 6
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(7, targets.size());
        assertTrue(targets.contains(board.getCell(0,0)));
        assertTrue(targets.contains(board.getCell(0,2)));
        assertTrue(targets.contains(board.getCell(1,1)));
        assertTrue(targets.contains(board.getCell(1,3)));
        assertTrue(targets.contains(board.getCell(2,0)));
        assertTrue(targets.contains(board.getCell(3,1)));
        assertTrue(targets.contains(board.getCell(3,3)));
    }
    @Test
    public void testTargetsMixed1(){//Testing that calcTargets for advanced moves with players and rooms added
        //Following 2 lines set up the occupied & in a room cells
        board.getCell(0,2).setOccupied(true); //Occupied by opponent
        board.getCell(1,2).setIsRoom(true); //This cell is a room
        TestBoardCell cell = board.getCell(0,3);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(3, targets.size());
        assertTrue(targets.contains(board.getCell(1,2)));
        assertTrue(targets.contains(board.getCell(2,2)));
        assertTrue(targets.contains(board.getCell(3,3)));

    }
    @Test
    public void testTargetsMixed2(){
        //Following 2 lines set up the occupied & in a room cells
        board.getCell(1,2).setOccupied(true); //Occupied by opponent
        board.getCell(3,3).setIsRoom(true); //This cell is a room
        TestBoardCell cell = board.getCell(0,3);
        board.calcTargets(cell, 3);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(4, targets.size());
        assertTrue(targets.contains(board.getCell(0,0)));
        assertTrue(targets.contains(board.getCell(2,2)));
        assertTrue(targets.contains(board.getCell(3,3)));
        assertTrue(targets.contains(board.getCell(1,1)));
    }
    @Test
    public void testTargetsMixed3(){
        //Following 2 lines set up the occupied & in a room cells
        board.getCell(0,1).setOccupied(true); //Occupied by opponent
        board.getCell(1,2).setOccupied(true);
        board.getCell(0,0).setIsRoom(true); //This cell is a room
        TestBoardCell cell = board.getCell(3,0);
        board.calcTargets(cell, 4);
        Set<TestBoardCell> targets = board.getTargets();
        assertEquals(5, targets.size());
        assertTrue(targets.contains(board.getCell(0,0)));
        assertTrue(targets.contains(board.getCell(2,3)));
        assertTrue(targets.contains(board.getCell(3,2)));
        assertTrue(targets.contains(board.getCell(2,3)));
        assertTrue(targets.contains(board.getCell(1,0)));
    }

}