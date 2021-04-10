package clueGame;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class BoardCell  {
    private int row, col;
    private char initial, secretPassage;
    private DoorDirection doorDirection;
    private boolean roomCenter, roomLabel, doorway, occupied, unUsed, walkWay, room, target;
    private Set<BoardCell> adjList;


    public BoardCell(int row, int col) {
        this.row = row;
        this.col = col;
        secretPassage = '0'; //Its null setting distracts me in the debugger so I set it to '0'
        adjList = new HashSet<>(); //Initialize adjacency list
        doorDirection = DoorDirection.NONE;
    }
    public void drawCell(Graphics2D g, int size, int xOffset, int yOffset){
        int x = (col * size) + xOffset;
        int y = (row * size) + yOffset;
        Board board = Board.getInstance();

        if (this.unUsed){
            g.setColor(Color.BLACK);
            g.fillRect(x,y,size, size);
        }
        else if (this.walkWay){
            g.setColor(new Color(220,220,220));
            g.fillRect(x,y,size,size);
            g.setColor(Color.BLACK);
            g.drawRect(x,y,size-1,size-1);
        }
        else if (this.room){
            g.setColor(new Color(25,25,112));
            g.fillRect(x,y,size,size);
        }
       if (target && !room){
            g.setColor(new Color(0,255,127));
            g.fillRect(x, y, size-2, size-2);
        }
       if (room && board.getRoom(this).getCenterCell().isTarget()){
            board.getTargets().add(this); //Now the whole room can be clicked on and will be a target --> Issue when Player is not exactly in center messes up targeting
            g.setColor(new Color(0,255,127));
            g.fillRect(x,y,size,size);
        }
    }
    public boolean containsClick(int mouseX, int mouseY, int xOffset, int yOffset, int size){
        int x = (col * size) + xOffset;
        int y = (row * size) + yOffset;
        Rectangle rect = new Rectangle(x,y,size,size);
        if (rect.contains(new Point(mouseX, mouseY))){
            return true;
        }
        return false;
    }


    public void drawRoomName(Graphics2D g, int size, int xOffset, int yOffset) {
        int x = (col * size) + xOffset;
        int y = (row * size) + yOffset;
        String roomName = Board.getInstance().getRoom(this).getName();
        g.setColor(Color.WHITE);
        g.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 12));
        g.drawString(roomName.toUpperCase(Locale.ROOT), x, y);
    }
    public void drawDoorWay(Graphics2D g, int size, int xOffset, int yOffset){
        int x = (col * size) + xOffset;
        int y = (row * size) + yOffset;
        DoorDirection whichWay = this.getDoorDirection();
        g.setColor(Color.GREEN);
        switch (whichWay){
            case DOWN -> {
                g.fillRect(x, y+size, size, size/6);
                g.setColor(Color.BLACK);
                g.drawRect(x, y+size, size - 1, size/6 - 1);
                g.setColor(Color.GREEN);
                g.fillRect(x+7, y+size+5, size/2, size/6);
                g.setColor(Color.BLACK);
                g.drawRect(x+7, y+size+5, size/2 - 1, size/6 - 1);
                g.setColor(Color.GREEN);
                g.fillRect(x+11, y+size+10, size/5, size/6);
                g.setColor(Color.BLACK);
                g.drawRect(x+11, y+size+10, size/5 - 1, size/6 - 1);
            }
            case UP -> {
                g.fillRect(x, y-size/6, size, size/6);
                g.setColor(Color.BLACK);
                g.drawRect(x, y-size/6, size - 1, size/6 - 1);
                g.setColor(Color.GREEN);
                g.fillRect(x+7, y-size/6-5, size/2, size/6);
                g.setColor(Color.BLACK);
                g.drawRect(x+7, y-size/6-5, size/2 - 1, size/6 - 1);
                g.setColor(Color.GREEN);
                g.fillRect(x+11, y-size/6-10, size/5, size/6);
                g.setColor(Color.BLACK);
                g.drawRect(x+11, y-size/6-10, size/5 - 1, size/6 - 1);
            }
            case RIGHT -> {
                g.fillRect(x+size, y, size/6, size);
                g.setColor(Color.BLACK);
                g.drawRect(x+size, y, size/6 - 1, size - 1);
                g.setColor(Color.GREEN);
                g.fillRect(x+size+5, y+6, size/6, size/2);
                g.setColor(Color.BLACK);
                g.drawRect(x+size+5, y+6, size/6 - 1, size/2 - 1);
                g.setColor(Color.GREEN);
                g.fillRect(x+size+10, y+10, size/6, size/4);
                g.setColor(Color.BLACK);
                g.drawRect(x+size+10, y+10, size/6 - 1, size/4 - 1);
            }
            case LEFT -> {
                g.fillRect(x-5, y, size/6, size);
                g.setColor(Color.BLACK);
                g.drawRect(x-5, y, size/6 - 1, size - 1);
                g.setColor(Color.GREEN);
                g.fillRect(x-10, y+7, size/6, size/2);
                g.setColor(Color.BLACK);
                g.drawRect(x-10, y+7, size/6 - 1, size/2 - 1);
                g.setColor(Color.GREEN);
                g.fillRect(x-15, y+11, size/6, size/4);
                g.setColor(Color.BLACK);
                g.drawRect(x-15, y+11, size/6 - 1, size/4 - 1);
            }
                }
        }
    public void drawSecretPassage(Graphics2D g, int size, int xOffset, int yOffset) {
        int x = (col * size) + xOffset;
        int y = (row * size) + yOffset;
        char roomID = Board.getInstance().getRoom(this).getIdentifier();
        if (roomID == 'B' || roomID == 'O') {
            g.setColor(Color.BLACK);
            g.fillRoundRect(x, y, size / 2 +5, size-5 , size, size);
            g.setColor(Color.WHITE);
            g.drawRoundRect(x, y, size / 2 +4, size-6 , size-1, size-1);
            g.setColor(Color.MAGENTA);
            g.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 12));
            if (roomID == 'B'){
                g.drawString("O", x+5, y+17);
            }
            else{
                g.drawString("B", x+6, y+17);
            }
        }
        else {
            g.setColor(Color.BLACK);
            g.fillRoundRect(x, y, size / 2 +5, size-5 , size, size);
            g.setColor(Color.WHITE);
            g.drawRoundRect(x, y, size / 2 +4, size-4 , size-1, size-1);
            g.setColor(Color.MAGENTA);
            g.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 12));
            if (roomID == 'M'){
                g.drawString("L", x+7, y+17);
            }
            else{
                g.drawString("M", x+4, y+18);
            }
        }

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




    //Is'ers
    public boolean isRoom(){ return room;}
    public boolean isOccupied() { return occupied; }
    public boolean isTarget() { return target;}
    public boolean isDoorway() { return doorway; }
    public boolean isLabel() { return roomLabel; }
    public boolean isRoomCenter() { return roomCenter; }
    public boolean isRoomLabel() { return roomLabel; }
    //Setters
    public void addAdjacency(BoardCell cell) { adjList.add(cell); }
    public void setInitial(char initial) { this.initial = initial; }
    public void setDoorway() { doorway = true; }
    public void setDoorDirection(DoorDirection doorDirection) { this.doorDirection = doorDirection; }
    public void setLabel() { roomLabel = true; }
    public void setRoomCenter() { roomCenter = true; }
    public void setSecretPassage(char secretPassage) { this.secretPassage = secretPassage; }
    public void setOccupied(boolean b) { occupied = b; }

    public void setTarget(boolean b) {
        target = b;
    }
    public void setRoom(){
        room = true;
    }
    public void setUnUsed() {
        unUsed = true;
    }

    public void setWalkWay() {
        walkWay= true;
    }



}
