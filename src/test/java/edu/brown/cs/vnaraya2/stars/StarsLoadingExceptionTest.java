package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class StarsLoadingExceptionTest {

  @Test
  public void testConstruction() {
    StarsLoadingException sl = new StarsLoadingException("Message");
    // Tests simple construction with message
    assertNotNull(sl);
  }
}
