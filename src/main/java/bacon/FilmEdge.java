package bacon;

import dijkstra.DEdge;

public class FilmEdge extends DEdge<ActorVertex, FilmEdge> {
  //
  private String filmName;

  public FilmEdge(String newName, String newId, float newWeight,
      ActorVertex newVertOne) {
    super(newId, newWeight, newVertOne);
    filmName = newName;
  }

  public String getName() {
    return filmName;
  }
}
