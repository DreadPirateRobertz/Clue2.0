package clueGame;

import java.io.*;
import java.util.*;

public class Board {
    private Set<BoardCell> targets;
    private Set<BoardCell> visited;
    private BoardCell[][] grid;
    private Map<Character, Room> roomMap;
    private String layoutConfigFile;
    private String setupConfigFile;
    private static int NUM_ROWS;
    private static int NUM_COLS;

    private static Board theInstance = new Board();
    //Private constructor to ensure only one -> Singleton Pattern
    private Board() {
        super();
    }
    public static Board getInstance(){
        return theInstance;
    }


    public void initialize()  {

        visited = new HashSet<BoardCell>();
        targets = new HashSet<BoardCell>();
        //Set-up board

        try {
            loadSetupConfig();
        }
        catch (FileNotFoundException | BadConfigFormatException e){
            System.out.println(e.getMessage());
        }
        try {
            loadLayoutConfig();
        }
        catch (FileNotFoundException | BadConfigFormatException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setConfigFiles(String layout, String legend) {
        setupConfigFile = legend;
        layoutConfigFile = layout;
    }

    public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
        roomMap = new HashMap<>();
        Scanner inFile = setInFile(setupConfigFile);
        while (inFile.hasNext())//Go until EOF
        {
            String data = inFile.nextLine();
            if (data.contains("//")) {  //Edit out pesky comments :)
                continue;
            }
            Room room = setupRoom(data); //Configures room with name and identifier
            roomMap.put(room.getIdentifier(), room); //Add it to the map
        }

    }

    private Scanner setInFile(String file) throws FileNotFoundException {
        FileReader reader = new FileReader(file);  //So we can read the file
        return new Scanner(reader);
    }

    private Room setupRoom(String data) throws BadConfigFormatException, FileNotFoundException {
        String[] array = data.split(",", 3); //Split this array into 3 using the comma as the delimiter
        Room room = new Room(); //Create a room
        String cardCheck = array[0].trim(); //Exception Testing Variable
        if(cardCheck.equals("Room") || cardCheck.equals("Space")) {
            room.setName(array[1].trim()); //Assign name -> Trim whitespace
            data = array[2].trim(); //Next 2 lines are converting string to character
            room.setIdentifier(data.charAt(0)); //Initial/Identifier extracted
            return room;
        }
        else{
            throw new BadConfigFormatException(cardCheck); //Throws exception if Room card is invalid
        }
    }


    public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
        Scanner inFile = setInFile(layoutConfigFile);
        ArrayList<String> csvData = new ArrayList<>();
        while(inFile.hasNext())//Go until EOF
        {
            String data = inFile.nextLine(); //Grab it all
            String[] splitData = data.split(","); //Harness the data
            for(int index = 0; index < splitData.length; index++){
//                if(index == 0) {
//                    String temp = splitData[0];     //This code block I had to put in because of some weird error with my own .csv file
//                    char x = temp.charAt(0);   //It didn't like the first letter (X) being read in without a comma but when I put in the comma I had to skip
//                    if (!Character.isLetter(x)) { //over the blank entry in the array, splitData[0]= ""
//                        continue;           //OKAY, I learned you need to save as a .csv and not a .csv(UTF-8) and this is no longer a problem!
//                    }
//                }
                String str = splitData[index].trim();
                char key = str.charAt(0);
                if(roomMap.containsKey(key)) {
                    csvData.add(str); //Refine the data
                }
                else{
                    throw new BadConfigFormatException(key);
                }
            }
        }
        int size = csvData.size(); 
        int cols = (int)Math.sqrt(size);
        int rows = (int)Math.ceil(size/(double)cols); //ceil rounds UP
        setNumCols(cols);
        setNumRows(rows);

        if(NUM_COLS*NUM_ROWS == size) {
            buildGameGrid(csvData);//Builds the game grid from ClueLayout.csv file now let's build the adjacency lists for each of the cells
            findAdjacencies();
        }
        else{
            throw new BadConfigFormatException(size, rows, cols);//Wrong size....you get a custom exception:)
        }
    }

