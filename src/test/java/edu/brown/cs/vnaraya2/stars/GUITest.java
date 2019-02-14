package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ac.AcCoordinator;
import edu.brown.cs.vnaraya2.common.GUI;
import kdtrees.KDTree;

public class GUITest {

  @Test
  public void testConstruction() {
    GUI sg = new GUI(new AllStars(), new KDTree(), new AcCoordinator());
    // Tests construction
    assertNotNull(sg);
  }
}
