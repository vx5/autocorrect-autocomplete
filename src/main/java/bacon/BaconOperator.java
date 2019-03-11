package bacon;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

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
  private static final BaconOperator BACON_OP = new BaconOperator();
  // Stores instance of BaconDbOp used to fulfill PathDbOp implementation
  private final BaconDbOp op = new BaconDbOp();
  // Stores instance of PathOrg that is used for paths
  private final PathOrg finder = new PathOrg();

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
    return BACON_OP;
  }

  /**
   * Assigns a new database to this implementation of the Bacon project.
   *
   * @param dbPath filepath to the new database
   * @throws Exception if there is problem with given file path
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
   * Returns shortest path, if possible, between actors, based on their given
   * names.
   *
   * @param fromName String form of starting actor's name
   * @param toName   String form of ending actor's name
   * @return a list of PathNodes whose order represents the path
   * @throws Exception if the database is not loaded, there is an error finding
   *                   the names in the database, or a path cannot be found
   */
  public ArrayList<PathNode> getPath(String fromName, String toName)
      throws Exception {
    // Checks that database has been loaded
    if (!op.hasDb()) {
      throw new Exception("no database has been loaded yet");
    }
    // Checks that names are not the same
    if (fromName.contentEquals(toName)) {
      throw new Exception("two different names must be entered");
    }
    // Finds path, relying on database to convert given names to actor ids
    return finder.findPath("dijkstra", op.actorNameToId(fromName),
        op.actorNameToId(toName));
  }

  /**
   * Wrapper method that allows conversion of actor's id to name.
   *
   * @param actorId actor of interest's id
   * @return given actor's name
   * @throws SQLException if there is error in interactions with the database
   */
  public String actorIdToName(String actorId) throws SQLException {
    return op.actorIdToName(actorId);
  }

  /**
   * Wrapper method that allows conversion of film's id to name.
   *
   * @param filmId film of interest's id
   * @return given film's name
   * @throws SQLException if there is error in interactions with the database
   */
  public String filmIdToName(String filmId) throws SQLException {
    return op.filmIdToName(filmId);
  }

  /**
   * Wrapper method that returns all films (by id) that an actor (by id) was in.
   *
   * @param actorId basis actor's id
   * @return HashSet of ids of all films that given actor was in
   * @throws SQLException if there is error in interactions with the database
   */
  public HashSet<String> getFilmIds(String actorId) throws SQLException {
    return op.actorIdToFilmIds(actorId);
  }

  /**
   * Wrapper method that returns all actors (by id) that were in a film (by id).
   *
   * @param filmId basis film's id
   * @return HashSet of ids of all actors that were in the given film
   * @throws SQLException if there is error in interactions with the database
   */
  public HashSet<String> getActorIds(String filmId) throws SQLException {
    return op.filmIdToActorIds(filmId);
  }

  /**
   * Wrapper method that returns all actor's names in the database.
   *
   * @return HashSet of names of all actors in the database
   * @throws SQLException if there is error in interactions with the database
   */
  public HashSet<String> getActorNames() throws SQLException {
    return op.getActors();
  }
}
