package paths;

public interface PathEdge {

  String getId();

  PathNode getOtherNode(PathNode givenNode);
}
