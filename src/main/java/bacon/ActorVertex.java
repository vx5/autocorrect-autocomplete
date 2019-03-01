package bacon;

import dijkstra.DVertex;

public class ActorVertex extends DVertex<ActorVertex, FilmEdge> {
  // Stores the Actor's name
  private String name;

  public ActorVertex(String newName, String newId, float newDist) {
    super(newId, newDist);
    name = newName;
  }

  public String getName() {
    return name;
  }
}