    private void buildGameGrid(ArrayList<String> csvData) {
        grid = new BoardCell[NUM_ROWS][NUM_COLS];
        int index = 0; //Using this index var to cycle through the ArrayList of csvData
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                grid[row][col] = new BoardCell(row, col);
                String csv = csvData.get(index);  //Grabbing the string from the arrayList with the index
                char initial = csv.charAt(0); //I am forcing it to be a character
                grid[row][col].setInitial(initial); //Filling in the grid according to the data harnessed from the ArrayList csvData
                index++;
                classify_room_symbology(row, col, csv, initial);
            }
        }
        index = 0;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                String csv = csvData.get(index);  //Grabbing the string from the ArrayList with the index
                sealTheRooms(row, col, csv); //Now that the grid is built you can assign all the doors to a particular Room
                index++;
            }
        }
    }
    private void classify_room_symbology(int row, int col, String csv, char initial) {
        if(csv.length() > 1){ //If string has special characters contained after the initial let's sort them out!
            char key = csv.charAt(1);
            switch(key) {
                case '#' -> { //Setting Label Cell
                    grid[row][col].setLabel();
                    Room roomy = roomMap.get(initial);
                    roomy.setLabelCell(grid[row][col]);
                }
                case '*' -> { //Setting Center Cell
                    grid[row][col].setRoomCenter();
                    Room roomy = roomMap.get(initial);
                    roomy.setCenterCell(grid[row][col]);
                }
                case '^' -> {
                    grid[row][col].setDoorDirection(DoorDirection.UP);
                    grid[row][col].setDoorway();

                }
                case '<' -> { //Setting doors to cells....you can't set ALL the doors to the Room...yet because everything around you is NULL
                    grid[row][col].setDoorDirection(DoorDirection.LEFT);//this will be done in sealTheRooms method after grid is filled
                    grid[row][col].setDoorway();
                }
                case '>' -> {
                    grid[row][col].setDoorDirection(DoorDirection.RIGHT);
                    grid[row][col].setDoorway();

                }
                case 'v' -> {
                    grid[row][col].setDoorDirection(DoorDirection.DOWN);
                    grid[row][col].setDoorway();
                }
                default -> {//The last item that will fall to default will be Secret Cells
                    grid[row][col].setSecretPassage(key); //Assigning Secret cell/Room logic
                    Room roomy = roomMap.get(initial);
                    roomy.setSecretCell(grid[row][col]);
                }

            }
        }
    }
    /*Function is going to help out a lot because then you don't have to do any nasty hard code or sloppy code
    to fill (*) center room adjacency conditions.  The center of the Room space acts as a single space and all doorways and secret passageways
    are directly adjacent
     */
    private void sealTheRooms(int row, int col, String csv) {
        if (csv.length() > 1) { //If string has special characters contained after the initial let's sort them out!
            char key = csv.charAt(1);
            switch (key) {
                case '^' -> {
                    Room roomy = roomMap.get(grid[row - 1][col].getInitial()); //It'll be easy to retrieve all the doors when doing the center (*) adjacency list
                    roomy.setDoorCell(grid[row][col]);
                }
                case '<' -> { 
                    Room roomy = roomMap.get(grid[row][col - 1].getInitial()); //The <door is pointing to which room
                    roomy.setDoorCell(grid[row][col]);  //Assigning the Doors to an ArrayList<Boardcell>                                 
                }
                case '>' -> {
                    Room roomy = roomMap.get(grid[row][col + 1].getInitial());
                    roomy.setDoorCell(grid[row][col]);
                }
                case 'v' -> {
                    Room roomy = roomMap.get(grid[row + 1][col].getInitial());
                    roomy.setDoorCell(grid[row][col]);
                }
            }
        }
    }

    private void findAdjacencies() {
        for (int i = 0; i < NUM_ROWS; i++) {  //Sets all the adjacency lists for each cell
            for (int j = 0; j < NUM_COLS; j++) {
                calcAdjacencies(grid[i][j]);
            }
        }
    }

    public void calcAdjacencies(BoardCell cell) {

        if (cell.getInitial() != 'X' && !cell.isDoorway() && !cell.isRoomCenter()) {
            addItUp(cell);  //Primitives such as chars can not use .equals method
        }
        else if (cell.isDoorway()) {
            addItUp(cell);
            switch (cell.getDoorDirection()) { //Center cells are directly adjacent to all doorways
                case RIGHT -> {
                    Room room = roomMap.get(grid[cell.getRow()][cell.getCol() + 1].getInitial());
                    BoardCell center = room.getCenterCell();
                    cell.addAdjacency(center);
                }
                case LEFT -> {
                    Room room = roomMap.get(grid[cell.getRow()][cell.getCol() - 1].getInitial());
                    BoardCell center = room.getCenterCell();
                    cell.addAdjacency(center);
                }
                case UP -> {
                    Room room = roomMap.get(grid[cell.getRow() - 1][cell.getCol()].getInitial());
                    BoardCell center = room.getCenterCell();
                    cell.addAdjacency(center);
                }
                case DOWN -> {
                    Room room = roomMap.get(grid[cell.getRow() + 1][cell.getCol()].getInitial());
                    BoardCell center = room.getCenterCell();
                    cell.addAdjacency(center);
                }
            }
        }
        else if (cell.isRoomCenter()) { //Explicit Room center, Pretty proud of this code block
            char roomID = cell.getInitial();//this inspired me to sealTheRooms so this would work smoothly
            Room room = roomMap.get(roomID);
            ArrayList<BoardCell> theRoom = room.getDoorCells();
            
            //Using var here because it's less cluttered and easier to read w/o BoardCell identifier
            for (var door : theRoom) { //For each door in the room  //var is equivalent to auto in c++
                cell.addAdjacency(door);//assign each to the center (*) cell
            }  
            
            if(room.getSecretCell() != null)//Is there a secret passageway?
                addSecret(cell, room); //If there is a secret passage this leads you to center (*) cell of that room
        }
    }

    private void addSecret(BoardCell cell, Room room) {
        BoardCell doorOfSecrets = room.getSecretCell(); //Find the room
        char secretKey = doorOfSecrets.getSecretPassage(); //Obtain the secret key
        Room secretRoom = roomMap.get(secretKey);//Burrow our way to the Secret Room
        doorOfSecrets = secretRoom.getCenterCell();//To unveil the Center(*) of the new secret Room :)
        cell.addAdjacency(doorOfSecrets);
    }

    private void addItUp(BoardCell cell) { //Normal adjacency rules for adding cells
        if (cell.getCol() < NUM_COLS - 1) {
            BoardCell cell_Right = getCell(cell.getRow() , cell.getCol()+1);
            if (cell_Right.getInitial() == 'W')
                cell.addAdjacency(cell_Right); //Refactored variables for readability
        }
        if (cell.getCol() > 0) {
            BoardCell cell_Left = getCell(cell.getRow(), cell.getCol()-1);
            if (cell_Left.getInitial() == 'W')
                cell.addAdjacency(cell_Left);
        }
        if (cell.getRow() > 0) {
            BoardCell cell_Up = getCell(cell.getRow()-1, cell.getCol() );
            if (cell_Up.getInitial() == 'W')
                cell.addAdjacency(cell_Up);
        }
        if (cell.getRow() < NUM_ROWS - 1) {
            BoardCell cell_Down = getCell(cell.getRow()+1, cell.getCol() );
            if (cell_Down.getInitial() == 'W')
                cell.addAdjacency(cell_Down);
        }
    }

    public void calcTargets(BoardCell startCell, int pathLength) {
        visited.clear();//Reset
        visited.add(startCell);
        targets.clear();//Reset
        findAllTargets(startCell, pathLength);
    }
    private void findAllTargets(BoardCell thisCell, int numSteps){
        Set<BoardCell> adjacentCells = thisCell.getAdjList();
        for(var adjCell : adjacentCells){//I think this is a good use of var(auto in c++) BoardCell seems cumbersome and it conveys the for-each loop explicitly
            if(visited.contains(adjCell) || (adjCell.getOccupied() && !adjCell.isRoomCenter())) { //better with this style
                continue; //Critical to not do a break right here since you want it to keep cycling through the adjacencies
            }
            visited.add(adjCell); //Add to visited list
            if(numSteps == 1 || adjCell.isRoomCenter()) { //Base Case
                targets.add(adjCell);//BAM
                if(adjCell.isRoomCenter()){//If you reach this room center cell...advance to next adjCell
                    continue;
                }
            }
            else {
                findAllTargets(adjCell, numSteps-1); //Recurse
            }
            visited.remove(adjCell);//Remove from visited list
        }
    }
    public Room getRoom(BoardCell cell) { return roomMap.get(cell.getInitial());}
    public Room getRoom(char key){ return roomMap.get(key); }
    public int getNumRows() { return NUM_ROWS; }
    public int getNumColumns() { return NUM_COLS; }
    public BoardCell getCell(int x, int y) { return grid[x][y]; }
    public Set<BoardCell> getAdjList(int row, int col) { return getCell(row, col).getAdjList();}
    private static void setNumRows(int numRows) {
        NUM_ROWS = numRows;
    }
    private static void setNumCols(int numCols) { NUM_COLS = numCols; }
    public Set<BoardCell> getTargets() { return targets; }
}
//