package kdtrees;

import java.util.Comparator;

/**
 * Comparator that compares KDNodes according to how close they are to some
 * defined center coordinates.
 *
 * @author vx5
 */
public class SpatialDistanceComparator implements Comparator<KDNode> {
  // Stores the coordinates each KDNode is compared to
  private float[] center;

  /**
   * Constructor that takes in and sets the center coordinates that distance
   * will be calculated from.
   *
   * @param compareCoords center coordinates to be used for distance
   *                      calculations
   */
  public SpatialDistanceComparator(float[] compareCoords) {
    center = compareCoords;
  }

  @Override
  public int compare(KDNode o1, KDNode o2) {
    // Obtain KDNodes' coordinates
    float[] coord1 = o1.getNodeObject().getCoordinates();
    float[] coord2 = o2.getNodeObject().getCoordinates();
    // Calculate distances of each node from the comparison
    // coordinates
    float d1 = KDTree.calcStraightDist(coord1, center);
    float d2 = KDTree.calcStraightDist(coord2, center);
    // Compare distance, return values appropriately
    if (d1 < d2) {
      return -1;
    } else {
      return 1;
    }
  }
}
