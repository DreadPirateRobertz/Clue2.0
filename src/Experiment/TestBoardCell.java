
package Experiment;

import java.util.HashSet;
import java.util.Set;

public class TestBoardCell {
    private int x, y;
    private Set<TestBoardCell> adjList;
    private boolean room; //False
    private boolean occupied;//False

    
    public TestBoardCell(int x, int y){
        adjList = new HashSet<TestBoardCell>();  //Intialize adjacency list
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    //Setter for adding a cell to adjacency list  //Working on switching this method to TestBoard
    public void addAdjacency(TestBoardCell cell) {
        adjList.add(cell);
    }
    //Returns the adjacency list
    public Set<TestBoardCell> getAdjList(){
        return adjList;
    }

    //Indicates a cell is part of a room
    public void setIsRoom(boolean currentCell){
        room = currentCell;
    }
    public boolean getIsRoom(){
        return room;
    }

    //Indicates a cell is occupied by another player
    public void setOccupied(boolean currentCell) {
        occupied = currentCell;
    }
    public boolean getOccupied(){
        return occupied;
    }
}