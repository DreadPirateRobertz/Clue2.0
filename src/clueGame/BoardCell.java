package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {

    private int row,col;
    private char initial;
    private char secretPassage = '0';
    private DoorDirection doorDirection;
    private boolean roomLabel;
    private boolean roomCenter;
    private boolean doorway;
    private boolean occupied;
    private Set<BoardCell> adjList;

    public BoardCell(int row, int col) { //Parameterized Constructor
        adjList = new HashSet<>();  //Initialize adjacency list
        this.row = row;
        this.col = col;
        doorDirection = DoorDirection.NONE;
    }

    //Getters
    public Set<BoardCell> getAdjList() {
        return adjList;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getInitial() {
        return initial;
    }

    public DoorDirection getDoorDirection() {
        return doorDirection;
    }

    public char getSecretPassage() {
        return secretPassage;
    }

    public boolean getOccupied() {
        return occupied;
    }

    public boolean isDoorway() {
        return doorway;
    }

    public boolean isLabel() {
        return roomLabel;
    }

    public boolean isRoomCenter() {
        return roomCenter;
    }

    //Setters
    public void addAdjacency(BoardCell cell) {
        adjList.add(cell);
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setInitial(char initial) {
        this.initial = initial;
    }

    public void setDoorway() {
        doorway = true;
    }

    public void setDoorDirection(DoorDirection doorDirection) {
        this.doorDirection = doorDirection;
    }

    public void setLabel() {
        roomLabel = true;
    }

    public void setRoomCenter() {
        roomCenter = true;
    }

    public void setSecretPassage(char secretPassage) {
        this.secretPassage = secretPassage;
    }

    public void setOccupied(boolean b) {
        occupied = b;
    }
}
