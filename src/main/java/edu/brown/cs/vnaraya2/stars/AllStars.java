package edu.brown.cs.vnaraya2.stars;

import java.util.ArrayList;

public class AllStars {
	// Data structure that stores all stars
	private Star[] starList;
	
	// Empty constructor
	public AllStars() {}
	
	// Constructs, adds all Stars based on starData
	public void addStars(ArrayList<String[]> starData) {
		// Checks for at least one star
		if (starData.size() < 2) {
			// Print error message
			System.out.println("ERROR: At least one star required in file");
		}
		else {
			// Clear existing Stars in starList
			starList = new Star[starData.size() - 1];
			// Iterates through all elements in starData, adds Stars
			for (int i = 1; i < starData.size(); i++) {
				// Add stars using makeStar method
				starList[i - 1] = this.makeStar(starData.get(i));
			}
		}
	}
	
	public Star makeStar(String[] starData) {
		// Check for at least 3 items in starInfo
		if (starData.length < 3) {
			// Prints error message
			System.out.println("ERROR: Insufficient information in CSV"
					+ "to construct this star");
			// Returns null as signal of error
			return null;
		}
		// Constructs star in case of sufficient information
		else {
			// Creates coordinate array to populate
			float[] coordinates = new float[starData.length - 2];
			// Populates array with floats from starInfo 
			for (int infoID = 2; infoID < starData.length; infoID++) {
				// Populates each individual coordinate value
				coordinates[infoID - 2] = Float.parseFloat(starData[infoID]);
				//TODO: Error message
			}
			// Returns new Star object using given information
			return new Star(starData[0], starData[1], coordinates);
		}
	}
	 
	public float[] nameToCoordinates(String name) {
		// Iterates through all Stars in the list
		for (int i = 0; i < starList.length; i++) {
			// Checks for matching names
			if (starList[i].getName().contentEquals(name)) {
				// Returns star coordinates
				return starList[i].getCoordinates();
			}
		}
		// In case that no match is found, returns null as
		// a signal that name was not found 
		return null;
	}
	
	public Star[] getStars() {
		return starList;
	}
}
