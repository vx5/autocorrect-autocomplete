package bacon;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.common.io.Files;

import dijkstra.Dijkstra;
import paths.PathNode;
import paths.PathOrg;

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
  private final BaconDbOp op = new BaconDbOp();;
  // Stores instance of PathOrg that is used for paths
  private final PathOrg finder = new PathOrg();;

  private BaconOperator() {
    // Adds Dijkstra to the PathOrg instance
    finder.addMethod("dijkstra", new Dijkstra<ActorVertex, FilmEdge>(op));
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

  public ArrayList<PathNode> getPath(String fromName, String toName)
      throws Exception {
    if (!op.hasDb()) {
      throw new Exception("no database has been loaded yet");
    }
    return finder.findPath("dijkstra", op.actorNameToId(fromName),
        op.actorNameToId(toName));
  }

  public String actorIdToName(String actorId) throws SQLException {
    return op.actorIdToName(actorId);
  }

  public String filmIdToName(String filmId) throws SQLException {
    return op.filmIdToName(filmId);
  }
}
