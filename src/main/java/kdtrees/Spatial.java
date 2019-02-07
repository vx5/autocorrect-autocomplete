package kdtrees;

/**
 * Interface that defines the type of objects that can be placed into a k-d
 * tree. The only requirements are that such objects can return their
 * coordinates and some form of ID.
 *
 * @author vx5
 */
public interface Spatial {
  /**
   * Returns the coordinates of the Spatial.
   *
   * @return a float array of the Spatial's coordinates in space
   */
  float[] getCoordinates();

  /**
   * Returns the Spatial's identifying String.
   *
   * @return a String identifier of the individual Spatial
   */
  String getID();
}
