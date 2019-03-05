package paths;

import java.util.ArrayList;

/**
 * @author vx5
 *
 *         Interface for any path-finding method (e.g. Dijkstra's shortest-path
 *         algorithm).
 */
public interface PathFinder {
  /**
   * Returns a path from one pathNode to another.
   *
   * @param startVert the id of the node to be used as the start of the path
   * @param endVert   the id of the node to which the path is expected to go
   * @return a list of PathNodes which represent the sequence of nodes visited
   *         by the path
   * @throws Exception if there is any error in finding the path, including if
   *                   no path exists
   */
  ArrayList<PathNode> getPath(String startVert, String endVert)
      throws Exception;
}
