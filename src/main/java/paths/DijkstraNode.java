package paths;

public class DijkstraNode implements PathNode {
  // Instance fields
  private String name;
  private float prevDist;
  private String prevEdgeName;
  private String prevName;
  private boolean visited;

  public DijkstraNode(String dName, float dPrevDist, String dPrevEdgeName,
      String dPrevName) {
    name = dName;
    prevDist = dPrevDist;
    prevEdgeName = dPrevEdgeName;
    prevName = dPrevName;
    visited = false;

  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return name;
  }

  @Override
  public float getDist() {
    // TODO Auto-generated method stub
    return prevDist;
  }

  @Override
  public String getPrevEdgeName() {
    // TODO Auto-generated method stub
    return prevEdgeName;
  }

  @Override
  public String getPrevName() {
    // TODO Auto-generated method stub
    return prevName;
  }

  public boolean visited() {
    return visited;
  }

  public void setVisited(boolean newStatus) {
    visited = newStatus;
  }

}
