package kdtrees;

/**
 * Element of a KDTree that stores a valid object (one that implements the
 * Spatial interface) as well as its position in the KDTree and the dimension
 * used to sort it.
 *
 * @author vx5
 */
public class KDNode {
  // Stores the main object being held in the KDNode overall
  private Spatial mainObj;
  // Stores the dimension along which this node was cut
  private int dimension;
  // Stores the node's children
  // lowChild is the node with a lower coordinate value in the
  // specified dimension
  private KDNode lowChild;
  // highChild is the node with a higher coordinate value in that dimension
  private KDNode highChild;

  /**
   * Constructs a KDNode.
   *
   * @param nodeObj       Object that implements Spatial represented by node
   * @param nodeDimension dimension through which this node is the median
   */
  public KDNode(Spatial nodeObj, int nodeDimension) {
    mainObj = nodeObj;
    dimension = nodeDimension;
    // Initializes both children to null as a signal that the child
    // does not exist
    lowChild = null;
    highChild = null;
  }

  /**
   * Returns the Object represented by and stored in this node.
   *
   * @return Spatial Object represented by this node
   */
  public Spatial getNodeObject() {
    return mainObj;
  }

  /**
   * Returns the dimension through which this node is at the median.
   *
   * @return the dimension through which this node is the median
   */
  public int getDimension() {
    return dimension;
  }

  /**
   * Links a child node to this node.
   *
   * @param child     KDNode being linked as a child to this node
   * @param childType the type of child ("below" or "above") being linked
   */
  public void setChild(KDNode child, int childType) {
    // Checks for childType, sets appropriate child
    if (childType == Constants.BELOW_CHILD) {
      lowChild = child;
    } else {
      highChild = child;
    }
  }

  /**
   * Gets this node's "below" or "above" child, depending on the input.
   *
   * @param childType the type of child to be returned
   * @return this node's child or null if specified child does not exist
   */
  public KDNode getChild(int childType) {
    // Checks for childType, sets appropriate child
    if (childType == Constants.BELOW_CHILD) {
      return lowChild;
    } else {
      return highChild;
    }
  }

}
