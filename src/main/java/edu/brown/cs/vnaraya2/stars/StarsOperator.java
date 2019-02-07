package edu.brown.cs.vnaraya2.stars;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

import filereader.CSVReader;
import kdtrees.KDNode;
import kdtrees.KDTree;

/**
 * Handler that executes specific Stars project-related tasks, such as loading
 * Stars from a file, and the neighbors and radius searches.
 *
 * @author vx5
 */
public class StarsOperator {

  // Stores the AllStars class being used in the current REPL
  private AllStars allStars;
  // Stores the k-d tree being used in the current REPL
  private KDTree kdTree;

  /**
   * Constructor that maintains reference to the REPL's instance of AllStars and
   * the KDTree.
   *
   * @param replAllStars REPL's instance of AllStars class
   * @param handlerTree  REPL's instance of k-d tree
   */
  public StarsOperator(AllStars replAllStars, KDTree handlerTree) {
    allStars = replAllStars;
    kdTree = handlerTree;
  }

  /**
   * Coordinates the loading of new Stars.
   *
   * @param filename the path of the file to be read
   * @return the number of Stars read
   * @throws IOException           if there is error in opening, reading,
   *                               closing file
   * @throws StarsLoadingException if there is error in .csv file
   */
  public int stars(String filename) throws IOException, StarsLoadingException {
    // Stores String arrays of lines in .csv file
    ArrayList<String[]> lines = CSVReader.readFile(filename, ",");
    // Uses star data to populate list of all stars
    allStars.addStars(lines);
    // Builds KDTree from stars
    kdTree.build(allStars.getStars());
    // Prints number of new Stars added
    return allStars.getStars().length;
  }

  /**
   * Manages the execution of a neighbors or radius command given a bound and a
   * coordinate-specific center of search.
   *
   * @param command String that identifies command, should be either "neighbors"
   *                or "radius"
   * @param bound   String that identifies bound for command, should be String
   *                form of an integer for number of neighbors if the command is
   *                "neighbors", or String form of a float radius to search
   *                within if the command is "radius"
   * @param xCoord  String form of float that represents center to search
   *                around's x coordinate
   * @param yCoord  String form of float that represents center to search
   *                around's y coordinate
   * @param zCoord  String form of float that represents center to search
   *                around's z coordinate
   * @return a PriorityQueue of KDNodes that represent all Stars found in the
   *         search
   * @throws InputValidityException if one or more arguments are not valid
   */
  public PriorityQueue<KDNode> commandStarCoord(String command, String bound,
      String xCoord, String yCoord, String zCoord)
      throws InputValidityException {
    // Checks for which command is being called
    if (command.contentEquals("neighbors")) {
      // Passes call to neighbors helper function
      return this.neighbors(bound, xCoord, yCoord, zCoord);
    } else {
      // In case of radius command [and defaults to radius command]
      // Passes call to radius helper function
      return this.radius(bound, xCoord, yCoord, zCoord);
    }
  }

  /**
   * Manages the execution of a neighbors or radius command given a bound and a
   * center of search Star.
   *
   * @param command  String that identifies command, should be either
   *                 "neighbors" or "radius"
   * @param bound    String that identifies bound for command, should be String
   *                 form of an integer for number of neighbors if the command
   *                 is "neighbors", or String form of a float radius to search
   *                 within if the command is "radius"
   * @param starName The name of the Star to be used as the center of the search
   * @return a PriorityQueue of KDNodes that represent all Stars found in the
   *         search
   * @throws InputValidityException if one or more arguments are not valid
   */
  public PriorityQueue<KDNode> commandStarName(String command, String bound,
      String starName) throws InputValidityException {
    // Checks for which command is being called
    if (command.contentEquals("neighbors")) {
      // Passes call to neighbors helper function
      return this.neighbors(bound, starName);
    } else {
      // In case of radius command [and defaults to radius command]
      // Passes call to neighbors helper function
      return this.radius(bound, starName);
    }
  }

