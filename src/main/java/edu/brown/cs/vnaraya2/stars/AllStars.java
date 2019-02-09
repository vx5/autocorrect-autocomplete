package edu.brown.cs.vnaraya2.stars;

import java.util.ArrayList;

/**
 * @author vx5
 *
 *         Data structure that stores all of the Stars being considered at a
 *         given moment, and allows manipulation and referencing of knowledge
 *         about those Stars.
 */
public class AllStars {
  // Array that stores all stars
  private Star[] starList;

  /**
   * Constructor that sets the array of Stars to null.
   */
  public AllStars() {
    starList = null;
  }

  /**
   * Uses input information to create Stars in the overall set of Stars.
   *
   * @param starData ArrayList where each String is row of .csv file of Stars
   * @throws StarsLoadingException when there is error in .csv file
   */
  public void addStars(ArrayList<String[]> starData)
      throws StarsLoadingException {
    // Checks for at least one star (first row is the column headers)
    if (starData.size() < 2) {
      // Throws error Exception
      throw new StarsLoadingException("at least two rows required "
          + "in file; one for headers, one for Star attributes");
    } else {
      // Clear existing Stars in starList
      starList = new Star[starData.size() - 1];
      // Iterates through all elements in starData, adds Stars
      for (int i = 1; i < starData.size(); i++) {
        // Add stars using makeStar method
        starList[i - 1] = this.makeStar(starData.get(i));
        // If error signaled, mark that error has occurred
        if (starList[i - 1] == null) {
          // Throws error Exception specifying location
          throw new StarsLoadingException(
              "Star in row " + i + " of .csv file could not be constructed");
        }
      }
    }
  }

  private Star makeStar(String[] starData) throws StarsLoadingException {
    // Check for exactly items in starInfo
    if (starData.length != 5) {
      // Returns null as signal of error
      return null;
    }
    // Creates coordinate array to populate
    float[] coordinates = new float[starData.length - 2];
    // Populates array with floats from starInfo
    for (int infoID = 2; infoID < starData.length; infoID++) {
      // Populates each individual coordinate value
      try {
        coordinates[infoID - 2] = Float.parseFloat(starData[infoID]);
      } catch (NumberFormatException e) {
        return null;
      }
    }
    // Returns new Star object using given information
    return new Star(starData[0], starData[1], coordinates);
  }

  /**
   * Takes in the name (different from ID) of a Star and returns its coordinates
   * in array form.
   *
   * @param name the String name (different from ID) of a Star
   * @return array of coordinates for the Star whose name was input
   * @throws InputValidityException if Star with given name could not be found
   */
  public float[] nameToCoordinates(String name) throws InputValidityException {
    // Iterates through all Stars in the list
    for (int i = 0; i < starList.length; i++) {
      // Checks for matching names
      if (starList[i].getName().contentEquals(name)) {
        // Returns star coordinates
        return starList[i].getCoordinates();
      }
    }
    // In case that no match is found, throws error that
    // no Star with given name was found
    throw new InputValidityException(
        "Star with name \'" + name + "\' could not be found");
  }

  /**
   * Takes in the name (different from ID) of a Star and returns its ID in
   * String form.
   *
   * @param name the String name (different from ID) of a Star
   * @return ID of the Star whose name was input
   * @throws InputValidityException if Star with given name cannot be found
   */
  public String nameToID(String name) throws InputValidityException {
    // Iterates through all Stars in the list
    for (int i = 0; i < starList.length; i++) {
      // Checks for matching names
      if (starList[i].getName().contentEquals(name)) {
        // Returns star's ID
        return starList[i].getID();
      }
    }
    // In case that no match is found, throws error that
    // no Stars with given name was found
    throw new InputValidityException(
        "Star with name \'" + name + "\' could not be found");
  }

  /**
   * Given a Star's ID, returns that Star's name (or a filler or empty String if
   * that Star has no name).
   *
   * @param id a Star's ID in String form
   * @return a Star's name in String form
   * @throws InputValidityException if Star with given ID could not be found
   */
  public String idToName(String id) throws InputValidityException {
    // Iterates through all Stars in the list
    for (int i = 0; i < starList.length; i++) {
      // Checks for matching names
      if (starList[i].getID().contentEquals(id)) {
        // Returns star's ID
        String name = starList[i].getName();
        if (name == null || name == "") {
          name = "-";
        }
        return name;
      }
    }
    // In case that no match is found, throws error that
    // no Stars with given name was found
    throw new InputValidityException(
        "Star with ID \'" + id + "\' could not be found");
  }

  /**
   * Returns the array of Stars that stores all the Stars currently in
   * existence.
   *
   * @return array containing all Stars that currently exist
   */
  public Star[] getStars() {
    return starList;
  }

  /**
   * Clears all existing Stars, failure status.
   */
  public void clearStars() {
    starList = null;
  }

  /**
   * Returns whether Stars have yet been loaded.
   *
   * @return true is there Star are loaded, false if not
   */
  public boolean starsLoaded() {
    return starList != null;
  }
}
