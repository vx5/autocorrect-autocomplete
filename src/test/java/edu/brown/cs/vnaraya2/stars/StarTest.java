package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StarTest {
  private Star a;

  @Test
  public void testConstruction() {
    float[] coords = {
        (float) 2.0, (float) 2.0, (float) 2.0
    };
    Star b = new Star("StarID", "StarName", coords);
    assertNotNull(b);
  }

  @Test
  public void testGetCoordinates() {
    float[] coords = a.getCoordinates();
    float delta = 0;
    assertEquals(coords[0], 2.0, delta);
    assertEquals(coords[1], 2.0, delta);
    assertEquals(coords[2], 2.0, delta);
  }

  @Test
  public void testGetID() {
    assertEquals("StarID", a.getID());
  }

  @Test
  public void testGetName() {
    assertEquals("StarName", a.getName());
  }

  @Before
  public void setUp() throws IOException, StarsLoadingException {
    float[] coords = {
        (float) 2.0, (float) 2.0, (float) 2.0
    };
    a = new Star("StarID", "StarName", coords);
  }

  @After
  public void tearDown() {
    a = null;
  }
}
