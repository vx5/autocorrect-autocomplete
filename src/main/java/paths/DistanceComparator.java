package paths;

import java.util.Comparator;

public class DistanceComparator implements Comparator<PathNode> {

  @Override
  public int compare(PathNode o1, PathNode o2) {
    if (o1.getDist() < o2.getDist()) {
      return -1;
    } else {
      return 1;
    }
  }

}
