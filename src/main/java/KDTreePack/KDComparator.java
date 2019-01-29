package KDTreePack;

import java.util.Comparator;

public class KDComparator implements Comparator<Spatial> {
	// Stores dimension being used for comparison
	private int dimension;
	
	public KDComparator(int compDimension) {
		dimension = compDimension;
	}

	@Override
	public int compare(Spatial o1, Spatial o2) {
		if (o1.getCoordinates()[dimension] < 
				o2.getCoordinates()[dimension]) {
			return -1;
		}
		else {
			return 1;
		}
	}
}
