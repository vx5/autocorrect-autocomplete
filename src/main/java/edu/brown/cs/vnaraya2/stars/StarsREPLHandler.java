package edu.brown.cs.vnaraya2.stars;

import java.util.PriorityQueue;

import kdtrees.KDNode;
import kdtrees.KDTree;

/**
 * @author vx5
 *
 *         Object that interprets line of input to the REPL by passing arguments
 *         to the StarsOperator class for processing.
 */
public class StarsREPLHandler {

  // Stores the AllStars class being used in the current REPL
  private AllStars allStars;
  // Stores the k-d tree being used in the current REPL
  private KDTree kdTree;
  // Stores the StarsOperator instance being used to direct arguments
  private StarsOperator op;

  /**
   * Constructor that fills class's instance fields and constructs instance of
   * StarsOperator to process input.
   *
   * @param thisAllStars instance of AllStars that holds all Stars
   * @param thisKDTree   instance of k-d tree object used for all search
   *                     commands
   */
  public StarsREPLHandler(AllStars thisAllStars, KDTree thisKDTree) {
    allStars = thisAllStars;
    kdTree = thisKDTree;
    op = new StarsOperator(allStars, kdTree);
  }

  /**
   * Uses arguments to command line to invoke the appropriate method calls in
   * the StarsOperator class to coordinate the appropriate steps.
   *
   * @param splitLine String array that includes all arguments to the command
   *                  line
   */
  public void handle(String[] splitLine) {
    // Attempt to handle given arguments with error
    try {
      // Loads Stars, if appropriate
      if (splitLine[0].contains("stars")) {
        // Checks for appropriate length
        if (splitLine.length != 2) {
          // Prints error message
          System.out.println("ERROR: 'stars' command"
              + " expects 1 argument in format:\n" + "stars <filepath>");
          // Returns out of this method call
          return;
        }
        // If appropriate length found, pass to the operator
        int numStars = op.stars(splitLine[1]);
        // Print required line to REPL
        System.out.println("Read " + numStars + " stars from " + splitLine[1]);
      } else if (allStars.getStars() == null) {
        // Check for case of command without stars being loaded
        // Prints error message
        System.out.println("ERROR: Stars have not yet been"
            + " successfully loaded with 'stars' command");
        // Returns out of this method call
        return;
      } else if (splitLine.length == 5) {
        // Checks for case of 5-argument command
        // Passes arguments to operator
        this.printPQueueIDs(op.commandStarCoord(splitLine[0], splitLine[1],
            splitLine[2], splitLine[3], splitLine[4]));
      } else {
        // Checks for case of 3-argument command
        // Checks for case of too few arguments
        if (splitLine.length < 3) {
          System.out.println("ERROR: 'neighbors' and 'radius' commands"
              + " expect 2 or 4 additional arguments in formats:\n"
              + "neighbors <number-neighbors> <name-of-star>\n"
              + "neighbors <number-neighbors> <x-coord> <y-coord>"
              + " <z-coord>\n" + "radius <max-radius> <name-of-star>\n"
              + "radius <max-radius> <x-coord> <y-coord>" + " <z-coord>");
        }
        // Joins later splitLine elements to form star name
        String fullName = "";
        for (int i = 2; i < splitLine.length; i++) {
          fullName += splitLine[i];
          // Adds space if there are more
          // elements to come
          if (i + 1 < splitLine.length) {
            fullName += " ";
          }
        }
        // Checks full star name for proper format
        if (fullName.length() < 3 || fullName.charAt(0) != '\"'
            || fullName.charAt(fullName.length() - 1) != '\"') {
          // Prints error message
          System.out.println("ERROR: command must be in format:\n"
              + "<command> <bound> <\"star-name\">");
          // Returns out of this method call
          return;
        }
        // Strips out quotation marks from Star's name
        String starName = fullName.substring(1, fullName.length() - 1);
        // Passes arguments to operator, prints resulting PriorityQueue
        this.printPQueueIDs(
            op.commandStarName(splitLine[0], splitLine[1], starName));
      }
    } catch (Exception e) {
      // Handles all error messages the same -- by printing them
      // Prints given exception
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  private void printPQueueIDs(PriorityQueue<KDNode> q) {
    // Iterates through queue, printing each node's ID
    while (q.peek() != null) {
      System.out.println(q.remove().getNodeObject().getID());
    }
  }
}
