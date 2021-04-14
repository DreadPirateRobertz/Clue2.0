package clueGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

public abstract class Player {

    private String name;
    private Color color;
    protected String startLocation;
    protected ArrayList<Card> playerHand, seenList;
    protected int row, col;
    protected boolean accusationFlag = false, stayinRoomFlag = false;
    protected Suggestion suggestion;

    public Player(String name, Color color, String startLocation) {
        this.name = name;
        this.color = color;
        this.startLocation = startLocation;
        playerHand = new ArrayList<>();
        seenList = new ArrayList<>();
    }
    public abstract Suggestion createSuggestion(Room room, ArrayList<Card> allCards);
    public abstract BoardCell selectTargets(ArrayList<BoardCell> targets);

    public void setAccusationFlag(){
        accusationFlag = true;
    }

    public Card disproveSuggestion(Suggestion s){
        ArrayList<Card> disproveList = new ArrayList<>();
        for (Card card : playerHand){
            if (card.equals(s.getPersonCard())) {
                disproveList.add(card);
            }
           else if (card.equals(s.getRoomCard())) {
                disproveList.add(card);
            }
           else if (card.equals(s.getWeaponCard())) {
                disproveList.add(card);
            }
        }
        if (disproveList.size() == 1){
            return disproveList.get(0);
        }
        else if (disproveList.size() > 1){
            Random randomize = new Random();
            Collections.shuffle(disproveList);
            return disproveList.get(randomize.nextInt(disproveList.size())); //Random Card is selected from list
        }
        return null;
    }

    public void draw(Graphics2D g, int size, int xOffset, int yOffset, Map<Room, ArrayList<Player>> roomOccupancyMap) {
        Board board = Board.getInstance();
        g.setColor(this.getColor());
        board.getCell(this).setOccupied(true); //TODO: FIGURE OUT WHERE TO SET FALSE for HUMANS, Computer is being taken care of in GameControlPanel
        if(board.getCell(this).isRoomCenter()) {
            Room room = board.getRoom(board.getCell(this));
            if (board.getCell(this).isRoomCenter() && roomOccupancyMap.get(room).size() > 1) {
                int addedOffset = size / 2;
                for (Player player : roomOccupancyMap.get(room)) {
                    g.setColor(player.getColor());
                    g.fillRoundRect(player.getCol() * size + xOffset + addedOffset, player.getRow() * size + yOffset, size, size, size, size);
                    g.setColor(Color.WHITE);
                    g.drawRoundRect(player.getCol() * size + xOffset + addedOffset, player.getRow() * size + yOffset, size - 1, size - 1, size - 1, size - 1);
                    addedOffset +=10;
                }
            }
            else{
                g.fillRoundRect(this.getCol() * size + xOffset, this.getRow() * size + yOffset, size, size, size, size);
                g.setColor(Color.WHITE);
                g.drawRoundRect(this.getCol() * size + xOffset, this.getRow() * size + yOffset, size - 1, size - 1, size - 1, size - 1);
            }
        }
        else {
            g.fillRoundRect(this.getCol() * size + xOffset, this.getRow() * size + yOffset, size, size, size, size);
            g.setColor(Color.WHITE);
            g.drawRoundRect(this.getCol() * size + xOffset, this.getRow() * size + yOffset, size - 1, size - 1, size - 1, size - 1);
        }
    }

    public void setStayInRoomFlag(boolean stayinRoomFlag) {
        this.stayinRoomFlag = stayinRoomFlag;
    }

    public boolean isStayInRoomFlag() {
        return stayinRoomFlag;
    }

    public boolean doAccusation(Suggestion s) {
        return Board.getInstance().checkAccusation(s);
    }

    //Setters
    public void setSuggestion(Suggestion s){
        suggestion = s;
    }
    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }
    public void updateHand(Card card){ playerHand.add(card); }
    public void updateSeenList(Card card){ seenList.add(card); }
    public void setPlayerHand(ArrayList<Card> cards) { this.playerHand = cards; }
    public void setPlayer_RowCol() { //Sets row/col logic for players since I put startLocation in as a String to ClueSetup
        row = Board.getInstance().getRoom(startLocation.charAt(0)).getCenterCell().getRow();
        col = Board.getInstance().getRoom(startLocation.charAt(0)).getCenterCell().getCol();
    }
    //Isers
    public boolean isAccusationFlag() { return accusationFlag; }
    //Getters
    public Suggestion getSuggestion() { return suggestion; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public ArrayList<Card> getSeenList() { return seenList; }
    public ArrayList<Card> getPlayerHand() { return playerHand; }
    public String getName() { return name; }
    public Color getColor() { return color; }
    public String getStartLocation() { return startLocation; }

}
