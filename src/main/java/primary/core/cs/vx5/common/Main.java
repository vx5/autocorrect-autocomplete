package primary.core.cs.vx5.common;

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

  private static final int DEFAULT_PORT = 5555;

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
    parser.accepts("repl");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);
    // Creates instance of AcCoordinator and AcOperator for Autocorrect
    AcCoordinator coordinator = new AcCoordinator();
    try {
      coordinator.addOp(new AcOperator());
    } catch (Exception e) {
      e.printStackTrace();
    }
    // Creates new instance of GUI object, accesses if the gui flagged was
    // used, passes essential objects for Autocorrect
    GUI gui = new GUI(coordinator);
    if (options.has("repl")) {
      // Uses the REPL class's static runREPL() method do trigger the REPL
      REPL.runREPL(coordinator);
    }
    // Default to GUI for deployment
    gui.runSparkServer((int) options.valueOf("port"));
  }

  static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 5555; // return default port if heroku-port isn't set (i.e. on
                 // localhost)
  }
}
