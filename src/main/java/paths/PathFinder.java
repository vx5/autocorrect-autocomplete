package paths;

import java.util.ArrayList;

public interface PathFinder {
  public ArrayList<PathNode> getPath(String startVert, String endVert)
      throws Exception;
}
