package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Board {
    private Set<BoardCell> targets;
    private Set<BoardCell> visited;
    private BoardCell[][] grid;
    private Map<Character, Room> roomMap;
    private String layoutConfigFile;
    private String setupConfigFile;
    private int num_rows;
    private int num_cols;



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
        roomMap = new HashMap<>();//Initialize the map
        Scanner inFile = setInFile(setupConfigFile);

        while (inFile.hasNext())//Go until EOF
        {
            String data = inFile.nextLine();
            if (!data.contains("//")) //Edit out pesky comments :)
                setupRoom(data); //Configures each Room with name and identifier & then sets the room
        }
        inFile.close();
    }

    private void setupRoom(String data) throws BadConfigFormatException, FileNotFoundException {
        String[] array = data.split(",", 3); //Split this array into 3 using the comma as the delimiter
        Room room = new Room(); //Create a room
        String cardCheck = array[0].trim(); //Exception Testing Variable

        if (cardCheck.equals("Room") || cardCheck.equals("Space")) {
            room.setName(array[1].trim()); //Assign name -> Trim whitespace
            data = array[2].trim(); //Next 2 lines are converting string to character
            room.setIdentifier(data.charAt(0)); //Initial/Identifier extracted

            setRoom(room);//Effectively adding the Room to the roomMap
        } else
            throw new BadConfigFormatException(cardCheck); //Throws exception if Room card is invalid
    }

    public void loadLayoutConfig() throws FileNotFoundException, BadConfigFormatException {
        Scanner inFile = setInFile(layoutConfigFile);
        ArrayList<String> csvData = new ArrayList<>();

        while (inFile.hasNext())//Go until EOF
        {
            String data = inFile.nextLine(); //Grab it all
            String[] splitData = data.split(","); //Harness the data
            //For each index in splitData
            for (var index : splitData) { //var is equivalent to auto, I think they make the for-each loops read more intuitively
                String cleanData = index.trim();
                char roomID = cleanData.charAt(0);

                if (roomMap.containsKey(roomID))
                    csvData.add(cleanData); //Now the data has been refined from raw input
                else
                    throw new BadConfigFormatException(roomID); //Means an undefined letter was found in the file data
            }
        }
        inFile.close();
        int gridSize = setRowsCols(csvData);
        if (num_cols * num_rows == gridSize) {
            buildGameGrid(csvData);//Builds the game grid from ClueLayout.csv file
            findAdjacencies();//Let's build the adjacency lists for each of the cells
        } else
            throw new BadConfigFormatException(gridSize, num_rows, num_cols);//Wrong size....you get a custom exception:)
    }

    private void buildGameGrid(ArrayList<String> csvData) {
        grid = new BoardCell[num_rows][num_cols];//Initialize game grid
        int index = 0; //Using this index variable to cycle through the ArrayList of csvData
        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_cols; col++) {
                grid[row][col] = new BoardCell(row, col);
                String csv = csvData.get(index);  //Grabbing the string from the ArrayList with the index
                char roomID = csv.charAt(0); //Storing the first index of csv into roomID

                grid[row][col].setInitial(roomID); //Filling in the grid according to the data harnessed from the ArrayList csvData
                classify_room_symbology(row, col, csv, roomID);
                index++;
            }
        }
        index = 0;
        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_cols; col++) {
                String csv = csvData.get(index);
                //Now that the grid is built you can assign all the doors to a particular Room
                assignDoors(row, col, csv);
                index++;
            }
        }
    }
    private void classify_room_symbology(int row, int col, String csv, char roomID) {
        if (csv.length() > 1) { //If string has special characters contained after the initial let's sort them out!
            char symbol = csv.charAt(1);
            switch (symbol) {
                case '#' -> { //Setting Label Cell
                    grid[row][col].setLabel();
                    Room roomy = getRoom(roomID);
                    roomy.setLabelCell(grid[row][col]);
                }
                case '*' -> { //Setting Center Cell
                    grid[row][col].setRoomCenter();
                    Room roomy = getRoom(roomID);
                    roomy.setCenterCell(grid[row][col]);
                }
                case '^' -> {
                    grid[row][col].setDoorDirection(DoorDirection.UP);
                    grid[row][col].setDoorway();
                }
                case '<' -> { //Setting doors to cells....you can't set ALL the doors to the Room...yet because everything around you is NULL
                    grid[row][col].setDoorDirection(DoorDirection.LEFT);//this will be done in assignDoors method after grid is filled
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
                    grid[row][col].setSecretPassage(symbol); //Assigning Secret cell/Room logic
                    Room roomy = getRoom(roomID);
                    roomy.setSecretCell(grid[row][col]);
                }

            }
        }
    }
    /*Function is going to help out a lot because then you don't have to do any nasty hard code or sloppy code
    to fill (*) center cell adjacency conditions.  The center of the Room space acts as a single space and all doorways and secret passageways
    are directly adjacent.  ALl the Rooms have 2-6 doors on our SpaceShip design.
     */
    private void assignDoors(int row, int col, String csv) {
        if (csv.length() > 1) { //If string has special characters contained after the initial let's sort them out!
            char key = csv.charAt(1);
            switch (key) {
                case '^' -> {
                    Room roomy = getRoom(grid[row - 1][col]);
                    roomy.setDoorCell(grid[row][col]);
                }
                case '<' -> {
                    Room roomy = getRoom(grid[row][col - 1]); //The <<<<door is pointing to which Room
                    roomy.setDoorCell(grid[row][col]);  //Assigning the doors to an ArrayList<Boardcell> doorCells
                }
                case '>' -> {
                    Room roomy = getRoom(grid[row][col + 1]);
                    roomy.setDoorCell(grid[row][col]);
                }
                case 'v' -> {
                    Room roomy = getRoom(grid[row + 1][col]);
                    roomy.setDoorCell(grid[row][col]);
                }
            }
        }
    }

    private void findAdjacencies() {
        for (int row = 0; row < num_rows; row++)  //Sets all the adjacency lists for each cell
            for (int col = 0; col < num_cols; col++)
                calcAdjacencies(grid[row][col], row, col);
    }

    public void calcAdjacencies(BoardCell cell, int row, int col) {

        if (cell.getInitial() != 'X' && !cell.isDoorway() && !cell.isRoomCenter())
            addItUp(cell, row, col); //Primitives such as chars cannot use .equals method
        else if (cell.isDoorway()) {
            addItUp(cell, row, col);
            switch (cell.getDoorDirection()) { //Center cells are directly adjacent to all doorways
                case RIGHT -> {
                    Room room = getRoom(grid[row][col + 1]);
                    BoardCell center = room.getCenterCell();

                    cell.addAdjacency(center);
                }
                case LEFT -> {
                    Room room = getRoom(grid[row][col - 1]);
                    BoardCell center = room.getCenterCell();

                    cell.addAdjacency(center);
                }
                case UP -> {
                    Room room = getRoom(grid[row - 1][col]);
                    BoardCell center = room.getCenterCell();

                    cell.addAdjacency(center);
                }
                case DOWN -> {
                    Room room = getRoom(grid[row + 1][col]);
                    BoardCell center = room.getCenterCell();

                    cell.addAdjacency(center);
                }
            }
        }
        else if (cell.isRoomCenter()) { //Explicit Room center, Pretty proud of this code block
            char roomID = cell.getInitial();//this inspired me to assignDoors so this would work smoothly
            Room room = getRoom(roomID);
            ArrayList<BoardCell> theRoom = room.getDoorCells();

            //Using var here because it's less cluttered and easier to read w/o BoardCell type
            for (var door : theRoom)  //For each door in theRoom  //var is equivalent to auto in c++
                cell.addAdjacency(door);//assign each to the center (*) cell

            if (room.getSecretCell() != null)//Is there a secret passageway?
                addSecret(cell, room); //If there is a secret passage this leads you to center (*) cell of that room
        }
    }

    private void addSecret(BoardCell cell, Room room) {
        BoardCell secretCell = room.getSecretCell(); //Find the secret cell
        char secretKey = secretCell.getSecretPassage(); //Obtain the secret key and burrow through the secret passage
        Room secretRoom = getRoom(secretKey); //Put in the secretKey to obtain the secretRoom
        BoardCell secretRoomCenter = secretRoom.getCenterCell();//To unveil the Center(*)

        cell.addAdjacency(secretRoomCenter);
    }

    private void addItUp(BoardCell cell, int row, int col) { //Normal adjacency rules for adding cells
        if (col < num_cols - 1) {
            BoardCell cell_Right = getCell(row, col + 1);

            if (cell_Right.getInitial() == 'W')
                cell.addAdjacency(cell_Right); //Refactored variables for readability
        }
        if (col > 0) {
            BoardCell cell_Left = getCell(row, col - 1);

            if (cell_Left.getInitial() == 'W')
                cell.addAdjacency(cell_Left);
        }
        if (row > 0) {
            BoardCell cell_Up = getCell(row - 1, col);

            if (cell_Up.getInitial() == 'W')
                cell.addAdjacency(cell_Up);
        }
        if (row < num_rows - 1) {
            BoardCell cell_Down = getCell(row + 1, col);

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
    private void findAllTargets(BoardCell thisCell, int numSteps) {
        Set<BoardCell> adjacentCells = thisCell.getAdjList();

        for (var adjCell : adjacentCells) {//I think this is a good use of var(auto in c++) BoardCell seems cumbersome and it conveys the for-each loop explicitly
            if (visited.contains(adjCell) || (adjCell.getOccupied() && !adjCell.isRoomCenter()))
                continue; //Critical to not do a break right here since you want it to keep cycling through the adjacencies

            visited.add(adjCell); //Add to visited list
            if (numSteps == 1 || adjCell.isRoomCenter()) { //Base Case
                targets.add(adjCell);//BAM
                if (adjCell.isRoomCenter()) {//If you reach this room center cell...STOP advancing
                    continue;
                }
            } else {
                findAllTargets(adjCell, numSteps - 1); //Recurse
            }
            visited.remove(adjCell);//Remove from visited list
        }
    }


    //Getters
    public Set<BoardCell> getAdjList(int row, int col) {
        return getCell(row, col).getAdjList();
    }
    public Room getRoom(BoardCell cell) { return roomMap.get(cell.getInitial()); }
    public Room getRoom(char key) { return roomMap.get(key); }
    public int getNumRows() { return num_rows; }
    public int getNumColumns() { return num_cols; }
    public BoardCell getCell(int row, int col) { return grid[row][col]; }
    public Set<BoardCell> getTargets() { return targets; }
    //Setters
    public void setConfigFiles(String layout, String legend) {
        setupConfigFile = legend;
        layoutConfigFile = layout;
    }
    private Scanner setInFile(String file) throws FileNotFoundException {
        FileReader reader = new FileReader(file); //So we can read the file
        return new Scanner(reader);
    }
    private int setRowsCols(ArrayList<String> csvData) {
        int size = csvData.size();
        int cols = (int)Math.sqrt(size);
        int rows = (int)Math.ceil(size/(double)cols); //ceil rounds UP
        setNumCols(cols);
        setNumRows(rows);
        return size;
    }
    private void setRoom(Room room) { roomMap.put(room.getIdentifier(), room);}
    private void setNumRows(int rows) { num_rows = rows; }
    private void setNumCols(int cols) { num_cols = cols; }
}
