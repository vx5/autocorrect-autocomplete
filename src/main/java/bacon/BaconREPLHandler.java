package bacon;

import java.util.ArrayList;
import java.util.Arrays;

import paths.PathNode;

/**
 * @author vx5
 *
 *         Class that houses static handle() message which orchestrates how
 *         Bacon responds to relevant REPL commands.
 */
public final class BaconREPLHandler {

  private BaconREPLHandler() {
  }

  /**
   * Parses input commands from REPL, sends appropriate instructions to core
   * operator class.
   *
   * @param splitLine array of Strings that represents inputs to the REPL
   */
  public static void handle(String[] splitLine) {
    // Stores instance of BaconOperator to use here
    BaconOperator op = BaconOperator.getInst();
    // Checks for case of "mdb" (database-setting) command
    if (splitLine[0].contentEquals("mdb")) {
      if (splitLine.length != 2) {
        // If incorrect number of arguments, print error message, return out of
        // function call
        System.out
            .println("ERROR: mdb command requires one argument, file path to "
                + "SQLite database");
        return;
      }
      try {
        // Attempts to set database to given filepath, and print appropriate
        // message
        op.setDb(splitLine[1]);
        System.out.println("db set to " + splitLine[1]);
      } catch (Exception e) {
        // Prints desired error message
        System.out.println("ERROR: " + e.getMessage());
      }
    } else {
      // Handles case of "connect" (pathfinding) command
      // Initializes array to hold names of actors
      String[] names = {};
      try {
        // Check for ending in quotation marks
        if (splitLine[splitLine.length - 1]
            .charAt(splitLine[splitLine.length - 1].length() - 1) != '\"') {
          throw new Exception(
              "two names required, each bounded by quotation marks");
        }
        // Store the names of actors (based on location of quotations)
        names = getInQuotes(splitLine);
        // Gets path using operator class
        ArrayList<PathNode> path = op.getPath(names[0], names[1]);
        // Prints paths as instructed in assignment handout
        for (int i = path.size() - 1; i > 0; i--) {
          // Prints desired output
          System.out.println(op.actorIdToName(path.get(i).getId()) + " -> "
              + op.actorIdToName(path.get(i - 1).getId()) + " : "
              + op.filmIdToName(path.get(i - 1).getPrevEdge().getId()));
        }
      } catch (Exception e) {
        // Prints appropriate message if path not found
        if (e.getMessage().contentEquals("path not found")) {
          System.out.println(names[0] + " -/- " + names[1]);
        } else {
          // Prints with standard error message format
          System.out.println("ERROR: " + e.getMessage());
        }
      }
    }
  }

  private static String[] getInQuotes(String[] splitLine) throws Exception {
    String[] nameSplit = Arrays.copyOfRange(splitLine, 1, splitLine.length);
    // Reconstruct all input
    String fullStr = "";
    for (int i = 0; i < nameSplit.length; i++) {
      fullStr += nameSplit[i] + " ";
    }
    // Splits string based on quotation marks
    nameSplit = fullStr.split("\"");
    // Checks for 5 arguments, signaling two properly built Strings
    if (nameSplit.length != 5) {
      throw new Exception(
          "two names required, each bounded by quotation marks");
    }
    // Creates end String
    String[] names = {
        nameSplit[1], nameSplit[3]
    };
    // Returns the name String
    return names;
  }
}
