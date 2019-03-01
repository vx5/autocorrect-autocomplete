package edu.brown.cs.vnaraya2.common;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ac.AcCoordinator;
import ac.AcOperator;
import edu.brown.cs.vnaraya2.stars.AllStars;
import kdtrees.KDTree;

public class GUITest {

  @Test
  public void testConstruction() {
    // Add operator to AcCoordinator
    AcCoordinator c = new AcCoordinator();
    c.addOp(new AcOperator());
    GUI sg = new GUI(new AllStars(), new KDTree(), c);
    // Tests construction
    assertNotNull(sg);
  }
}
