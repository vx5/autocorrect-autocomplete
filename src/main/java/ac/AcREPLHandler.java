package ac;

import java.util.Arrays;
import java.util.LinkedList;

import filereader.StringOps;

public class AcREPLHandler {
  private AcCoordinator coord;

  public AcREPLHandler(AcCoordinator c) {
    coord = c;
  }

  public void handle(String command) {
    // Split command line according to spaces
    String[] splitLine = command.split(" ");
    // Get main operator to be used for this project
    AcOperator o = coord.getOp(0);
    // Check for case of simple status requests
    if (splitLine.length == 1) {
      // Get the requested status
      switch (splitLine[0]) {
      case "prefix":
        System.out.println("prefix " + o.getPrefixStatus());
        break;
      case "whitespace":
        System.out.println("whitespace " + o.getWsStatus());
        break;
      case "smart":
        System.out.println("smart " + o.getSmartStatus());
        break;
      case "led":
        System.out.println("led " + o.getLedSetting());
        break;
      default:
        System.out.println(
            "ERROR: corpus, ac commands require at least one argument");
        return;
      }
      // Check for case of simple status updates
    } else if (splitLine.length == 2 && (splitLine[0].contentEquals("prefix")
        || splitLine[0].contentEquals("whitespace")
        || splitLine[0].contentEquals("smart")
        || splitLine[0].contentEquals("led"))) {
      // If command is a led command, parse and pass argument
      if (splitLine[0].contentEquals("led")) {
        try {
          int newSetting = Integer.parseInt(splitLine[1]);
          // Pass if non-negative integer
          if (newSetting >= 0) {
            o.setLedSetting(newSetting);
            // Print error message if not
          } else {
            System.out.println("ERROR: argument to 'led' cannot be negative");
            return;
          }
        } catch (NumberFormatException e) {
          // If could not be parsed, print error message
          System.out.println(
              "ERROR: argument to 'led' could not be parsed to an Integer");
          return;
        }
        // Otherwise, move on to checking for "on" or "off input
      } else {
        // Initialize variable storing new setting
        boolean onStatus;
        if (splitLine[1].contentEquals("on")) {
          onStatus = true;
        } else if (splitLine[1].contentEquals("off")) {
          onStatus = false;
        } else {
          // Print error message
          System.out.println("ERROR: \"on\" or \"off\" argument expected");
          return;
        }
        // Again, use switch statement to direct command
        switch (splitLine[0]) {
        case "prefix":
          o.setPrefixStatus(onStatus);
          break;
        case "whitespace":
          o.setWsStatus(onStatus);
          break;
        case "smart":
          o.setSmartStatus(onStatus);
          break;
        }
      }
      // Check for corpus command
    } else if (splitLine[0].contentEquals("corpus")) {
      // Check for too many inputs
      if (splitLine.length != 2) {
        // Print error message
        System.out
            .println("ERROR: corpus command requires one argument, a filepath");
        return;
      }
      // Stores the filepath
      String filepath = splitLine[1];
      // Reads from file
      try {
        o.addCorpus(filepath);
        // Print desired output line
        System.out.println("corpus " + filepath + " added");
        // End call
        return;
      } catch (Exception e) {
        // Prints error message
        System.out.println("ERROR: " + e.getMessage());
        return;
      }
      // Check for ac command
    } else if (splitLine[0].contentEquals("ac")) {
      try {
        // Processes command list for "cleaning"
        String[] cleanSplit = StringOps.cleanInput(command).split(" ");
        // Gets the output list
        LinkedList<String> output = o
            .ac(Arrays.copyOfRange(cleanSplit, 1, cleanSplit.length));
        // Print back command line
        System.out.println(command);
        // Prints best suggestions to user, up to 5
        for (int i = 0; i < Math.min(output.size(), 5); i++) {
          System.out.println(output.get(i));
        }
      } catch (Exception e) {
        // Prints error message
        System.out.println("ERROR: " + e.getMessage());
        return;
      }
    } else {
      // Print error message
      System.out
          .println("ERROR: updates to settings require only two arguments:\n"
              + "one for setting (e.g. \"prefix\") and one for the new status (\"on\" or "
              + "\"off\")");
    }
  }

}
