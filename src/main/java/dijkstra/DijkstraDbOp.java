package dijkstra;

public interface DijkstraDbOp<V extends DVertex<V, E>, E extends DEdge<V, E>> {

  boolean validNeighbors(V from, V to);

  void giveNeighbors(V origin) throws Exception;

  V makeVertex(String Id, float dist);
}
