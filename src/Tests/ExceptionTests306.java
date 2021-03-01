//package Tests;
//
///*
// * This program tests that, when loading config files, exceptions
// * are thrown appropriately.
// */
//
//import java.io.FileNotFoundException;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//
//import clueGame.BadConfigFormatException;
//import clueGame.Board;
//import clueGame.BadConfigFormatException;
//
//
//public class ExceptionTests306 {
//
//
//
//	// Test that an exception is thrown for a config file that does not
//	// have the same number of columns for each row
//
//	//(expected = BadConfigFormatException.class) Started to mess up my code so I did the J-Unit 5 declaration below
//	@Test(expected = BadConfigFormatException.class)//\\ IDE is yelling at me for this expected (all of a sudden) so I'm commenting it out //\\
//	public void testBadColumns() throws BadConfigFormatException, FileNotFoundException { //When I comment them out it fails regardless if I have good tests but you can see
//                                                                                                //that exceptions are being thrown appropriately// I uncommented these lines to turn it in
//		// Note that we are using a LOCAL Board variable, because each
//		// test will load different files
//		Board board = Board.getInstance();
//		board.setConfigFiles("ClueLayoutBadColumns306.csv", "ClueSetup306.txt");
//		// Instead of initialize, we call the two load functions directly.
//		// This is necessary because initialize contains a try-catch.
//		board.loadSetupConfig();
//		// This one should throw an exception
//		board.loadLayoutConfig();
//	}
//
//	// Test that an exception is thrown for the layout file that specifies
//	// a room that is not in the setup file See first test for other important
//	// comments.
//	//(expected = BadConfigFormatException.class)
//	@Test(expected = BadConfigFormatException.class)  //\\ IDE is yelling at me for this expected all of a sudden so I'm commenting it out //\\
//	public void testBadRoom() throws BadConfigFormatException, FileNotFoundException {
//		Board board = Board.getInstance();
//		board.setConfigFiles("ClueLayoutBadRoom306.csv", "ClueSetup306.txt");
//		board.loadSetupConfig();
//		board.loadLayoutConfig();
//	}
//
//	// Test that an exception is thrown for a config file with a room type
//	// that is not Card or Other
//	//(expected = BadConfigFormatException.class)
//	@Test(expected = BadConfigFormatException.class)
//	public void testBadRoomFormat() throws BadConfigFormatException, FileNotFoundException {
//		Board board = Board.getInstance();
//		board.setConfigFiles("ClueLayout306.csv", "ClueSetupBadFormat306.txt");
//		board.loadSetupConfig();
//		board.loadLayoutConfig();
//	}
//
//}
