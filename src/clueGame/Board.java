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

    public void setConfigFiles(String layout, String legend) {
        setupConfigFile = legend;
        layoutConfigFile = layout;
    }

    public void loadSetupConfig() throws FileNotFoundException, BadConfigFormatException {
        roomMap = new HashMap<>();//Initialize the map
        Scanner inFile = setInFile(setupConfigFile);

        while (inFile.hasNext())//Go until EOF
        {
            String data = inFile.nextLine();
            if (data.contains("//"))   //Edit out pesky comments :)
                continue;

            Room room = setupRoom(data); //Configures room with name and identifier
            roomMap.put(room.getIdentifier(), room); //Add it to the map
        }
    }

    private Room setupRoom(String data) throws BadConfigFormatException, FileNotFoundException {
        String[] array = data.split(",", 3); //Split this array into 3 using the comma as the delimiter
        Room room = new Room(); //Create a room
        String cardCheck = array[0].trim(); //Exception Testing Variable

        if (cardCheck.equals("Room") || cardCheck.equals("Space")) {
            room.setName(array[1].trim()); //Assign name -> Trim whitespace
            data = array[2].trim(); //Next 2 lines are converting string to character
            room.setIdentifier(data.charAt(0)); //Initial/Identifier extracted
            return room;
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
            //For each index of splitData
            for (var index : splitData) { //var is equivalent to auto, I think they make the for-each loops read more intuitively
                String cleanData = index.trim();
                char key = cleanData.charAt(0);

                if (roomMap.containsKey(key))
                    csvData.add(cleanData); //Now the data has been refined from raw input
                else
                    throw new BadConfigFormatException(key);
            }
        }
        int gridSize = setRowsCols(csvData);
        if (num_cols * num_rows == gridSize) {
            buildGameGrid(csvData);//Builds the game grid from ClueLayout.csv file now let's build the adjacency lists for each of the cells
            findAdjacencies();
        } else
            throw new BadConfigFormatException(gridSize, num_rows, num_cols);//Wrong size....you get a custom exception:)
    }

    private void buildGameGrid(ArrayList<String> csvData) {
        grid = new BoardCell[num_rows][num_cols];//Initialize game grid
        int index = 0; //Using this index variable to cycle through the ArrayList of csvData
        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_cols; col++) {
                grid[row][col] = new BoardCell(row, col);
                String csv = csvData.get(index);  //Grabbing the string from the arrayList with the index
                char initial = csv.charAt(0); //I am forcing it to be a character

                grid[row][col].setInitial(initial); //Filling in the grid according to the data harnessed from the ArrayList csvData
                classify_room_symbology(row, col, csv, initial);
                index++;
            }
        }
        index = 0;
        for (int row = 0; row < num_rows; row++) {
            for (int col = 0; col < num_cols; col++) {
                String csv = csvData.get(index);  //Grabbing the string from the ArrayList at the appropriate index
                //Now that the grid is built you can assign all the doors to a particular Room
                sealTheRooms(row, col, csv);
                index++;
            }
        }
    }
    private void classify_room_symbology(int row, int col, String csv, char initial) {
        if (csv.length() > 1) { //If string has special characters contained after the initial let's sort them out!
            char key = csv.charAt(1);
            switch (key) {
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
    to fill (*) center cell adjacency conditions.  The center of the Room space acts as a single space and all doorways and secret passageways
    are directly adjacent
     */
    private void sealTheRooms(int row, int col, String csv) {
        if (csv.length() > 1) { //If string has special characters contained after the initial let's sort them out!
            char key = csv.charAt(1);
            switch (key) {
                case '^' -> {
                    Room roomy = roomMap.get(grid[row - 1][col].getInitial());
                    roomy.setDoorCell(grid[row][col]);
                }
                case '<' -> {
                    Room roomy = roomMap.get(grid[row][col - 1].getInitial()); //The <<<<door is pointing to which Room
                    roomy.setDoorCell(grid[row][col]);  //Assigning the doors to an ArrayList<Boardcell>
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
        for (int row = 0; row < num_rows; row++)  //Sets all the adjacency lists for each cell
            for (int col = 0; col < num_cols; col++)
                calcAdjacencies(grid[row][col]);
    }

    public void calcAdjacencies(BoardCell cell) {

        if (cell.getInitial() != 'X' && !cell.isDoorway() && !cell.isRoomCenter())
            addItUp(cell);  //Primitives such as chars cannot use .equals method

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

            //Using var here because it's less cluttered and easier to read w/o BoardCell type
            for (var door : theRoom)  //For each door in theRoom  //var is equivalent to auto in c++
                cell.addAdjacency(door);//assign each to the center (*) cell

            if (room.getSecretCell() != null)//Is there a secret passageway?
                addSecret(cell, room); //If there is a secret passage this leads you to center (*) cell of that room
        }
    }

    private void addSecret(BoardCell cell, Room room) {
        BoardCell secretRoomCenter = room.getSecretCell(); //Find the room
        char secretKey = secretRoomCenter.getSecretPassage(); //Obtain the secret key and burrow through the secret passage
        Room secretRoom = roomMap.get(secretKey); //Put in the secretKey to obtain the secretRoom
        secretRoomCenter = secretRoom.getCenterCell();//To unveil the Center(*)

        cell.addAdjacency(secretRoomCenter);
    }

    private void addItUp(BoardCell cell) { //Normal adjacency rules for adding cells
        if (cell.getCol() < num_cols - 1) {
            BoardCell cell_Right = getCell(cell.getRow(), cell.getCol() + 1);

            if (cell_Right.getInitial() == 'W')
                cell.addAdjacency(cell_Right); //Refactored variables for readability
        }
        if (cell.getCol() > 0) {
            BoardCell cell_Left = getCell(cell.getRow(), cell.getCol() - 1);

            if (cell_Left.getInitial() == 'W')
                cell.addAdjacency(cell_Left);
        }
        if (cell.getRow() > 0) {
            BoardCell cell_Up = getCell(cell.getRow() - 1, cell.getCol());

            if (cell_Up.getInitial() == 'W')
                cell.addAdjacency(cell_Up);
        }
        if (cell.getRow() < num_rows - 1) {
            BoardCell cell_Down = getCell(cell.getRow() + 1, cell.getCol());

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
            if (visited.contains(adjCell) || (adjCell.getOccupied() && !adjCell.isRoomCenter()))  //better with this style
                continue; //Critical to not do a break right here since you want it to keep cycling through the adjacencies

            visited.add(adjCell); //Add to visited list
            if (numSteps == 1 || adjCell.isRoomCenter()) { //Base Case
                targets.add(adjCell);//BAM
                if (adjCell.isRoomCenter()) {//If you reach this room center cell...advance to next adjCell
                    continue;
                }
            } else {
                findAllTargets(adjCell, numSteps - 1); //Recurse
            }
            visited.remove(adjCell);//Remove from visited list
        }
    }



    //Getters
    public Room getRoom(BoardCell cell) { return roomMap.get(cell.getInitial()); }
    public Room getRoom(char key) { return roomMap.get(key); }
    public int getNumRows() { return num_rows; }
    public int getNumColumns() { return num_cols; }
    public BoardCell getCell(int x, int y) { return grid[x][y]; }
    public Set<BoardCell> getTargets() { return targets; }
    //Setters
    private int setRowsCols(ArrayList<String> csvData) {
        int size = csvData.size();
        int cols = (int) Math.sqrt(size);
        int rows = (int) Math.ceil(size / (double) cols); //ceil rounds UP
        setNumCols(cols);
        setNumRows(rows);
        return size;
    }
    private Scanner setInFile(String file) throws FileNotFoundException {
        FileReader reader = new FileReader(file); //So we can read the file
        return new Scanner(reader);
    }
    public Set<BoardCell> getAdjList(int row, int col) {
        return getCell(row, col).getAdjList();
    }
    private void setNumRows(int rows) { num_rows = rows; }
    private void setNumCols(int cols) {
        num_cols = cols;
    }
}
