package bacon;

import java.util.ArrayList;
import java.util.Arrays;

import paths.PathNode;

public class BaconREPLHandler {

  public static void handle(String[] splitLine) {
    // Stores instance of BaconOperator to use here
    BaconOperator op = BaconOperator.getInst();
    //
    if (splitLine[0].contentEquals("mdb")) {
      if (splitLine.length != 2) {
        System.out
            .println("ERROR: mdb command requires one argument, file path to "
                + "SQLite database");
      }
      try {
        op.setDb(splitLine[1]);
        //
        System.out.println("db set to " + splitLine[1]);
      } catch (Exception e) {
        // Prints desired error message
        System.out.println("ERROR: " + e.getMessage());
      }
    } else {
      String[] names = {};
      try {
        // Store the names
        names = getInQuotes(splitLine);
        // Gets path
        ArrayList<PathNode> path = op.getPath(names[0], names[1]);
        //
        for (int i = path.size() - 1; i > 0; i--) {
          // Prints desired output
          System.out.println(op.actorIdToName(path.get(i).getId()) + " -> "
              + op.actorIdToName(path.get(i - 1).getId()) + " : "
              + op.filmIdToName(path.get(i - 1).getPrevEdge().getId()));
        }
      } catch (Exception e) {
        if (e.getMessage().contentEquals("path not found")) {
          System.out.println(names[0] + " -/- " + names[1]);
        } else {
          // Prints standard error message
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
