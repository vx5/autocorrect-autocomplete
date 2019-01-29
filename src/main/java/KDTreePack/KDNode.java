package KDTreePack;

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
	
	public KDNode(Spatial nodeObj, int nodeDimension) {
		mainObj = nodeObj;
		dimension = nodeDimension;
		lowChild = null;
		highChild= null;
	}
	
	public Spatial getNodeObject() {
		// Returns primary node object
		return mainObj;
	}
	
	public int getDimension() {
		// Returns this node's cutting dimension
		return dimension;
	}
	
	public void setChild(KDNode child, int childType) {
		// Checks for childType, sets appropriate child
		if (childType == Constants.BELOW_CHILD) {
			lowChild = child;
		}
		else {
			highChild = child;
		}
	}
	
	public KDNode getChild(int childType) {
		// Checks for childType, sets appropriate child
		if (childType == Constants.BELOW_CHILD) {
			return lowChild;
		}
		else {
			return highChild;
		}
	}
	
	public KDNode getLowChild() {
		// Returns lowChild 
		return lowChild;
	}
	
	public KDNode getHighChild() {
		// Returns highChild 
		return highChild;
	}
}
