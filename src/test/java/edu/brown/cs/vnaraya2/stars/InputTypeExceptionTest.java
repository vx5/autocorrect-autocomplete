package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class InputTypeExceptionTest {

  @Test
  public void testConstruction() {
    // Tests simple construction with message
    InputTypeException e = new InputTypeException("Message");
    assertNotNull(e);
  }
}
