package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class InputValidityExceptionTest {

  @Test
  public void testConstruction() {
    // Tests simple construction with method
    InputValidityException e = new InputValidityException("Message");
    assertNotNull(e);
  }
}
