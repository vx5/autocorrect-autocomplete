package paths;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author vx5
 *
 *         Class that directs path requests to the correct pathfinding method.
 */
public class PathOrg {
  // Stores instances of methods to be used for operations
  private HashMap<String, PathFinder> map = new HashMap<String, PathFinder>();

  /**
   * Empty constructor.
   */
  public PathOrg() {
  }

  /**
   * Adds a new pathfinding method to this PathOrg instance.
   *
   * @param name       String form of name for new pathfinding method
   * @param pathFinder new pathfinding method, which must implement the
   *                   PathFinder instance
   */
  public void addMethod(String name, PathFinder pathFinder) {
    map.put(name, pathFinder);
  }

  /**
   * Finds a path from one node to another in a graph using a given type of
   * pathfinding method.
   *
   * @param pathMethod    the type of pathfinding method to be used (only option
   *                      is currently "dijkstra")
   * @param startVertName the name of the PathNode to be used as the start of
   *                      the path
   * @param endVertName   the name of the PathNode that the path should end at
   * @return a list of PathNodes that represents the returned path
   * @throws Exception if there is any error in finding the path, including if
   *                   no path exists
   */
  public ArrayList<PathNode> findPath(String pathMethod, String startVertName,
      String endVertName) throws Exception {
    return map.get(pathMethod).getPath(startVertName, endVertName);
  }
}
