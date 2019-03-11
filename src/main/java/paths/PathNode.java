package paths;

/**
 * @author vx5
 *
 *         Interface that represents the most abstract form of a graph node
 *         required to display a path.
 */
public interface PathNode {

  /**
   * Returns the id of this graph node.
   *
   * @return this node's id in String form
   */
  String getId();

  /**
   * Returns the previous edge from this node as specified by the path.
   *
   * @return this node's previous edge according to the path
   */
  PathEdge getPrevEdge();
}
