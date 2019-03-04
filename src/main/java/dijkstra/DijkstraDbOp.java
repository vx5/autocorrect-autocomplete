package dijkstra;

/**
 * @author vx5
 *
 * @param <V> the subclass of DVertex to be used in this Dijkstra implementation
 * @param <E> the subclass of DEdge to be used in this Dijkstra implementation
 *
 *        Interface containing all relevant vertex-edge manipulation operations
 *        relevant to Dijkstra's algorithm.
 */
public interface DijkstraDbOp<V extends DVertex<V, E>, E extends DEdge<V, E>> {

  /**
   * Returns whether to vertices are valid neighbors to exist on either side of
   * an edge.
   *
   * @param from vertex on "origin" side of edge to be evaluated
   * @param to   vertex on "destination" side of edge to be valuated
   * @return true if edge is valid, false if not
   */
  boolean validNeighbors(V from, V to);

  /**
   * Extends all outward edges from a given vertex.
   *
   * @param origin vertex on "origin" side of all edges
   * @throws Exception if there is error in interactions with database
   */
  void giveNeighbors(V origin) throws Exception;

  /**
   * Makes a new vertex (of the parametrized type).
   *
   * @param Id   String form of id of new vertex
   * @param dist distance of new vertex from the start of the search
   * @return the new vertex, constructed as specified
   */
  V makeVertex(String Id, float dist);
}
