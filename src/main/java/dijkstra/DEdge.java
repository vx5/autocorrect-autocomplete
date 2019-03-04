package dijkstra;

import paths.PathEdge;
import paths.PathNode;

/**
 * @author vx5
 *
 * @param <V> the specific subclass of DVertex used in this Dijkstra's
 *        implementation
 * @param <E> the specific subclass of DEdge used in this Dijkstra's
 *        implementation
 */
public class DEdge<V extends DVertex<V, E>, E extends DEdge<V, E>>
    implements PathEdge {
  // Stores edge ID
  protected String id;
  // Stores edge weight
  protected float weight;
  // Stores vertices on either end of edge
  protected V vertOne;
  protected V vertTwo;

  /**
   * Constructor that stores edge's id, weight, and starting vertex.
   *
   * @param newId      id of this edge
   * @param newWeight  weight of this edge
   * @param newVertOne vertex stored at the start of this edge
   */
  public DEdge(String newId, float newWeight, V newVertOne) {
    id = newId;
    weight = newWeight;
    vertOne = newVertOne;
  }

  /**
   * Sets the ending vertex of this edge.
   *
   * @param newVertTwo sets ending vertex of this edge
   */
  public void setTail(V newVertTwo) {
    vertTwo = newVertTwo;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public PathNode getOtherNode(PathNode givenNode) {
    if (givenNode.getId().contentEquals(vertOne.getId())) {
      return vertTwo;
    } else {
      return vertOne;
    }
  }

  /**
   * Returns the the vertex opposite the given node on a particular edge.
   *
   * @param givenVert the given Vertex, whose opposite vertex we want
   * @return the opposite vertex
   */
  public V getOtherNode(DVertex<V, E> givenVert) {
    if (givenVert.getId().contentEquals(vertOne.getId())) {
      return vertTwo;
    } else {
      return vertOne;
    }
  }

  /**
   * Returns the weight of this edge.
   *
   * @return the weight of this edge in float form
   */
  public float getWeight() {
    return weight;
  }

}
