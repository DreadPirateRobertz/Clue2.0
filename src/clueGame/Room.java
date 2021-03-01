package clueGame;

import java.util.ArrayList;

public class Room {
    private String name;
    private char identifier;
    private BoardCell labelCell;
    private BoardCell centerCell;
    private BoardCell secretCell;
    private ArrayList<BoardCell> doorCells = new ArrayList<>();

    public ArrayList<BoardCell> getDoorCells() {
        return doorCells;  //Nice now it'll be easy to retrieve all the doors when doing the center (*) adjacency list
    }

    public void setDoorCell(BoardCell doorCell) {
        doorCells.add(doorCell);
    }
    public void setIdentifier(char identifier) {
        this.identifier = identifier;
    }
    public char getIdentifier() {
        return identifier;
    }
    public BoardCell getSecretCell() { return secretCell;}
    public void setSecretCell(BoardCell secretCell) { this.secretCell = secretCell; }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public BoardCell getLabelCell() {
        return labelCell;
    }
    public void setLabelCell(BoardCell cell){
        labelCell = cell;
    }
    public BoardCell getCenterCell() {
        return centerCell;
    }
    public void setCenterCell(BoardCell cell){ centerCell = cell; }
}
