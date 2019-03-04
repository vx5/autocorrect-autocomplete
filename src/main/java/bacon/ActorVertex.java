package bacon;

import dijkstra.DVertex;

/**
 * @author vx5
 *
 *         Subclass of DVertex specific to this Bacon implementation. The only
 *         additional functionality beyond what Dijkstra vertices require is the
 *         storage and retrieval of the vertex's name.
 *
 */
public class ActorVertex extends DVertex<ActorVertex, FilmEdge> {
  // Stores the Actor's name
  private String name;

  /**
   * Constructor for ActorVertex, that uses superclass constructor.
   *
   * @param newName name of new ActorVertex
   * @param newId   id of new ActorVertex (required for all DVertexes)
   * @param newDist path distance of new ActorVertex (required for all
   *                DVertexes)
   */
  public ActorVertex(String newName, String newId, float newDist) {
    super(newId, newDist);
    name = newName;
  }

  /**
   * Returns name of this vertex.
   *
   * @return vertex name in String form
   */
  public String getName() {
    return name;
  }
}
