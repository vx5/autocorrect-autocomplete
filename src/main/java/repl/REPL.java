package repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
  public static void runREPL(AllStars galaxy, KDTree kdTree) {
    // Creates new BufferedReader to handle input
    try (BufferedReader br = new BufferedReader(
        new InputStreamReader(System.in))) {
      // Creates instance of AllStars, KDTree, Stars command handler
      StarsREPLHandler handler = new StarsREPLHandler(galaxy, kdTree);
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
          handler.handle(splitLine);
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