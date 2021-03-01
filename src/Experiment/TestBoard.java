package Experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoard {

    private Set<TestBoardCell> targets;
    private Set<TestBoardCell> visited;
    private TestBoardCell[][] testGrid;
    protected final static int NUM_COLS = 4;
    protected final static int NUM_ROWS = 4;

    //Creating board
    public TestBoard() {
        visited = new HashSet<TestBoardCell>();
        targets = new HashSet<TestBoardCell>();
        testGrid = new TestBoardCell[NUM_ROWS][NUM_COLS];
        setupGameGrid();
    }

    private void setupGameGrid() {  //Sets up the game Grid by creating new cells
        createAllCells();
        findAdjacencies();
    }

    private void findAdjacencies() {
        for (int i = 0; i < NUM_ROWS; i++) {  //Sets all the adjacency lists for each cell
            for (int j = 0; j < NUM_COLS; j++) {
                calcAdjacencies(testGrid[i][j]);
            }
        }
    }

    private void createAllCells() {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                testGrid[i][j] = new TestBoardCell(i, j);
            }
        }
    }

    //This function calculates all adjacent options to the cell according to game rules: no diagonals
    public void calcAdjacencies(TestBoardCell cell){

        if(cell.getX() < NUM_ROWS -1) {
            TestBoardCell cell_Right = getCell(cell.getX() + 1, cell.getY());
            cell.addAdjacency(cell_Right); //Refactored for readability
        }
        if(cell.getX() > 0) {
            TestBoardCell cell_Left = getCell(cell.getX() - 1, cell.getY());
            cell.addAdjacency(cell_Left);
        }
        if(cell.getY() < NUM_COLS -1) {
            TestBoardCell cell_Up = getCell(cell.getX(), cell.getY() + 1);
            cell.addAdjacency(cell_Up);
        }
        if(cell.getY() > 0) {
            TestBoardCell cell_Down = getCell(cell.getX(), cell.getY() - 1);
            cell.addAdjacency(cell_Down);
        }
    }


    //Calculates legal targets for a move from startCell of length path length
    //Set up to recursive call findAllTargets
    public void calcTargets(TestBoardCell startCell, int pathLength){
        visited.clear();//Reset
        visited.add(startCell);
        targets.clear();//Reset
        findAllTargets(startCell, pathLength);
    }
    private void findAllTargets(TestBoardCell adjCell, int numSteps){
        Set<TestBoardCell> adjacencyList = adjCell.getAdjList();
        for(TestBoardCell cell : adjacencyList){
            if(visited.contains(cell) || cell.getOccupied()) {
                continue; //Critical to not do a break right here since you want it to keep cycling through the adjacencies
            }
            visited.add(cell); //Add to visited list
            if(numSteps == 1 || cell.getIsRoom()) { //Base Case
                targets.add(cell);
            }
            else {
                findAllTargets(cell, numSteps-1); //Recurse
            }
            visited.remove(cell);//Remove from visited list
        }
    }

    public Set<TestBoardCell> getTargets(){
        return targets;
    }
    public TestBoardCell getCell(int x, int y){
        return testGrid[x][y];
    }

}