package edu.brown.cs.vnaraya2.stars;

import java.util.ArrayList;
import java.util.PriorityQueue;

import KDTreePack.KDNode;
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
			// Prints required line to the REPL
			System.out.println("Read " + allStars.getStars().length + 
					" stars from " + filename);
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
				PriorityQueue<KDNode> neighborNodes;
				// Check for 3-argument-variant of command line
				if (splitLine.length == 3) {
					// Stores name of star whose neighbors are to be found
					String starName = splitLine[2];
					// Tells KDTree to not consider the given Star
					// as a neighbor
					kdTree.setAvoidName(starName);
					// Passes to KDTree
					neighborNodes = kdTree.getNeighbors(numNeighbors, 
							allStars.nameToCoordinates(starName));
					// Resets KDTree's avoid string 
					kdTree.resetAvoidName();
				}
				// Assumes 5-argument-variant of command line
				else {
					// Stores x, y, and z coordinates to be used for center
					float x = Float.parseFloat(splitLine[2]);
					float y = Float.parseFloat(splitLine[3]);
					float z = Float.parseFloat(splitLine[4]);
					float[] coordinates = {x, y, z};
					// Passes to KDTree
					neighborNodes = kdTree.getNeighbors(numNeighbors,
							coordinates);
				}
				// Prints all stars by order of nodes in PriorityQueue
				while (neighborNodes.peek() != null) {
					System.out.println(
							neighborNodes.remove().getNodeObject().getID());
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
					PriorityQueue<KDNode> validNodes;
					// Check for 3-argument-variant of command line
    				if (splitLine.length == 3) {
    					// Stores name of star whose neighbors are to be found
    					String starName = splitLine[2];
    					// Tells KDTree not to consider the given Star 
    					// as an eligible Spatial to consider 
    					kdTree.setAvoidName(starName);
    					// Passes to KDTree
    					validNodes = kdTree.searchRadius(radius, 
    							allStars.nameToCoordinates(starName));
    					// Resets KDTree's avoid name
    					kdTree.resetAvoidName();
    				}
    				// Assumes 5-argument-variant of command line
    				else {
    					// Stores x, y, and z coordinates to be used for center
    					float x = Float.parseFloat(splitLine[2]);
    					float y = Float.parseFloat(splitLine[3]);
    					float z = Float.parseFloat(splitLine[4]);
    					float[] coordinates = {x, y, z};
    					// Passes to KDTree
    					validNodes = kdTree.searchRadius(radius, coordinates);
    				}
    				// Prints all stars by order of nodes in PriorityQueue
    				while (validNodes.peek() != null) {
    					System.out.println(
    							validNodes.remove().getNodeObject().getID());
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
