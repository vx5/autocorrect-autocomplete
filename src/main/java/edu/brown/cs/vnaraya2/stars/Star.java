package edu.brown.cs.vnaraya2.stars;

import kdtrees.Spatial;

/**
 * @author vx5
 *
 *         Object that represents a position in space with a given ID and,
 *         possibly, a name.
 */
public class Star implements Spatial {
  // Instance variable that stores ID
  // String class used because it is most generic
  private String id;
  // Instance variable that stores star's name
  private String name;
  // Instance variable that stores coordinates
  private float[] coordinates;

  /**
   * Constructor that builds star from its 3 attributes: its ID, its name, and
   * its coordinates in space.
   *
   * @param starID          identifying String of this Star
   * @param starName        additional name String of this star
   * @param starCoordinates array of coordinates for Star's location
   */
  public Star(String starID, String starName, float[] starCoordinates) {
    id = starID;
    name = starName;
    coordinates = starCoordinates;
  }

  @Override
  public float[] getCoordinates() {
    return coordinates;
  }

  @Override
  public String getID() {
    return id;
  }

  /**
   * Returns the Star's name (not its ID).
   *
   * @return the Star's name
   */
  public String getName() {
    return name;
  }

}
