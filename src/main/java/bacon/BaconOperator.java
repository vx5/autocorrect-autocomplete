package bacon;

import java.io.File;
import java.util.ArrayList;

import com.google.common.io.Files;

import paths.PathFinder;
import paths.PathNode;

/**
 * @author vx5
 *
 *         Accepts parsed, raw calls related to Bacon project. Uses singleton
 *         pattern for access, so there is, at most, one instance of this class.
 */
public final class BaconOperator {
  // Stores instance of this object for singleton pattern
  private static final BaconOperator baconOp = new BaconOperator();
  // Stores instance of BaconDbOp used to fulfill PathDbOp implementation
  private final BaconDbOp op = new BaconDbOp();
  // Stores instance of PathFinder that is used for paths
  private final PathFinder finder = new PathFinder(op);

  private BaconOperator() {
  }

  /**
   * For singleton pattern, allows access to the only BaconOperator instance.
   *
   * @return reference to the only BaconOperator instance
   */
  public static BaconOperator getInst() {
    return baconOp;
  }

  /**
   * Assigns a new database to this implementation of the Bacon project.
   *
   * @param dbPath filepath to the new database
   * @throws Exception
   */
  public void setDb(String dbPath) throws Exception {
    // Checks that file exists
    if (!new File(dbPath).isFile()) {
      throw new Exception("file \"" + dbPath + "\" could not be found");
    }
    // Checks that file is of proper type
    // This check could be amended if future uses of this method involve file
    // types besides .sqlite3
    if (!Files.getFileExtension(dbPath).contentEquals("sqlite3")) {
      throw new Exception("file at \"" + dbPath + "\" is not of type .sqlite3");
    }
    // Sets up new database in GraphOp class, which handles graph operations
    op.setSqlDb(dbPath);
  }

  /**
   * Returns an ordered list of PathNodes that correspond to a set of ordered
   * actors that map a path from one given actor to another.
   *
   * @param fromActor String name of actor path should be mapped from
   * @param toActor   String name of actor path should be mapped to
   * @return list of PathNodes representing
   * @throws Exception
   */
  public ArrayList<PathNode> getPath(String fromActor, String toActor)
      throws Exception {
    return finder.findPath("dijkstra", fromActor, toActor);
  }

}
