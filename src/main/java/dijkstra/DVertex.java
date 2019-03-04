package dijkstra;

import java.util.HashSet;

import paths.PathEdge;
import paths.PathNode;

/**
 * @author vx5
 *
 * @param <V> subclass of DVertex used in this Dijkstra's implementation
 * @param <E> subclass of DEdge used in this Dijkstra's implementation
 *
 *        Superclass for vertices in Dijkstra's vertex-edge graph, given
 *        application of Dijkstra to finding the shortest path.
 */
public class DVertex<V extends DVertex<V, E>, E extends DEdge<V, E>>
    implements PathNode, Comparable<DVertex<V, E>> {
  // Stores vertex name
  protected String id;
  // Stores current best previous edge
  protected DEdge<V, E> prevEdge;
  // Stores all outgoing edges from this vertex
  protected HashSet<E> edges;
  // Stores current distance of this word
  protected float currDist;

  /**
   * Constructor that uses id, distance from origin or shortest path search,
   * initializes all internal characteristics (stored in instance variables).
   *
   * @param newId   String form of new id
   * @param newDist distance to be used from origin
   */
  public DVertex(String newId, float newDist) {
    id = newId;
    currDist = newDist;
    prevEdge = null;
    edges = new HashSet<E>();
  }

  @Override
  public String getId() {
    return id;
  }

  /**
   * Returns the current distance from the search origin.
   *
   * @return this vertex's current distance
   */
  public float getDist() {
    return currDist;
  }

  /**
   * Sets the current distance from the search origin.
   *
   * @param newDist the new distance to be used for this vertex
   */
  public void setDist(float newDist) {
    currDist = newDist;
  }

  /**
   * Adds a new outgoing edge from this vertex.
   *
   * @param newEdge new edge to be added from this vertex
   */
  public void addEdge(E newEdge) {
    edges.add(newEdge);
  }

  @Override
  public PathEdge getPrevEdge() {
    return prevEdge;
  }

  /**
   * Sets the current best incoming edge to this vertex.
   *
   * @param newEdge new edge to be added to this vertex
   */
  public void setPrevEdge(DEdge<V, E> newEdge) {
    prevEdge = newEdge;
  }

  /**
   * Returns all outgoing edges from this vertex.
   *
   * @return a HashSet of all outgoing edges from this vertex
   */
  public HashSet<E> getEdges() {
    return edges;
  }

  @Override
  public int compareTo(DVertex<V, E> o) {
    if (currDist < o.getDist()) {
      return -1;
    } else if (o.getDist() < currDist) {
      return 1;
    } else {
      return 0;
    }
  }
}
