package clueGame;

import java.awt.*;
import java.util.ArrayList;

public class Room {
    private String name;
    private char ID;
    private BoardCell labelCell, centerCell, secretCell;
    private boolean walkWay, unUsed;

    public Room(String name, char ID) {
        this.name = name;
        this.ID = ID;
    }
    public Room(){}

    //Getters
    public BoardCell getCenterCell() {
        return centerCell;
    }
    public char getIdentifier() { return ID; }
    public BoardCell getSecretCell() { return secretCell; }
    public String getName() { return name; }
    public BoardCell getLabelCell() { return labelCell; }
    //Is'ers
    public boolean isWalkWay() {return walkWay;}
    public boolean isUnUsed() { return unUsed;}
    //Setters
    public void setID(char ID) {
        this.ID = ID;
    }
    public void setSecretCell(BoardCell secretCell) { this.secretCell = secretCell; }
    public void setName(String name) {
        this.name = name;
    }
    public void setLabelCell(BoardCell cell){
        labelCell = cell;
    }
    public void setCenterCell(BoardCell cell){ centerCell = cell; }
    public void setWalkway(){walkWay = true;}
    public void setUnused() {
        unUsed = true;
    }

}
