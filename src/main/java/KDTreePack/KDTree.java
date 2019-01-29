package KDTreePack;

import java.util.ArrayList;
import java.util.Arrays;

public class KDTree {
	// Stores the root node
	private KDNode root;
	// Stores number of total dimensions in KDTree
	private int dimensions;
	
	// Builds KDTree from array of Spatial objects
	public void build(Spatial[] starList) {
		// Obtains number of dimensions
		dimensions = starList[0].getCoordinates().length;
		// Specifies index bounds
		int lowID = 0;
		int highID = starList.length - 1;
		// Sorts starList based on comparator that uses lowest dimension
		Arrays.sort(starList, new KDComparator(0));
		// Calculates median, pickers higher value if two possible medians
		int medianID = (highID + lowID) / 2;
		if ((highID - lowID + 1) % 2 == 0) {
			medianID++;
		}
		// Creates root node based on median, lowest dimension
		root = new KDNode(starList[medianID], 0);
		// Iterates on Spatials of value below, if possible 
		if (medianID > lowID) {
			this.buildHelper(root, starList, lowID, medianID - 1, 
					Constants.BELOW_CHILD);
		}
		// Iterates on Spatials of value above, if possible
		if (medianID < highID) {
			this.buildHelper(root, starList, medianID + 1, highID,
					Constants.ABOVE_CHILD);
		}	
	}
	
	private void buildHelper(KDNode subTreeRoot, Spatial[] starList, 
			int lowID, int highID, int childType) {
		// Calculates new dimension used for sorting
		int newDimension = (subTreeRoot.getDimension() + 1) % dimensions;
		// Creates new comparator based on current dimension
		KDComparator c = new KDComparator(newDimension);
		// Sorts section starList based on new comparator
		// +1 used for inclusiveness purposes
		Arrays.sort(starList, lowID, highID + 1, c);
		// Calculates median, pickers higher value if two possible medians
		int medianID = (highID + lowID) / 2;
		if ((highID - lowID + 1) % 2 == 0) {
			medianID++;
		}
		// Creates new node to act as child
		KDNode newNode = new KDNode(starList[medianID], newDimension);
		// Sets new node as child of current parent node
		subTreeRoot.setChild(newNode, childType);
		// Iterates on Spatials of value below, if possible 
		if (medianID > lowID) {
			this.buildHelper(newNode, starList, lowID, medianID - 1, 
					Constants.BELOW_CHILD);
		}
		// Iterates on Spatials of value above, if possible
		if (medianID < highID) {
			this.buildHelper(newNode, starList, medianID + 1, highID,
					Constants.ABOVE_CHILD);
		}		
	}
	
	public String[] getNeighbors(int numNeighbors, float[] centerCoords) {
		// Creates ArrayList that stores all neighbor nodes
		ArrayList<KDNode> neighbors = new ArrayList<KDNode>();
		// Stores current furthest node
		KDNode furthestNode;
		// 
		
		return null;
	}
	
	public void neighbHelper(int numNeighbors, float[] centerCoords, 
			ArrayList<KDNode> neighbors, KDNode furthestNode, KDNode target) {
		//
		float centerDtarget = this.calcStraightDist(centerCoords, 
				target.getNodeObject().getCoordinates());
		//
		float currentFarDist = this.calcStraightDist(centerCoords, 
				furthestNode.getNodeObject().getCoordinates());
		//
		if (centerDtarget <  currentFarDist) {
			neighbors.remove(furthestNode);
			neighbors.add(target);
		}
		//
		else if (neighbors.size() < numNeighbors) {
			neighbors.add(target);
			// Correct furthestNode
			furthestNode = target;
		}
		//
		int relevantD = target.getDimension();
		//
		if (centerCoords[relevantD] == 
				target.getNodeObject().getCoordinates()[relevantD]) {
			KDNode belowChild = target.getChild(Constants.BELOW_CHILD);
			//
			if (belowChild != null) {
				neighbHelper(numNeighbors, centerCoords, neighbors, 
						furthestNode, belowChild);
			}
			//
			KDNode aboveChild = target.getChild(Constants.ABOVE_CHILD);
			//
			if (aboveChild != null) {
				neighbHelper(numNeighbors, centerCoords, neighbors, 
						furthestNode, aboveChild);
			}			
		}
		//
		else {
			//
			int childTypeToRecur = Constants.ABOVE_CHILD;
			//
			if (centerCoords[relevantD] > 
					target.getNodeObject().getCoordinates()[relevantD]) {
				//
				childTypeToRecur = Constants.BELOW_CHILD;
			}
			//
			KDNode childToRecur = target.getChild(childTypeToRecur);
			//
			if (childToRecur != null) {
				neighbHelper(numNeighbors, centerCoords, neighbors, 
						furthestNode, childToRecur);
			}
			// Checks for case of checking other child
			if (this.calcStraightDist(target.getNodeObject().getCoordinates(), 
					furthestNode.getNodeObject().getCoordinates()) > 
					Math.abs(centerCoords[relevantD] - 
							target.getNodeObject().getCoordinates()[relevantD])) {
				//
				int otherChildType = (childTypeToRecur + 1) % 2;
				//
				KDNode otherChild = target.getChild(otherChildType);
				//
				if (otherChild != null) {
					neighbHelper(numNeighbors, centerCoords, neighbors, 
							furthestNode, otherChild);
				}
				
			}
			
		}
	}
	
	public float calcStraightDist(float[] k1Coords, float[] k2Coords) {
		int counter = 0;
		//
		float currentVal = 0;
		//
		while (counter < dimensions) {
			// 
			float nextDif = Math.abs(k1Coords[counter] - k2Coords[counter]);
			//
			double currentValD = (double) currentVal;
			double nextDifD = (double) nextDif;
			//
			currentVal = (float) Math.sqrt(Math.pow(currentValD, 2) + 
					Math.pow(nextDifD, 2));
		}
		//
		return currentVal;
	}
 
	
	public String[] searchRadius(float radius, float[] centerCoords) {
		return null;
	}
	
	public KDNode getRoot() {
		return root;
	}
}
