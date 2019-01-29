package edu.brown.cs.vnaraya2.stars;

import java.util.ArrayList;

import KDTreePack.Spatial;

public class Star implements Spatial {
	// Instance variable that stores ID
	// String class used because it is most generic
	private String id;
	// Instance variable that stores star's name
	private String name;
	// Instance variable that stores coordinates
	private float[] coordinates;
	
	public Star() {}
	
	public Star(String starID, String starName, 
			float[] starCoordinates) {
		id = starID;
		name = starName;
		coordinates = starCoordinates;
	}

	@Override
	public float[] getCoordinates() {
		// Returns coordinate ArrayList
		return coordinates; 
	}

	@Override
	public String getID() {
		// Returns star ID
		return id;
	}
	
	public boolean hasName() {
		// Returns whether star has name
		return (name.length() != 0);
	}
	
	public String getName() {
		// Returns star's name
		return name;
	}
}
	
	
	