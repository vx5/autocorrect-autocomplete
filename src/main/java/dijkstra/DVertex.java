package dijkstra;

import java.util.HashSet;

import paths.PathEdge;
import paths.PathNode;

public class DVertex<V extends DVertex<V, E>, E extends DEdge<V, E>>
    implements PathNode, Comparable<DVertex<V, E>> {
  // Stores vertex name
  protected String id;
  // Stores current best previous edge
  protected DEdge<V, E> prevEdge;
  // Stores all edges from this vertex
  protected HashSet<E> edges;
  // Stores current distance of this word
  protected float currDist;

  public DVertex(String newId, float newDist) {
    id = newId;
    currDist = newDist;
    prevEdge = null;
  }

  @Override
  public String getId() {
    return id;
  }

  public float getDist() {
    return currDist;
  }

  public void setDist(float newDist) {
    currDist = newDist;
  }

  public void addEdge(E newEdge) {
    edges.add(newEdge);
  }

  @Override
  public PathEdge getPrevEdge() {
    return prevEdge;
  }

  public void setPrevEdge(DEdge<V, E> newEdge) {
    prevEdge = newEdge;
  }

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
