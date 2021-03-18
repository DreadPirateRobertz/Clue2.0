package clueGame;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;

public class Board {
    private BoardCell[][] grid;
    private Map<Character, Room> roomMap;
    private Map<Player, ArrayList<Card>> playerMap;
    private Set<BoardCell> targets, visited;
    private ArrayList<Card> roomCards, playerCards, weaponCards, allCards;
    private static int num_rows, num_cols;
    private String setupConfigFile, layoutConfigFile;


    private static Board theInstance = new Board();
    //Private constructor to ensure only one -> Singleton Pattern
    private Board() {
        super();
    }
    public static Board getInstance() {
        return theInstance;
    }


    public void initialize() {//Set-up board
        visited = new HashSet<>();
        targets = new HashSet<>();
        roomMap = new HashMap<>();
        playerMap = new HashMap<>();
        roomCards = new ArrayList<>();
        playerCards = new ArrayList<>();
        weaponCards = new ArrayList<>();
        allCards = new ArrayList<>();

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
        String[] array;

        while (inFile.hasNext())//Go until EOF
        {
            String data = inFile.nextLine();
            array = data.split(",", 2);
            String cardCheck = array[0].trim();//Exception Testing Variable

            if (!data.contains("//")) {//Edit out pesky comments :)

                switch (cardCheck) {
                    case "Room", "Space" -> {
                        array = data.split(",", 3);
                        setupRoom(array);//Configures each Room with name and identifier & then sets the room
                    }
                    case "Person" -> {
                        array = data.split(",", 5);
                        setupPlayer(array);
                    }
                    case "Weapon" -> {
                        setupWeapon(array);
                    }
                    default -> throw new BadConfigFormatException(cardCheck);
                }
            }
        }
        inFile.close();
        if(!playerCards.isEmpty() && !weaponCards.isEmpty()) {
            deal();
            getTheAnswer();
        }
    }

    private void setupRoom(String[] array)  {
        Room room = new Room();//Create a room
        String cardCheck = array[0].trim();
        String name = array[1].trim();
        String data = array[2].trim();
        char roomID = data.charAt(0);
        Card card = new Card(CardType.ROOM, name);

        room.setName(name);
        room.setID(roomID);//I liked this better as setID than setIdentifier, I believe an exception to the naming rule is acceptable
        if(cardCheck.equals("Space") && !name.equals("Unused")) {//if space equals anything but Unused...then setWalkway...No more hardcoding
            room.setWalkway();//Also this would cover a hallway, breezeway, freeway... or whatever someone desired to implement for usable Space
        }
        setRoom(room);//Effectively adding the Room to the roomMap
        if (cardCheck.equals("Room")) {
            roomCards.add(card);
            allCards.add(card);
        }
    }

    private void setupPlayer(String[] array) {
        String name = array[1].trim();
        String dataColor = array[2].trim();
        String playerType = array[3].trim();
        String startLocation = array[4].trim();
        Card card = new Card(CardType.PERSON, name);
        Color color;

        switch(dataColor){
            case "Orange" ->{
                color = Color.orange;
            }
            case "Magenta" ->{
                color = Color.magenta;
            }
            case "Blue" ->{
                color = Color.blue;
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
            default -> throw new IllegalStateException("Unexpected value: " + dataColor);
        }

        if (playerType.equals("Human")){
            Human player = new Human(name, color, startLocation);
            playerMap.put(player, null);
        }
        else{
            Computer player = new Computer(name, color, startLocation);
            playerMap.put(player, null);
        }
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
        Random randy = new Random();
        shuffle(roomCards);//Performs the shuffle function x100
        shuffle(playerCards);
        shuffle(weaponCards);

        Solution.setPerson(playerCards.get(randy.nextInt(playerCards.size())));
        Solution.setRoom(roomCards.get(randy.nextInt(roomCards.size())));
        Solution.setWeapon(weaponCards.get(randy.nextInt(weaponCards.size())));

        ArrayList<Card> workingDeck = new ArrayList<>(allCards);
        workingDeck.remove(Solution.getPerson());
        workingDeck.remove(Solution.getRoom());
        workingDeck.remove(Solution.getWeapon());
        shuffle(workingDeck);

        int cardAllotment = Math.floorDiv(workingDeck.size(), playerMap.keySet().size());
        ArrayList<Player> keys = new ArrayList<>(playerMap.keySet());

        for (Player K : playerMap.keySet()) {//I kept this at K, indicating key because I make a random player key directly below
            Player key = keys.get(randy.nextInt(keys.size()));//This allows me total random access to my playerMap which is already a HashMap
            ArrayList<Card> cardLoader = new ArrayList<>();
            int count = cardAllotment;

            if (randy.nextBoolean()) {//50/50 shot of iterating backwards or forwards
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
            playerMap.put(key, cardLoader);
            shuffle(workingDeck);
            keys.remove(key);
            for (Card pick : cardLoader) {
                workingDeck.remove(pick);
            }
        }

        keys = new ArrayList<>(playerMap.keySet());//Resetting keys which are all the Players
        while(!workingDeck.isEmpty()) {//Deals any residual cards after general allotment is made

            for (Card card : workingDeck) {//There was mention of adding more cards so figured this may be needed
                Player key = keys.get(randy.nextInt(keys.size()));
                ArrayList<Card> tempList = new ArrayList<>(playerMap.get(key));
                tempList.add(card);//Intention was copying all the values and then adding this value to this list and pushing it back to the playerMap

                playerMap.put(key, tempList);
                workingDeck.remove(card);
                keys.remove(key);
                shuffle(workingDeck);
                break; //Shuffling....so this break resets the iter on this for loop and the while keeps it going
            }
        }
        for(Player playerKey : playerMap.keySet()){//Now each player has an ArrayList of their cards to directly access for updating the hand
            playerKey.setMyCards(playerMap.get(playerKey));
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
        int gridSize = setRowsCols(csvData);
        if (num_cols * num_rows == gridSize) {
            buildGameGrid(csvData);//Builds the game grid from ClueLayout.csv file
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
                if(isRoom(symbol)) {//If it's gotten to here and fails then this means it's not a Room or any approved symbol
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

    private void findAdjacencies() {
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
            if (visited.contains(adjCell) || (adjCell.getOccupied() && !adjCell.isRoomCenter())) {
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

    private void shuffle(ArrayList<Card> cards) {
        int i = 0;
        while(i < 100) {
            Collections.shuffle(cards);
            i++;
        }
    }

    //Getters
    public Player getPlayer(String name){
        for (Player player : playerMap.keySet()){
            if (player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }
    public Set<BoardCell> getAdjList(int row, int col) { return getCell(row, col).getAdjList(); }
    public Room getRoom(BoardCell cell) { return roomMap.get(cell.getInitial()); }
    public Room getRoom(char key) { return roomMap.get(key); }
    public int getNumRows() { return num_rows; }
    public int getNumColumns() { return num_cols; }
    public BoardCell getCell(int row, int col) { return grid[row][col]; }
    public Set<BoardCell> getTargets() { return targets; }
    private void getTheAnswer() { Solution.theAnswer(); }
    //Is'ers
    public boolean isWalkway(BoardCell cell){return getRoom(cell).isWalkWay(); }
    public boolean isRoom(char symbol) { return roomMap.containsKey(symbol); }
    //Setters
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
}

