package paths;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author vx5
 *
 */
public class PathOrg {
  // Stores instances of methods to be used for operations
  private HashMap<String, PathFinder> map = new HashMap<String, PathFinder>();

  /**
   * Empty constructor.
   */
  public PathOrg() {
  }

  public void addMethod(String name, PathFinder pathFinder) {
    map.put(name, pathFinder);
  }

  public ArrayList<PathNode> findPath(String pathMethod, String startVertName,
      String endVertName) throws Exception {
    return map.get(pathMethod).getPath(startVertName, endVertName);
  }
}
