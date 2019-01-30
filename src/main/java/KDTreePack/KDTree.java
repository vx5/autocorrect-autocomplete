package KDTreePack;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class KDTree {
	// Stores the root node
	private KDNode root;
	// Stores number of total dimensions in KDTree
	private int dimensions;
	// Stores name of Spatial to not be counted
	private String toAvoid;
	
	// Empty constructor
	public KDTree() {
		// Initializes toAvoid variable as null
		toAvoid = null;
	}
	
	// Sets the toAvoid string 
	public void setAvoidName(String avoidName) {
		toAvoid = avoidName.substring(1, avoidName.length() - 1);
	}
	
	// Resets the toAvoid string 
	public void resetAvoidName() {
		toAvoid = null;
	}
	
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
	
	public PriorityQueue<KDNode> getNeighbors(int numNeighbors, float[] centerCoords) {
		// Creates ArrayList that stores all neighbor nodes
		PriorityQueue<KDNode> neighbors = new PriorityQueue<KDNode>(
				numNeighbors, new SpatialDistanceComparator(centerCoords));
		// Passes to helper function, begins with root as target node
		neighbHelper(numNeighbors, centerCoords, neighbors, root);
		// Returns ArrayList of all nearby neighbor nodes
		return neighbors;
	}
	
	public void neighbHelper(int numNeighbors, float[] centerCoords, 
			PriorityQueue<KDNode> neighbors, KDNode target) {
		// If the target is closer than the current farthest node, 
		// add to the list using the below steps:
		// Calculates distance from center to target node
		float centerTargetDist = KDTree.calcStraightDist(centerCoords,
				target.getNodeObject().getCoordinates());
		// Calculates distance from center to farthest node
		float centerFarthestDist;
		// Instantiates farthest node from center
		KDNode farthestNode = null;
		// If neighbors if empty, set the distance to the max
		// distance 
		if (neighbors.isEmpty()) {
			centerFarthestDist = Float.POSITIVE_INFINITY;
		}
		// Otherwise, look through queue for farthest node and its
		// distance
		else {
			// First, identifies farthest node by iterating through queue
			// Creates a farthestNode variable and uses a helper function
			farthestNode = this.backOfQueue(neighbors);
			// Finally calculates distance from center to farthest node 
			centerFarthestDist = KDTree.calcStraightDist(centerCoords,
					farthestNode.getNodeObject().getCoordinates());
		}
		// If not enough neighbors have been found yet, and if 
		// neighbor is not one "to be avoided", add to list
		if (neighbors.size() < numNeighbors) {
			if(toAvoid == null || 
					!target.getNodeObject().getName().contentEquals(toAvoid)) {
				neighbors.add(target);
			}
		}
		// Else if the target is closer than the current farthest node, 
		// and is not one "to be avoided", add to list, removing 
		// farthestNode
		else if (centerTargetDist < centerFarthestDist) {
			if(toAvoid == null || 
					!target.getNodeObject().getName().contentEquals(toAvoid)) {
				// So long as neighbors is not empty, remove farthest node 
				if (!neighbors.isEmpty()) {
					neighbors.remove(farthestNode);
				}
				neighbors.add(target);
			}
		}
		// 
		//
		// RECURRING STEPS
		//
		//
		//
		// Stores the target node's relevant dimension of sorting
		int relDimen = target.getDimension();
		// Stores the type of child that is recurred on, defaults to above
		int childType = Constants.ABOVE_CHILD;
		// Check for recurrence on "below" child instead of default
		// Check if target is "above" center in relevant dimension 
		if (target.getNodeObject().getCoordinates()[relDimen] > 
				centerCoords[relDimen]) {
			// Sets child for recurrence as "below" child 
			childType = Constants.BELOW_CHILD;
		}
		// Check if target has a valid child in given position
		KDNode targetChild = target.getChild(childType);
		if (targetChild != null) {
			// Recurs on below child 
			this.neighbHelper(numNeighbors, centerCoords, neighbors,
					targetChild);
		}	
		// Checks whether recurring on other branch is necessary 
		// Updates distance from center to farthest node after recurs 
		farthestNode = this.backOfQueue(neighbors);
		centerFarthestDist = KDTree.calcStraightDist(centerCoords,
				farthestNode.getNodeObject().getCoordinates());
		// Recurs on other child in two cases: either the straight
		// distance from the center to the farthest node is worse than 
		// the distance to the target node on the relevant dimension
		// OR the neighbors queue is not full 
		if (centerFarthestDist > Math.abs(centerCoords[relDimen] - 
				target.getNodeObject().getCoordinates()[relDimen]) || 
				neighbors.size() < numNeighbors) {
			// Identify the opposite child type of the one used for 
			// recurrence above 
			int otherChildType = (childType + 1) % 2;
			// Recur on child if possible 
			KDNode otherChild = target.getChild(otherChildType);
			if (otherChild != null) {
				this.neighbHelper(numNeighbors, centerCoords, neighbors,
						otherChild);
			}
		}
	}
	
	public KDNode backOfQueue(Queue<KDNode> q) {
		PriorityQueue<KDNode> qCopy = 
				new PriorityQueue<KDNode>(q);
		// Strips elements off front of queue until only last one remains
		while (qCopy.size() > 1) {
			qCopy.remove();
		}
		// Uses the last remaining element as the back of queue
		return qCopy.remove();
	}
	
	public static float calcStraightDist(float[] k1Coords, float[] k2Coords) {
		// Stores counter that iterates through all dimensions
		int counter = 0;
		// Isolates number of dimensions to iterate through 
		int dimensions = k1Coords.length;
		// Stores the current value of the direct diagonal distance between
		// the floats, which iterates through all the dimensions
		float currentVal = 0;
		// Loop that calculates the diagonal, dimension by dimension
		while (counter < dimensions) {
			// Calculates straightline distance through current dimension
			float nextDif = Math.abs(k1Coords[counter] - k2Coords[counter]);
			// Converts straightline distance and current diagonal values
			// to doubles so that power and square root functions can 
			// manipulate them
			double currentValD = (double) currentVal;
			double nextDifD = (double) nextDif;
			// Calculates next diagonal distance based on Pythagorean theorem
			currentVal = (float) Math.sqrt(Math.pow(currentValD, 2) + 
					Math.pow(nextDifD, 2));
			// Iterates counter up through next dimension
			counter++;
		}
		// Returns the final diagonal length
		return currentVal;
	}
 	
	public PriorityQueue<KDNode> searchRadius(float radius, float[] centerCoords) {
		// Initializes the ArrayList used to store all valid nodes 
		PriorityQueue<KDNode> closeNodes = new PriorityQueue<KDNode>(
				10, new SpatialDistanceComparator(centerCoords));
		// Passes to helper method, starts searching at root node
		this.radiusHelper(radius, centerCoords, closeNodes, root);
		// Returns finished ArrayList of valid nodes
		return closeNodes;
	}
	
	public void radiusHelper(float radius, float[] centerCoords, 
			PriorityQueue<KDNode> closeNodes, KDNode target) {
		// Isolates dimension that target node was sorted through
		int dimension = target.getDimension();
		// Checks whether current target node is valid and different 
		// from the Spatial to be avoided
		if (KDTree.calcStraightDist(target.getNodeObject().getCoordinates(), 
				centerCoords) <= radius) {
			if (toAvoid == null ||
					!target.getNodeObject().getName().contentEquals(toAvoid)) {
				// Adds node to the valid node list 
				closeNodes.add(target);			
			}
		}
		// Calculates target and center's coordinates on the given dimension 
		float targetC = target.getNodeObject().getCoordinates()[dimension];
		float centerC = centerCoords[dimension];
		// Checks for case that, in relevant dimension, algorithm should 
		// check on right ("above") side of the target node
		if (targetC <= centerC || targetC - centerC <= radius) {
			KDNode aboveChild = target.getChild(Constants.ABOVE_CHILD);
			// If child node exists, recur on it 
			if (aboveChild != null) {
				this.radiusHelper(radius, centerCoords, closeNodes,
						aboveChild);
			}
		}
		// Checks for case that, in relevant dimension, algorithm should 
		// check on left ("below") side of the target node
		if (targetC >= centerC || centerC - targetC <= radius) {
			KDNode belowChild = target.getChild(Constants.BELOW_CHILD);
			// If child node exists, recur on it 
			if (belowChild != null) {
				this.radiusHelper(radius, centerCoords, closeNodes,
						belowChild);
			}
		}	
	}
}
