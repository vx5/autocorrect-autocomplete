package paths;

/**
 * @author vx5
 *
 *         Interface that represents most abstract form of graph edge required
 *         to represent a path.
 */
public interface PathEdge {
  /**
   * Returns the id of this edge.
   *
   * @return this edge's id in String form
   */
  String getId();

  /**
   * Returns the opposite vertex from the given vertex.
   *
   * @param givenNode the vertex to be used as the basis for the opposite vertex
   * @return the opposite vertex on this edge from the given vertex
   */
  PathNode getOtherNode(PathNode givenNode);
}
