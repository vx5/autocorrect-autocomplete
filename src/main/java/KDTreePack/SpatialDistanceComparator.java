package KDTreePack;

import java.util.Comparator;

public class SpatialDistanceComparator implements Comparator<KDNode> {
	// Stores the coordinates each KDNode is compared to 
	private float[] center;
	
	// Constructor initializes the center coordinates each KDNode is 
	// compared to 
	public SpatialDistanceComparator(float[] compareCoords) {
		center = compareCoords;
	}

	@Override
	public int compare(KDNode o1, KDNode o2) {
		// Obtain KDNodes' coordinates
		float[] coord1 = o1.getNodeObject().getCoordinates();
		float[] coord2 = o2.getNodeObject().getCoordinates();
		// Calculate distances of each node from the comparison
		// coordinates
		float d1 = KDTree.calcStraightDist(coord1, center);
		float d2 = KDTree.calcStraightDist(coord2, center);
		// Compare distance, return values appropriately
		if (d1 < d2) {
			return -1;
		}
		else {
			return 1;
		}
	}

}
