package clueGame;

import java.util.ArrayList;

public class Room {
    private String name;
    private char identifier;
    private BoardCell labelCell, centerCell, secretCell;
    //Getters
    public BoardCell getCenterCell() {
        return centerCell;
    }
    public char getIdentifier() { return identifier; }
    public BoardCell getSecretCell() { return secretCell; }
    public String getName() { return name; }
    public BoardCell getLabelCell() { return labelCell; }
    //Setters
    public void setIdentifier(char identifier) {
        this.identifier = identifier;
    }
    public void setSecretCell(BoardCell secretCell) { this.secretCell = secretCell; }
    public void setName(String name) {
        this.name = name;
    }
    public void setLabelCell(BoardCell cell){
        labelCell = cell;
    }
    public void setCenterCell(BoardCell cell){ centerCell = cell; }
}
