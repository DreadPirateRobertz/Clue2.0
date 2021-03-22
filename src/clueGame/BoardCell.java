package clueGame;

import java.util.HashSet;
import java.util.Set;

public class BoardCell {
    private int row, col;
    private char initial, secretPassage;
    private DoorDirection doorDirection;
    private boolean roomCenter, roomLabel, doorway, occupied;
    private Set<BoardCell> adjList;



    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        secretPassage = '0'; //Its null setting distracts me in the debugger so I set it to '0'
        adjList = new HashSet<>(); //Initialize adjacency list
        doorDirection = DoorDirection.NONE;
    }

    //Getters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    public Set<BoardCell> getAdjList() { return adjList; }
    public char getInitial() { return initial; }
    public DoorDirection getDoorDirection() { return doorDirection; }
    public char getSecretPassage() { return secretPassage; }
    public boolean getOccupied() { return occupied; }
    //Is'ers
    public boolean isDoorway() { return doorway; }
    public boolean isLabel() { return roomLabel; }
    public boolean isRoomCenter() { return roomCenter; }
    //Setters
    public void addAdjacency(BoardCell cell) { adjList.add(cell); }
    public void setInitial(char initial) { this.initial = initial; }
    public void setDoorway() { doorway = true; }
    public void setDoorDirection(DoorDirection doorDirection) { this.doorDirection = doorDirection; }
    public void setLabel() { roomLabel = true; }
    public void setRoomCenter() { roomCenter = true; }
    public void setSecretPassage(char secretPassage) { this.secretPassage = secretPassage; }
    public void setOccupied(boolean b) { occupied = b; }
}
