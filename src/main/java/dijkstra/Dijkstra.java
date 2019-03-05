package dijkstra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;

import paths.PathFinder;
import paths.PathNode;

/**
 * @author vx5
 *
 * @param <V> the specific subclass of DVertex to be used for this particular
 *        implementation of Dijkstra
 * @param <E> the specific subclass of DEdge to be used for this particular
 *        implementation of Dijkstra
 *
 *        Object responsible for coordination of performing Dijkstra's algorithm
 *        to return a shortest path.
 */
public class Dijkstra<V extends DVertex<V, E>, E extends DEdge<V, E>>
    implements PathFinder {
  // Stores database operator instance
  private DijkstraDbOp<V, E> op;

  /**
   * Constructor that only accepts instance of DijkstraDbOp that allows this
   * class to perform key operations.
   *
   * @param newOp instance of DijkstraDbOp to be used by this Dijkstra class
   */
  public Dijkstra(DijkstraDbOp<V, E> newOp) {
    op = newOp;
  }

  @Override
  public ArrayList<PathNode> getPath(String startId, String endId)
      throws Exception {
    // Stores map of vertex IDs to vertices
    HashMap<String, V> map = new HashMap<String, V>();
    // Stores all visited names
    HashSet<String> visited = new HashSet<String>();
    // PriorityQueue that stores to-be-visited vertices
    PriorityQueue<V> pq = new PriorityQueue<V>();
    // Creates new, first node
    V firstVert = op.makeVertex(startId, 0);
    // Add first node to map
    map.put(startId, firstVert);
    // Add first node to pq
    pq.add(firstVert);
    // Establishes would-be terminal vertex
    V terminal = null;
    // Begins Dijkstra search
    while (pq.size() > 0) {
      // Stores current node
      V currVert = pq.poll();
      // "Visits" the node
      visited.add(currVert.getId());
      // Checks whether node is terminal
      if (currVert.getId().contentEquals(endId)) {
        // If so, store current node and exit search
        terminal = currVert;
        break;
      }
      // Populate the node's outgoing edges
      op.giveNeighbors(currVert);
      // Iterate through all those outgoing edges
      Iterator<E> iter = currVert.getEdges().iterator();
      while (iter.hasNext()) {
        // Get and store edge
        E thisEdge = iter.next();
        // Get vertex on other side of edge
        V neighbor = thisEdge.getOtherNode(currVert);
        // Remove this edge (no longer needs to be checked)
        iter.remove();
        // Check for validity (with outside validity conditions, and
        // check for unvisited node
        if (!op.validNeighbors(currVert, neighbor)
            || visited.contains(neighbor.getId())) {
          // Proceed to next edge
          continue;
        } else if (map.containsKey(neighbor.getId())) {
          // Check for already-stored vertex
          // Obtain the existing vertex
          V existing = map.get(neighbor.getId());
          // If the new option is superior
          if (neighbor.getDist() < existing.getDist()) {
            // Alter the existing node's distance
            existing.setDist(neighbor.getDist());
            // Alter the edge's tail
            thisEdge.setTail(existing);
            // Alter the existing node's best previous edge
            existing.setPrevEdge(thisEdge);
          }
        } else {
          // Otherwise, add new vertex reference to map
          map.put(neighbor.getId(), neighbor);
          // Add the vertex to the priorityQueue
          pq.add(neighbor);
        }
      }
    }
    // If no path found, throw an Exception
    if (terminal == null) {
      throw new Exception("path not found");
    }
    // Otherwise, begin assembling the path
    ArrayList<PathNode> path = new ArrayList<PathNode>();
    // Stores current node in backtrace
    PathNode currNode = terminal;
    // Runs backtrace
    while (currNode.getPrevEdge() != null) {
      path.add(currNode);
      currNode = currNode.getPrevEdge().getOtherNode(terminal);
    }
    // Adds final node
    path.add(currNode);
    // Returns path
    return path;
  }
}
