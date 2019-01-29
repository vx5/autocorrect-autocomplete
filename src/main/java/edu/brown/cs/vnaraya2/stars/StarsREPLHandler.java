package edu.brown.cs.vnaraya2.stars;

import KDTreePack.KDTree;
import fileReading.CSVReader;

public class StarsREPLHandler {
	
	// Stores the AllStars class being used in the current REPL
	private AllStars allStars;
	// Stores the KDTree being used in the current REPL
	private KDTree kdTree;
	
	// Constructor that stores instance of AllStars class being used
	// and of KDTree being used
	public StarsREPLHandler(AllStars replAllStars, KDTree handlerTree) {
		allStars = replAllStars;
		kdTree = handlerTree;
	}
	
	// Handles all run input command related to Stars project
	public void handle(String[] splitLine) {
		// Looks at following arguments differently depending 
		// on command type
		if (splitLine[0].contentEquals("stars")) {
			// Checks for correct number of arguments
			if (splitLine.length != 2) {
				System.out.println("ERROR: stars command expects 1"
						+ " argument");
			}
			// Stores filename as second argument
			String filename = splitLine[1];
			// Uses star data to populate list of all stars
			allStars.addStars(CSVReader.readFile(filename, ","));
			// Builds KDTree from stars
			kdTree.build(allStars.getStars());
		}
		else if (splitLine[0].contentEquals("neighbors")) {
			// Check for standard number of inputs
			if (splitLine.length != 3 && splitLine.length != 5) {
				// Error message for insufficient arguments
				System.out.println("ERROR: neighbors command expects"
						+ " 2 or 4 arguments");
			}
			// Normal input procedure
			else {
				// Stores number of neighbors
				int numNeighbors = Integer.parseInt(splitLine[1]);
				// Instantiates array of neighborIDs for output
				String[] neighborIDs;
				// Check for 3-argument-variant of command line
				if (splitLine.length == 3) {
					// Stores name of star whose neighbors are to be found
					String starName = splitLine[2];
					// Passes to KDTree
					neighborIDs = kdTree.getNeighbors(numNeighbors, 
							allStars.nameToCoordinates(starName));
				}
				// Assumes 5-argument-variant of command line
				else {
					// Stores x, y, and z coordinates to be used for center
					float x = Float.parseFloat(splitLine[2]);
					float y = Float.parseFloat(splitLine[3]);
					float z = Float.parseFloat(splitLine[4]);
					float[] coordinates = {x, y, z};
					// Passes to KDTree
					neighborIDs = kdTree.getNeighbors(numNeighbors,
							coordinates);
				}
				// Prints output
				for (int i = 0; i < neighborIDs.length; i++) {
					System.out.println(neighborIDs[i]);
				}
			}   			
		}
		else if (splitLine[0].contentEquals("radius")) {
			// Check for standard number of inputs
			if (splitLine.length != 3 && splitLine.length != 5) {
				// Error message for insufficient arguments
				System.out.println("ERROR: radius command expects"
						+ " 2 or 4 arguments");
			}
			// Normal input procedure
			else {
				// Stores number of neighbors
				float radius = Float.parseFloat(splitLine[1]);
				// Check for positive radius
				if (radius >= 0) {
					// Instantiates array of validIDs for output
					String[] validIDs;
					// Check for 3-argument-variant of command line
    				if (splitLine.length == 3) {
    					// Stores name of star whose neighbors are to be found
    					String starName = splitLine[2];
    					// Passes to KDTree
    					validIDs = kdTree.searchRadius(radius, 
    							allStars.nameToCoordinates(starName));
    				}
    				// Assumes 5-argument-variant of command line
    				else {
    					// Stores x, y, and z coordinates to be used for center
    					float x = Float.parseFloat(splitLine[2]);
    					float y = Float.parseFloat(splitLine[3]);
    					float z = Float.parseFloat(splitLine[4]);
    					float[] coordinates = {x, y, z};
    					// Passes to KDTree
    					validIDs = kdTree.searchRadius(radius, coordinates);
    				}
    				// Prints output
    				for (int i = 0; i < validIDs.length; i++) {
    					System.out.println(validIDs[i]);
    				}
				}
				// Prints error for negative radius
				else {
					System.out.println("ERROR: radius value must be >= 0");
				}	
			}   	
		}
		// Prints error message for invalid command type
		else {
			System.out.println("ERROR: Command must begin with "
					+ "'stars', 'neighbors', or 'radius'");
		}
	}
}
