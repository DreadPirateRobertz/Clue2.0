package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/*Your BadConfigFormatException will simply have two constructors,
one that takes no parameters and sets a default message (pick something meaningful),
the other will take a String as a parameter (messages should be more specific).
 */
public class BadConfigFormatException extends Exception {
         //Please note: log file needs to be deleted or manually cleared upon review in between runs -> not a great way to do in program
         //because it clears it here as it tries to append to log or clears/deleted log if you put logic elsewhere in the program
        //Trying to figure this out in another part of the program but it's always clearing the file and then not writing to it here.....
         PrintWriter out = new PrintWriter(new FileOutputStream(("logfile.txt"),true));//Create PrintWriter object to output to file
                                                         //Code snippet from StackOverFLow on how to append properly


    BadConfigFormatException() throws FileNotFoundException {  //Never triggers in my code since I did a special constructor for each error I encountered
        super("Error: Incorrect configuration format");       //Leaving in code, for the case of general usage later on
        CharSequence csq = "Error: Incorrect configuration format";
        appendToLog(csq);
    }
    public BadConfigFormatException(String card) throws FileNotFoundException {
        super(card + " is not a valid Room or Space card");
        CharSequence csq = card + " is not a valid Room or Space card";
        appendToLog(csq);
    }
    public BadConfigFormatException(char key) throws FileNotFoundException {
        super(key + " is not recognized as being part of this layout, please check your configuration file(s)");
        CharSequence csq = key + " is not recognized as being part of this layout, please check your configuration file(s)";
        appendToLog(csq);
    }
    public BadConfigFormatException(int size, int rows, int cols) throws FileNotFoundException {
        super("There is a size mismatch of your data size of " + size +" being insufficient to fill the grid[" + rows + "x" + cols + "]. You require a data size of "
                        + rows*cols);

        CharSequence csq = "There is a size mismatch of your data size of " + size + " being insufficient to fill the grid[" + rows + "x" + cols + "]. You require a data size of "
                + rows*cols;    //Tried to clean up but super needs to be first and was weird about casting a csq to String
        appendToLog(csq);
    }

    private void appendToLog(CharSequence csq) {
        out.append(csq);
        out.append("\n");
        out.close();
    }

}
