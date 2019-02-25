package paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

public class Dijkstra {

  private Dijkstra() {
  }

  public static ArrayList<PathNode> findPath(PathDbOp op, String fromName,
      String toName) throws Exception {
    // Stores node decorations
    HashMap<String, DijkstraNode> decor = new HashMap<String, DijkstraNode>();
    // Initializes PriorityQueue
    PriorityQueue<DijkstraNode> pq = new PriorityQueue<DijkstraNode>(10,
        new DistanceComparator());
    // Initializes first node
    DijkstraNode firstNode = new DijkstraNode(fromName, 0, null, null);
    decor.put(fromName, firstNode);
    pq.add(firstNode);
    // Initializes what should be the final node
    DijkstraNode finalNode = null;
    // Dijkstra's algorithm
    while (pq.size() != 0) {
      // Checks if next node is terminal node
      if (pq.peek().getName().contentEquals(toName)) {
        // If so, end the loop
        finalNode = pq.poll();
        break;
      }
      // Accesses current node
      DijkstraNode currNode = pq.poll();
      // Sets current node to visited
      decor.get(currNode.getName()).setVisited(true);
      // Accesses neighbor nodes
      HashSet<PathNode> neighbors = op.getNeighbors(currNode);
      // Prepares to iterate through all given neighbors
      Iterator<PathNode> i = neighbors.iterator();
      while (i.hasNext()) {
        // In this case, we know we are receiving DijkstraNodes
        DijkstraNode neighbor = (DijkstraNode) i.next();
        // If node is completely new
        if (!decor.keySet().contains(neighbor.getName())
            && op.validNeighbors(currNode, neighbor)) {
          // Add node to the decoration map as is
          decor.put(neighbor.getName(), neighbor);
          // If node already has attributes, and is not visited
        } else if (op.validNeighbors(currNode, neighbor)
            && !decor.get(neighbor.getName()).visited()) {
          // Check whether new distance is better
          if (neighbor.getDist() < decor.get(neighbor.getName()).getDist()) {
            // Update the node
            decor.put(neighbor.getName(), neighbor);
          }
        }
      }
    }
    // Throws Exception if final node not found
    if (finalNode == null) {
      throw new Exception("end node not found");
    }
    // If not, begin process of working backward through nodes
    ArrayList<PathNode> path = new ArrayList<PathNode>();
    // Obtain the end node
    path.add(decor.get(toName));
    // Traces back through graph
    while (!path.get(0).getName().contentEquals(fromName)) {
      PathNode prevNode = decor.get(path.get(0).getPrevName());
      path.set(0, prevNode);
    }
    // Returns ending path
    return path;
  }
}
