package paths;

import java.util.HashSet;

public interface PathDbOp {

  public HashSet<PathNode> getNeighbors(PathNode fromNode);

  public boolean validNeighbors(PathNode from, PathNode to);
}
