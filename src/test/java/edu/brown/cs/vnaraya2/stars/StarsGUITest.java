package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import kdtrees.KDTree;

public class StarsGUITest {

  @Test
  public void testConstruction() {
    StarsGUI sg = new StarsGUI(new AllStars(), new KDTree());
    // Tests construction
    assertNotNull(sg);
  }
}
