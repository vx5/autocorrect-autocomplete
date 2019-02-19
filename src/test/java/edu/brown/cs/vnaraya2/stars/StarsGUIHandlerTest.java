package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import kdtrees.KDTree;

public class StarsGUIHandlerTest {

  @Test
  public void testConstruction() {
    StarsGUIHandler b = new StarsGUIHandler(new AllStars(), new KDTree());
    // Tests construction
    assertNotNull(b);
  }

}
