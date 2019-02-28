package bacon;

import java.util.ArrayList;

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
      if (splitLine.length != 3) {
        System.out.println("ERROR: connect command requires two arguments, "
            + "names of actors to connect");
      }
      try {
        // Gets path
        ArrayList<PathNode> path = op.getPath(splitLine[1], splitLine[2]);
        //
        for (int i = 0; i < path.size() - 1; i++) {
          // Prints desired output
          System.out.println(op.actorIdToName(path.get(i).getId()) + " -> "
              + op.actorIdToName(path.get(i + 1).getId()) + " : "
              + op.filmIdToName(path.get(i).getPrevEdge().getId()));
        }
      } catch (Exception e) {
        if (e.getMessage().contentEquals("path not found")) {
          System.out.println(splitLine[1] + " -/- " + splitLine[2]);
        }
        // Prints standard error message
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }
}
