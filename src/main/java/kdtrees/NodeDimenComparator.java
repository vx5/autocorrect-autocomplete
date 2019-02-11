package kdtrees;

import java.util.Comparator;

/**
 * Comparator that compares KDNodes according to position in a particular
 * dimension.
 *
 * @author vx5
 */
public class NodeDimenComparator implements Comparator<Spatial> {
  // Stores dimension being used for comparison
  private int dimension;

  /**
   * Constructs a comparator that compares along the input dimension.
   *
   * @param compDimension dimension along which to compare nodes' position
   */
  public NodeDimenComparator(int compDimension) {
    dimension = compDimension;
  }

  @Override
  public int compare(Spatial o1, Spatial o2) {
    if (o1.getCoordinates()[dimension] < o2.getCoordinates()[dimension]) {
      return -1;
    } else {
      return 1;
    }
  }
}
