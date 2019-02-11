package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kdtrees.KDTree;

public class StarsGUIHandlerTest {
  private StarsGUIHandler a;

  @Test
  public void testConstruction() {
    StarsGUIHandler b = new StarsGUIHandler(new AllStars(), new KDTree());
    // Tests construction
    assertNotNull(b);
  }

  @Before
  public void setUp() throws IOException, StarsLoadingException {
    a = new StarsGUIHandler(new AllStars(), new KDTree());
  }

  @After
  public void tearDown() {
    a = null;
  }

}
