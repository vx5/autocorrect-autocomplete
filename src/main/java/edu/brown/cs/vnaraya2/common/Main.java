package edu.brown.cs.vnaraya2.common;

import ac.AcCoordinator;
import ac.AcOperator;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import repl.REPL;

/**
 * @author vx5
 *
 *         The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    // Creates instance of AcCoordinator and AcOperator for Autocorrect
    AcCoordinator coordinator = new AcCoordinator();
    coordinator.addOp(new AcOperator());
    // Creates new instance of GUI object, accesses if the gui flagged was
    // used, passes essential objects for Autocorrect
    GUI gui = new GUI(coordinator);
    if (options.has("gui")) {
      gui.runSparkServer((int) options.valueOf("port"));
    }
    // Uses the REPL class's static runREPL() method do trigger the REPL
    REPL.runREPL(coordinator);
  }
}
