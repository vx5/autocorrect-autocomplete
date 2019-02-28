package dijkstra;

import paths.PathEdge;
import paths.PathNode;

public class DEdge<V extends DVertex<V, E>, E extends DEdge<V, E>>
    implements PathEdge {
  // Stores edge ID
  protected String id;
  // Stores edge weight
  protected float weight;
  // Stores vertices on either end of edge
  protected V vertOne;
  protected V vertTwo;

  public DEdge(String newId, float newWeight, V newVertOne) {
    id = newId;
    weight = newWeight;
    vertOne = newVertOne;
  }

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
      return vertOne;
    } else {
      return vertTwo;
    }
  }

  public V getOtherNode(DVertex<V, E> givenVert) {
    if (givenVert.getId().contentEquals(vertOne.getId())) {
      return vertOne;
    } else {
      return vertTwo;
    }
  }

  public float getWeight() {
    return weight;
  }

}
