package repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import KDTreePack.KDTree;
import edu.brown.cs.vnaraya2.stars.AllStars;
import edu.brown.cs.vnaraya2.stars.StarsREPLHandler;
import fileReading.CSVReader;

public class REPL {
	// Runs REPL loop for System.in
	public static void runREPL() {
	    // Creates new BufferedReader to handle input
	    try (BufferedReader br = new BufferedReader(
	    		new InputStreamReader(System.in))) {
	    	// Creates instance of AllStars, KDTree, Stars command handler
	    	AllStars galaxy = new AllStars();
	    	KDTree kdTree = new KDTree();
	    	StarsREPLHandler handler = new StarsREPLHandler(galaxy, 
	    			kdTree);
	    	// REPL loop body
	    	while (true) {
	    		// Reads the command line
	    		String commandLine = br.readLine();
	    		// Checks for EOF character
	    		if (commandLine == null) {
	    			break;
	    		}
	    		// Split command line based on spaces 
	    		String[] splitLine = commandLine.split(" ");
	    		// Check for no arguments
	    		if (splitLine.length == 0) {
	    			System.out.println("ERROR: command arguments required");
	    		}
	    		// If command is correctly related to Stars project, 
	    		// command is passed to the Stars command handler
	    		if (splitLine[0].contentEquals("stars") || 
	    				splitLine[0].contentEquals("neighbors") ||
	    				splitLine[0].contentEquals("radius")) {
		    		handler.handle(splitLine);	    			
	    		}
	    	}
	    }
	    catch (IOException ioe) {
	    	// Not possible. No error message can make sense of this.
	        ioe.printStackTrace();
	    }
	}
}
