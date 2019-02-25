package paths;

import java.util.ArrayList;

/**
 * @author vx5
 *
 *         Houses findPath() method, which delegates finding a path between two
 *         PathNodes, represented by their names, to the correct path method.
 */
public class PathFinder {
  // Stores instance of PathDbOp to be used for operations
  private PathDbOp op;

  /**
   * Constructor that stores primary operator to be used in finding new paths.
   *
   * @param newOp PathDbOp to be used in finding paths
   */
  public PathFinder(PathDbOp newOp) {
    op = newOp;
  }

  /**
   * Returns a path between two PathNodes, represented by their String names,
   * and based on a provided type of path to be used.
   *
   * @param pathType type of path to be used, currently only option is
   *                 "dijkstra"
   * @param fromNode name of node to be used as start of path
   * @param toNode   name of node to be used as end of path
   * @return ArrayList of PathNodes along path from node indicated by fromNode
   *         to node indicated by toNode
   * @throws Exception FIXTHISFIXTHIS
   */
  public ArrayList<PathNode> findPath(String pathType, String fromName,
      String toName) throws Exception {
    //@formatter:off
    switch (pathType) {
      case "dijkstra":
        return Dijkstra.findPath(op, fromName, toName);
      default:
        return Dijkstra.findPath(op, fromName, toName);
    }
    //@formatter:on
  }
}