  private PriorityQueue<KDNode> neighbors(String strNumNeighbors,
      String strXCoord, String strYCoord, String strZCoord)
      throws InputValidityException {
    // Initializes number of neighbors variable
    int numNeighbors = 0;
    // Tries to parse numNeighbors to an int
    try {
      numNeighbors = Integer.parseInt(strNumNeighbors);
    } catch (NumberFormatException e) {
      // If error in parsing, add message to the error being thrown
      throw new NumberFormatException(
          "number of neighbors given " + "could not be parsed to an integer");
    }
    // Checks for non-negative number of neighbors, throws Exception if
    // issue found
    if (numNeighbors < 0) {
      throw new InputValidityException(
          "number of neighbors cannot" + " be negative");
    }
    // Initializes array of coordinates
    float[] centerCoords = {
        0, 0, 0
    };
    // Tries to parse coordinate Strings to floats
    try {
      centerCoords[0] = Float.parseFloat(strXCoord);
      centerCoords[1] = Float.parseFloat(strYCoord);
      centerCoords[2] = Float.parseFloat(strZCoord);
    } catch (NumberFormatException e) {
      // Again adds message to error being thrown if there is error
      // in parsing
      throw new NumberFormatException(
          "one or more coordinates " + "could not be parsed to a float");
    }
    // Returns the nearest nodes
    return kdTree.getNeighbors(numNeighbors, centerCoords);
  }

  private PriorityQueue<KDNode> neighbors(String strNumNeighbors,
      String starName) throws InputValidityException {
    // Initializes number of neighbors variable
    int numNeighbors = 0;
    // Tries to parse numNeighbors to an int
    try {
      numNeighbors = Integer.parseInt(strNumNeighbors);
    } catch (NumberFormatException e) {
      // If error in parsing, add message to the error being thrown
      throw new NumberFormatException(
          "number of neighbors given " + "could not be parsed to an integer");
    }
    // Checks for non-negative number of neighbors, throws Exception if
    // issue found
    if (numNeighbors < 0) {
      throw new InputValidityException(
          "number of neighbors cannot" + " be negative");
    }
    // Attempts to look up Star specified by name
    float[] centerCoords = allStars.nameToCoordinates(starName);
    // Returns the nearest nodes. +1 So that given node is
    // included, but can be excluded
    return this.removeStar(kdTree.getNeighbors(numNeighbors + 1, centerCoords),
        starName);
  }

  private PriorityQueue<KDNode> radius(String strRadius, String strXCoord,
      String strYCoord, String strZCoord) throws InputValidityException {
    // Initializes radius variable
    float radius = 0;
    // Tries to parse radius to a float
    try {
      radius = Float.parseFloat(strRadius);
    } catch (NumberFormatException e) {
      // If error in parsing, add message to the error being thrown
      throw new NumberFormatException(
          "radius given " + "could not be parsed to a float");
    }
    // Checks for non-negative radius, throws Exception if
    // issue found
    if (radius < 0) {
      throw new InputValidityException("radius cannot" + " be negative");
    }
    // Initializes array of coordinates
    float[] centerCoords = {
        0, 0, 0
    };
    // Tries to parse coordinate Strings to floats
    try {
      centerCoords[0] = Float.parseFloat(strXCoord);
      centerCoords[1] = Float.parseFloat(strYCoord);
      centerCoords[2] = Float.parseFloat(strZCoord);
    } catch (NumberFormatException e) {
      // Again adds message to error being thrown if there is error
      // in parsing
      throw new NumberFormatException(
          "one or more coordinates " + "could not be parsed to a float");
    }
    // Returns nodes chosen by KDTree's searchRadius() method
    return kdTree.searchRadius(radius, centerCoords);
  }

  private PriorityQueue<KDNode> radius(String strRadius, String starName)
      throws InputValidityException {
    // Initializes radius variable
    float radius = 0;
    // Tries to parse radius to a float
    try {
      radius = Float.parseFloat(strRadius);
    } catch (NumberFormatException e) {
      // If error in parsing, add message to the error being thrown
      throw new NumberFormatException(
          "radius given " + "could not be parsed to a float");
    }
    // Checks for non-negative radius, throws Exception if
    // issue found
    if (radius < 0) {
      throw new InputValidityException("radius cannot" + " be negative");
    }
    // Attempts to look up Star specified by name
    float[] centerCoords = allStars.nameToCoordinates(starName);
    // Returns the radius-found nodes. So that given node is
    // excluded, removes it
    return this.removeStar(kdTree.searchRadius(radius, centerCoords), starName);
  }

  private PriorityQueue<KDNode> removeStar(PriorityQueue<KDNode> q,
      String starName) throws InputValidityException {
    // Makes a new PriorityQueue that will be filled
    PriorityQueue<KDNode> newQueue = new PriorityQueue<KDNode>(q.comparator());
    // Gets the ID of the starName given
    String starID = allStars.nameToID(starName);
    // Iterates through entirety of given queue
    while (q.peek() != null) {
      // Obtains first KDNode in queue
      KDNode currentNode = q.remove();
      // If node's Spatial does not match the given star's ID, add it to
      // the new queue
      if (!currentNode.getNodeObject().getID().contentEquals(starID)) {
        newQueue.add(currentNode);
      }
    }
    // Returns new queue
    return newQueue;
  }
}
