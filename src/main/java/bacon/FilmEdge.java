package bacon;

import dijkstra.DEdge;

/**
 * @author vx5
 *
 *         Subclass of DEdge specific to this Bacon implementation. The only
 *         additional functionality beyond what Dijkstra edges require is the
 *         storage and retrieval of the edge's name.
 */
public class FilmEdge extends DEdge<ActorVertex, FilmEdge> {
  // Stores film name relevant to this edge
  private String filmName;

  /**
   * Constructor that receives all relevant inputs to superclass as well as name
   * of this FilmEdge.
   *
   * @param newName    name of film relevant to this edge
   * @param newId      id of film relevant to this edge
   * @param newWeight  weight of film relevant to this edge (reciprocal of
   *                   number of actors)
   * @param newVertOne ActorVertex which represents the "origin" vertex of this
   *                   node
   */
  public FilmEdge(String newName, String newId, float newWeight,
      ActorVertex newVertOne) {
    super(newId, newWeight, newVertOne);
    filmName = newName;
  }

  /**
   * Return film's name.
   *
   * @return String form of film name
   */
  public String getName() {
    return filmName;
  }
}
