package edu.brown.cs.vnaraya2.stars;

import java.util.Comparator;

import KDTreePack.KDNode;

public class KDNodeIDNumComparator implements Comparator<KDNode> {

	@Override
	public int compare(KDNode o1, KDNode o2) {
		if (Integer.parseInt(o1.getNodeObject().getID()) < 
				Integer.parseInt(o2.getNodeObject().getID())) {
			return -1;
		}
		else {
			return 1;
		}
	}

}
