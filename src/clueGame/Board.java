package clueGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board extends JPanel {
    private BoardCell[][] grid;
    private Map<Character, Room> roomMap;
    private Map<Player, ArrayList<Card>> playerMap;
    private Set<BoardCell> targets, visited;
    private ArrayList<Card> roomCards;
    private ArrayList<Card> playerCards;
    private ArrayList<Card> weaponCards;
    private ArrayList<Card> allCards;
    private int gridSize;
    private static int num_rows, num_cols;
    private String setupConfigFile, layoutConfigFile;
    private static Board theInstance = new Board();
    //Private constructor to ensure only one -> Singleton Pattern
    private Board() { super(); }
    public static Board getInstance() {
        return theInstance;
    }
    private static ArrayList<Card> theAnswer;
    private static ArrayList<Player> players;
    private int index = 0;
    private boolean playerFlag = false;
    private Color accuserColor;
    private String accuserPlayer;



    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public void initialize() {//Set-up board
        theAnswer = new ArrayList<>();
        visited = new HashSet<>();
        targets = new HashSet<>();
        roomMap = new HashMap<>();
        playerMap = new LinkedHashMap<>(); //Did this to preserve insertion order of players
        roomCards = new ArrayList<>();
        playerCards = new ArrayList<>();
        weaponCards = new ArrayList<>();
        allCards = new ArrayList<>();
        addMouseListener(new whichTargetListener());

        try {
            loadSetupConfig();
        } catch (FileNotFoundException | BadConfigFormatException e) {
            System.out.println(e.getMessage());
        }
        try {
            loadLayoutConfig();
        } catch (FileNotFoundException | BadConfigFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
        Scanner inFile = setInFile(setupConfigFile);
        String[] split1, split2, split3;

        while (inFile.hasNext())//Go until EOF
        {
            String data = inFile.nextLine();
            split1 = data.split(",", 3);
            split2 = data.split(",", 5);
            split3 = data.split(",", 2);
            String cardCheck = split3[0].trim();//Exception Testing Variable

            if (!data.contains("//")) {//Edit out pesky comments :)
                switch (cardCheck) {
                    case "Room", "Space" -> {
                        setupRoom(split1);//Configures each Room with name and identifier & then sets the room
                    }
                    case "Person" -> {
                        setupPlayer(split2);
                    }
                    case "Weapon" -> {
                        setupWeapon(split3);
                    }
                    default -> throw new BadConfigFormatException(cardCheck);
                }
            }
        }
        inFile.close();
        if (!playerCards.isEmpty() && !weaponCards.isEmpty()) {
            deal();
        }
    }

    private void setupRoom(String[] array)  {
        String cardCheck = array[0].trim();
        String name = array[1].trim();
        String data = array[2].trim();
        char roomID = data.charAt(0);
        Card card = new Card(CardType.ROOM, name);
        Room room = new Room(name, roomID);

        if (cardCheck.equals("Space") && !name.equals("Unused")) {//if space equals anything but Unused...then setWalkway...No more hardcoding
            room.setWalkway();//Also this would cover a hallway, breezeway, freeway... or whatever someone desired to implement for usable Space
        }
        else if (cardCheck.equals("Space") && name.equals("Unused")){
            room.setUnused();
        }
        else{
            room.setRoom();
        }
        setRoom(room);//Effectively adding the Room to the roomMap
        if (cardCheck.equals("Room")) {
            roomCards.add(card);
            allCards.add(card);
        }
    }

    private void setupPlayer(String[] array) throws FileNotFoundException, BadConfigFormatException {
        String name = array[1].trim();
        String dataColor = array[2].trim();
        String playerType = array[3].trim();
        String startLoc = array[4].trim();
        char roomID = startLoc.charAt(0);
        Card card = new Card(CardType.PERSON, name);
        Color color;
        Player player;

        switch(dataColor){
            case "Orange" ->{
                color = Color.orange;
            }
            case "Magenta" ->{
                color = Color.magenta;
            }
            case "Pink" ->{
                color = Color.pink;
            }
            case "Cyan" ->{
                color = Color.cyan;
            }
            case "Green" ->{
                color = Color.green;
            }
            case "Red" ->{
                color = Color.red;
            }
            default -> throw new IllegalStateException("Unexpected Color: " + dataColor);
        }
        if (!getRoom(roomID).getName().equals(startLoc)){
            throw new BadConfigFormatException(startLoc);
        }
        switch (playerType){
            case "Human" -> {
                player = new Human(name, color, startLoc); //Polymorphism
            }
            case "Computer" -> {
                player = new Computer(name, color, startLoc);

            }
            default -> throw new IllegalStateException("Unexpected Player Type: " + playerType);
        }
        playerMap.put(player, null);
        playerCards.add(card);
        allCards.add(card);
    }

    private void setupWeapon(String[] array) {
        String name = array[1].trim();
        Card card = new Card(CardType.WEAPON, name);
            weaponCards.add(card);
            allCards.add(card);
    }

    public void deal() {
        Random randomize = new Random();
        shuffle(roomCards);//Performs the Collections shuffle function x100
        shuffle(playerCards);
        shuffle(weaponCards);

        theAnswer(randomize);

        ArrayList<Card> workingDeck = new ArrayList<>(allCards);//Deep copy
        workingDeck.remove(theAnswer.get(0));
        workingDeck.remove(theAnswer.get(1));
        workingDeck.remove(theAnswer.get(2));
        shuffle(workingDeck);

        int cardAllotment = Math.floorDiv(workingDeck.size(), playerMap.keySet().size());
        ArrayList<Player> keys = new ArrayList<>(playerMap.keySet());

        for (Player k : playerMap.keySet()) {//I kept this at k, indicating key because I make a random player key directly below and I don't use k for anything below
            Player player = keys.get(randomize.nextInt(keys.size()));//This allows me total random access to my playerMap
            ArrayList<Card> cardLoader = new ArrayList<>();
            int count = cardAllotment;

            if (randomize.nextBoolean()) {//50/50 shot of iterating backwards or forwards (else below)
                for (int i = workingDeck.size() - 1; i > -1; i--) {
                    if (count > 0) {
                        cardLoader.add(workingDeck.get(i));
                        count--;
                    }
                    else {
                        break;
                    }}}
            else {
                for (Card card : workingDeck) {
                    if (count > 0) {
                        cardLoader.add(card);
                        count--;
                    }
                    else {
                        break;
                    }}}
            player.setPlayerHand(cardLoader);//Sets all the cards the Player initially holds (their hand)
            playerMap.put(player, cardLoader);//Also I found very useful having a map of the Players & Cards for Board itself
            shuffle(workingDeck);
            keys.remove(player);
            for (Card pick : cardLoader) {
                workingDeck.remove(pick);
            }
        }

        keys = new ArrayList<>(playerMap.keySet());//Resetting keys which are all the Players
        players = new ArrayList<>(keys);
        int index = 0;
        for(Player player: players){ //Putting the Human first as I make this list for cycling thru the players to play Clue
            if (player.getClass().equals(Human.class)){
                if(index == 0){
                    break;
                }
                else{
                    Player playa = players.get(0);
                    players.set(0, player);
                    players.set(index, playa);
                    break;
                }
            }
            index++;
        }
        while(!workingDeck.isEmpty()) {//Deals any residual cards after general allotment is made
            for (Card card : workingDeck) {//There was mention of adding more cards so figured this may be needed
                Player player = keys.get(randomize.nextInt(keys.size()));
//                card.setColor(player.getColor());
                player.updateHand(card); //Reverse link will also update playerMap appropriately
                workingDeck.remove(card);
                keys.remove(player);
                shuffle(workingDeck);
                break; //Shuffling....so this break resets the iter on this for loop and the while keeps it going
            }
        }
    }

    public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
        Scanner inFile = setInFile(layoutConfigFile);
        ArrayList<String> csvData = new ArrayList<>();

        while (inFile.hasNext())//Go until EOF
        {
            String data = inFile.nextLine();//Grab it all
            String[] splitData = data.split(",");//Harness the data
            //For each index in splitData
            for (String index : splitData) {
                String cleanData = index.trim();
                char roomID = cleanData.charAt(0);

                if (isRoom(roomID)) {
                    csvData.add(cleanData);//Now the data has been refined from raw input
                }
                else {
                    throw new BadConfigFormatException(roomID);//Means an undefined letter was found in the file data
                }
            }
        }
        inFile.close();
        gridSize = setRowsCols(csvData);
        if (num_cols * num_rows == gridSize) {
            buildGameGrid(csvData);//Builds the game grid from ClueLayout.csv file
            setPlayerStartLocations();//Sets row/col data for all my players since I didn't directly insert row/col data into ClueSetup file
            findAdjacencies();//Let's build the adjacency lists for each of the cells
        } else {
            throw new BadConfigFormatException(gridSize, num_rows, num_cols);//Wrong size....you get a custom exception:)
        }
    }

    private void buildGameGrid(ArrayList<String> csvData) throws FileNotFoundException, BadConfigFormatException {
        grid = new BoardCell[num_rows][num_cols];//Initialize game grid
        int index = 0;//Using this index variable to cycle through the ArrayList of csvData

        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_cols; col++) {
                grid[row][col] = new BoardCell(row, col);
                String csv = csvData.get(index);//Grabbing the string from the ArrayList with the index
                char roomID = csv.charAt(0);//Storing the first index of csv into roomID
                grid[row][col].setInitial(roomID);//Setting cell initial which is the roomID
                if(isUnUsed(grid[row][col])){
                    grid[row][col].setUnUsed();
                }
                if(isWalkway(grid[row][col])){
                    grid[row][col].setWalkWay();
                }
                if(isRoom(grid[row][col])){
                    grid[row][col].setRoom();
                }
                if (csv.length() > 1) {//If string has special characters contained after the initial let's sort them out!
                    char symbol = csv.charAt(1);
                    classify_room_symbology(grid[row][col], symbol);
                }
                index++;
            }
        }
    }

    private void classify_room_symbology(BoardCell cell, char symbol) throws FileNotFoundException, BadConfigFormatException {
        Room room;
        switch (symbol) {
            case '#' -> {//Setting Label Cell
                cell.setLabel();
                room = getRoom(cell);
                room.setLabelCell(cell);
            }
            case '*' -> {//Setting Center Cell
                cell.setRoomCenter();
                room = getRoom(cell);
                room.setCenterCell(cell);
            }
            case '^' -> {
                cell.setDoorDirection(DoorDirection.UP);
                cell.setDoorway();
            }
            case '<' -> {
                cell.setDoorDirection(DoorDirection.LEFT);
                cell.setDoorway();
            }
            case '>' -> {
                cell.setDoorDirection(DoorDirection.RIGHT);
                cell.setDoorway();
            }
            case 'v' -> {
                cell.setDoorDirection(DoorDirection.DOWN);
                cell.setDoorway();
            }
            default -> {//The last item that will fall to default should be Secret Cells
                if (isRoom(symbol)) {//If it's gotten to here and fails then this means it's not a Room or any approved symbol
                    cell.setSecretPassage(symbol);
                    room = getRoom(cell);//Assigning Secret cell/Room logic
                    room.setSecretCell(cell);
                }
                else {//We never check the second letter in earlier test for errant roomID's, so my thinking was to throw this exception
                    throw new BadConfigFormatException(symbol);//just in case there's an errant SECOND character in the layout file
                }
            }
        }
    }

    public void findAdjacencies() {
        for (int row = 0; row < num_rows; row++)//Sets all the adjacency lists for each cell
            for (int col = 0; col < num_cols; col++)
                calcAdjacencies(grid[row][col], row, col);
    }

    public void calcAdjacencies(BoardCell cell, int row, int col) {
        DoorDirection theWay = cell.getDoorDirection();
        BoardCell centerCell, doorWay;
        Room theRoom;

        if (isWalkway(cell)) {
            addWalkways(cell, row, col);

            if (cell.isDoorway()) {
                doorWay = cell;
                switch (theWay) {//This is theWay... :)
                    case RIGHT -> {
                        theRoom = getRoom(grid[row][col + 1]);//doorWay------>>>theRoom
                        centerCell = theRoom.getCenterCell();
                        doorWay.addAdjacency(centerCell);
                        centerCell.addAdjacency(doorWay);
                    }              //Note: Originally was assigning doorWays to the Room with a separate method that req'd an additional cycle of the grid
                    case LEFT -> {//and was taking care of adding doorWays in the else-if below but it was functionality that was not needed and this sol'n reduced code and time complexity
                        theRoom = getRoom(grid[row][col - 1]);
                        centerCell = theRoom.getCenterCell();
                        doorWay.addAdjacency(centerCell);
                        centerCell.addAdjacency(doorWay);
                    }
                    case UP -> {
                        theRoom = getRoom(grid[row - 1][col]);
                        centerCell = theRoom.getCenterCell();
                        doorWay.addAdjacency(centerCell);//This will add the centerCell to the doorWay's adj list
                        centerCell.addAdjacency(doorWay);//This will add the doorWay to the centerCell's adj list
                    }
                    case DOWN -> {
                        theRoom = getRoom(grid[row + 1][col]);
                        centerCell = theRoom.getCenterCell();
                        doorWay.addAdjacency(centerCell);
                        centerCell.addAdjacency(doorWay);
                    }
                }
            }
        }
        else if (cell.isRoomCenter()) {//Explicit Room center
            BoardCell secretCell = getRoom(cell).getSecretCell();

            if (secretCell != null) {//Is there a secret cell?
                addSecret(cell, secretCell);//This method leads you to center (*) cell of that room
            }
        }
    }

    private void addSecret(BoardCell cell, BoardCell secretCell) {
        char secretKey = secretCell.getSecretPassage();//Obtain the secret key and burrow through the secret passage
        Room secretRoom = getRoom(secretKey);//Put in the secretKey to obtain the secretRoom
        BoardCell secretRoomCenter = secretRoom.getCenterCell();//To unveil the Center(*)

        cell.addAdjacency(secretRoomCenter);
    }

    private void addWalkways(BoardCell cell, int row, int col) {//Standard adjacency rules for adding cells
        if (col < num_cols - 1) {
            BoardCell cell_Right = getCell(row, col + 1);
            if (isWalkway(cell_Right)) {
                cell.addAdjacency(cell_Right);//Refactored variables for readability
            }
        }
        if (col > 0) {
            BoardCell cell_Left = getCell(row, col - 1);
            if (isWalkway(cell_Left)) {
                cell.addAdjacency(cell_Left);
            }
        }
        if (row > 0) {
            BoardCell cell_Up = getCell(row - 1, col);
            if (isWalkway(cell_Up)) {
                cell.addAdjacency(cell_Up);
            }
        }
        if (row < num_rows - 1) {
            BoardCell cell_Down = getCell(row + 1, col);
            if (isWalkway(cell_Down)) {
                cell.addAdjacency(cell_Down);
            }
        }
    }

    public void calcTargets(BoardCell startCell, int pathLength) {
        visited.clear();//Reset
        visited.add(startCell);
        targets.clear();//Reset
        findAllTargets(startCell, pathLength);
    }

    private void findAllTargets(BoardCell thisCell, int numSteps) {
        Set<BoardCell> adjacentCells = thisCell.getAdjList();

        for (BoardCell adjCell : adjacentCells) {
            if (visited.contains(adjCell) || (adjCell.isOccupied() && !adjCell.isRoomCenter())) {
                continue;//Critical to not do a break right here since you want it to keep cycling through the adjacencies
            }
            visited.add(adjCell);//Add to visited list
            if (numSteps == 1 || adjCell.isRoomCenter()) {//Base Case
                targets.add(adjCell);//BAM
                if (adjCell.isRoomCenter()) {//If you reach this room center cell...STOP advancing
                    continue;
                }
            }
            else {
                findAllTargets(adjCell, numSteps - 1); //Recurse
            }
            visited.remove(adjCell);//Remove from visited list
        }
    }

    public Card handleSuggestion(Player accuser, Suggestion suggestion) { //No real great word for Suggester...
        for (Player player : playerMap.keySet()){
            if (player.equals(accuser)){
                continue;
            }
            Card card = player.disproveSuggestion(suggestion);
            if (card != null){
                accuserColor = player.getColor();
                accuserPlayer = player.getName();
                accuser.updateSeenList(card);
                if(accuser.getClass().equals(Human.class)) {//I want this to only start displaying colors in guess fields when Human already knows color or who it's from
                    card.setColor(player.getColor());
                }
                return card;
            }
        }
        accuser.setAccusationFlag();
        return null;
    }

    public boolean checkAccusation(Suggestion suggestion) {
        return suggestion.getPersonCard().equals(getTheAnswer_Person()) && suggestion.getRoomCard().equals(getTheAnswer_Room()) && suggestion.getWeaponCard().equals(getTheAnswer_Weapon());
    }

    private void theAnswer(Random randomize) {
        setTheAnswer_Person(playerCards.get(randomize.nextInt(playerCards.size())));
        setTheAnswer_Room(roomCards.get(randomize.nextInt(roomCards.size())));
        setTheAnswer_Weapon(weaponCards.get(randomize.nextInt(weaponCards.size())));
    }

    private void shuffle(ArrayList<Card> cards) {
        int i = 0;
        while (i < 100) {
            Collections.shuffle(cards);
            i++;
        }
    }
    private Color setRandomColor(){
        Random randomize = new Random();
        int red = randomize.nextInt(256);
        int green = randomize.nextInt(256);
        int blue = randomize.nextInt(256);
        return new Color(red,green, blue);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Random randomize = new Random();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        int size;
        if (getHeight() < getWidth()) {
            size = getHeight() / num_cols;
        } else {
            size = getWidth() / num_rows;
        }
        ArrayList<Point> pointy = new ArrayList<>();
        for (int i = 0; i < size * 5; i++) {
            Point p = new Point();
            p.setLocation(randomize.nextInt(size), randomize.nextInt(size));
            pointy.add(p);
        }
        for (Point point : pointy) { //Cool Random Stars for my SpaceShip Theme
            if (randomize.nextInt(5) == 1) {
                g2.setColor(setRandomColor());
            } else {
                g2.setColor(Color.WHITE); //Resizing the window has cool effect of traveling thru space :)
            }
            g2.drawRect((int) point.getX() * 80, (int) point.getY() * 50, 1, 1);
        }
        int xOffset = (getWidth() / 2) - ((num_cols / 2) * size);
        int yOffset = (getHeight() / 2) - ((num_rows / 2) * size);

        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_cols; col++) {
                grid[row][col].drawCell((Graphics2D) g, size, xOffset, yOffset);
            }
        }

        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_cols; col++) {
                if (grid[row][col].isDoorway()) {
                    grid[row][col].drawDoorWay((Graphics2D) g, size, xOffset, yOffset);
                }
                if (grid[row][col].isRoomLabel()) {
                    grid[row][col].drawRoomName((Graphics2D) g, size, xOffset, yOffset);
                }
                if (grid[row][col].getSecretPassage() != '0') {
                    grid[row][col].drawSecretPassage((Graphics2D) g, size, xOffset, yOffset);
                }
            }
        }
        Map<Room, ArrayList<Player>> roomOccupancyMap = new HashMap<>();
        for (Player player : players) {
            if(getCell(player).isRoom()){ //This is setting up so you can click anywhere in a room...Made the whole Room a target and then Player will draw itself in the center
                player.setRow(this.getRoom(this.getCell(player)).getCenterCell().getRow());
                player.setCol(this.getRoom(this.getCell(player)).getCenterCell().getCol());
            }
            if (getCell(player).isRoomCenter()) {
                ArrayList<Player> playas = new ArrayList<>();
                Room room = getRoom(getCell(player));
                if (!roomOccupancyMap.containsKey(room)) {
                    playas.add(player);
                } else {
                    playas = roomOccupancyMap.get(room);
                    playas.add(player);
                }
                roomOccupancyMap.put(room, playas);
            }
        }
        for (Player player : players) {
            player.draw((Graphics2D) g, size, xOffset, yOffset, roomOccupancyMap);
        }
    }
    private class whichTargetListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            if(getWhoseTurn().getClass().equals(Human.class)) {
                int size;
                if (getHeight() < getWidth()) {
                    size = getHeight() / num_cols;
                } else {
                    size = getWidth() / num_rows;
                }
                int xOffset = (getWidth() / 2) - ((num_cols / 2) * size);
                int yOffset = (getHeight() / 2) - ((num_rows / 2) * size);
                BoardCell whichTarget = null;
                ArrayList<BoardCell> targets = new ArrayList<>(getTargets());
                for (int i = 0; i < targets.size(); i++) {
                    if (targets.get(i).containsClick(e.getX(), e.getY(), xOffset, yOffset, size)) {
                        whichTarget = targets.get(i);
                        break;
                    }
                }
                if (whichTarget != null) {
                    Player playa = getWhoseTurn();
                    getCell(playa).setOccupied(false);
                    playa.setRow(whichTarget.getRow());
                    playa.setCol(whichTarget.getCol());
                    for (BoardCell target : targets){
                        target.setTarget(false);
                    }
                    if(whichTarget.isRoomCenter()){
                        playa.createSuggestion(getRoom(whichTarget), allCards); //TODO: STUB
                    }
                    playerFlag = true;
                    repaint();
                }
                else {
                    Object[] theresOnlyOneAnswer = {"My Bad"};
                    JOptionPane.showOptionDialog(null, "Invalid Target Selection",
                            "This is Not the Way", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, theresOnlyOneAnswer, theresOnlyOneAnswer[0]);
                }
            }
        }
        @Override
        public void mousePressed(MouseEvent e) { }
        @Override
        public void mouseReleased(MouseEvent e) { }
        @Override
        public void mouseEntered(MouseEvent e) { }
        @Override
        public void mouseExited(MouseEvent e) { }
    }
    //Getters
    public Player getWhoseTurn(){
        if(index == 0){
            return players.get(players.size()-1);
        }
        return players.get(index-1);
    }
    public ArrayList<Card> getAllCards() {
        return allCards;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Set<BoardCell> getAdjList(int row, int col) { return getCell(row, col).getAdjList(); }
    public Room getRoom(BoardCell cell) { return roomMap.get(cell.getInitial()); }
    public Room getRoom(char key) { return roomMap.get(key); }
    public int getNumRows() { return num_rows; }
    public int getNumColumns() { return num_cols; }
    public BoardCell getCell(int row, int col) { return grid[row][col]; }
    public BoardCell getCell(Player player) { return getCell(player.getRow(), player.getCol());}
    public Set<BoardCell> getTargets() { return targets; }
    public static ArrayList<Card> getTheWholeAnswer() { return theAnswer; }
    public static Card getTheAnswer_Person(){ return theAnswer.get(0); }
    public static Card getTheAnswer_Room(){ return theAnswer.get(1); }
    public static Card getTheAnswer_Weapon(){ return theAnswer.get(2); }
    public Map<Player, ArrayList<Card>> getPlayerMap() { return playerMap; }
    public Color getAccuserColor(){return accuserColor;}
    public String getAccuserPlayer(){return accuserPlayer;}
    //Is'ers
    public boolean isPlayerFlag() { return playerFlag; }
    public boolean isWalkway(BoardCell cell){return getRoom(cell).isWalkWay(); }
    public boolean isUnUsed(BoardCell cell){return getRoom(cell).isUnUsed(); }
    public boolean isRoom(char symbol) { return roomMap.containsKey(symbol); }
    public boolean isRoom(BoardCell cell){return getRoom(cell).isRoom(); }
    //Setters
    public static void setTheAnswer_Person(Card card){ theAnswer.add(card); }
    public static void setTheAnswer_Room(Card card){ theAnswer.add(card); }
    public static void setTheAnswer_Weapon(Card card){ theAnswer.add(card); }
    public void setConfigFiles(String layout, String legend) {
        setupConfigFile = "data/" + legend;
        layoutConfigFile = "data/" + layout;
    }
    private Scanner setInFile(String file) throws FileNotFoundException {
        FileReader reader = new FileReader(file);//So we can read the file
        return new Scanner(reader);
    }
    private int setRowsCols(ArrayList<String> csvData) {
        int size = csvData.size();
        int cols = (int)Math.sqrt(size);
        int rows = (int)Math.ceil(size/(double)cols);//ceil rounds UP
        setNumCols(cols);
        setNumRows(rows);
        return size;
    }
    private void setPlayerStartLocations(){//I liked not inserting row/col directly in setup file so I have to execute this logic after grid is built
        for (Player playa : playerMap.keySet()){
            playa.setPlayer_RowCol();
        }
    }
    public Player setWhoseTurn(){
        Player playa = players.get(index);
        if(index == players.size() -1){
            index = 0;
        }
        else{
            index++;
        }
        return playa;
    }
    public void setPlayerFlag(boolean playerFlag) { this.playerFlag = playerFlag; }
    private void setRoom(Room room) { roomMap.put(room.getIdentifier(), room);}
    private void setNumRows(int rows) { num_rows = rows; }
    private void setNumCols(int cols) { num_cols = cols; }


    //Testing
    public int getPlayerCount() {
        return playerCards.size();
    }
    public int getPlayerCardTypeCount(){
        int count = 0;
        for(var card : playerCards){
            if(card.getCardType() == CardType.PERSON){
                count++;
            }
        }
        return count;
    }
    public int getAllCardsSize(){
        return allCards.size();
    }
    public int getTotalCardsDealtToPlayers(){
        int count = 0;
        for (ArrayList<Card> list : playerMap.values()) {
            for (Card card : list) {
                count++;
            }
        }
        return count;
    }
    public int getRoomCardTypeCount(){
        int count = 0;
        for(Card card : roomCards){
            if(card.getCardType() == CardType.ROOM){
                count++;
            }
        }
        return count;
    }
    public int getWeaponCardTypeCount(){
        int count = 0;
        for(Card card : weaponCards){
            if(card.getCardType() == CardType.WEAPON){
                count++;
            }
        }
        return count;
    }

    public boolean isValidPlayer(String name){
        for (Card player : playerCards){
            if (player.getCardName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Card> getPlayerMapValues() {
        ArrayList<Card> temp = new ArrayList<>();
        for (ArrayList<Card> list : playerMap.values()) {
            temp.addAll(list);
        }
        return temp;
    }

    public Player getPlayer(String name){
        for (Player player : playerMap.keySet()){
            if (player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }
}

