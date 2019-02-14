package repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import ac.AcCoordinator;
import ac.AcREPLHandler;
import edu.brown.cs.vnaraya2.stars.AllStars;
import edu.brown.cs.vnaraya2.stars.StarsREPLHandler;
import kdtrees.KDTree;

/**
 * Class that holds store static runREPL() method triggers a command REPL that
 * parses input to the command line and directs it to the appropriate handler as
 * necessary.
 *
 * @author vx5
 */
public final class REPL {

  /**
   * Private constructor given utility class.
   */
  private REPL() {
  }

  /**
   * Triggers command REPL that parses input to the terminal's command line and
   * directs it to a handler as necessary.
   *
   * @param galaxy AllStars instance specific to this run of the program
   * @param kdTree KDTree instance specific to this run of the program
   */
  public static void runREPL(AllStars galaxy, KDTree kdTree,
      AcCoordinator coord) {
    // Creates new BufferedReader to handle input
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in))) {
      // Creates instance of AllStars, KDTree, Stars command handler
      StarsREPLHandler starHandler = new StarsREPLHandler(galaxy, kdTree);
      // Creates instance of Autocorrect command handler
      AcREPLHandler acHandler = new AcREPLHandler(coord);
      // REPL loop body
      while (true) {
        // Reads the command line
        String commandLine = br.readLine();
        // Checks for EOF character
        if (commandLine == null) {
          break;
        }
        // Split command line based on spaces
        String[] splitLine = commandLine.split(" ");
        // If command is correctly related to Stars project,
        // command is passed to the Stars command handler
        if (splitLine[0].contentEquals("stars")
            || splitLine[0].contentEquals("neighbors")
            || splitLine[0].contentEquals("radius")) {
          starHandler.handle(splitLine);
          // If command is correctly related to Autocorrect,
          // command is passed to the Autocorrect
          // REPL handler
        } else if (splitLine[0].contentEquals("corpus")
            || splitLine[0].contentEquals("ac")
            || splitLine[0].contentEquals("prefix")
            || splitLine[0].contentEquals("whitespace")
            || splitLine[0].contentEquals("smart")
            || splitLine[0].contentEquals("led")) {
          acHandler.handle(commandLine);
        } else {
          // If command not recognized, print error message
          System.out.println(
              "ERROR: command \'" + splitLine[0] + "\' not recognized");
        }
      }
      // Clears the stars after end of loop
      galaxy.clearStars();
    } catch (IOException ioe) {
      // Print error message
      System.out.println("ERROR: IOError in parsing command line");
    }
  }
}
