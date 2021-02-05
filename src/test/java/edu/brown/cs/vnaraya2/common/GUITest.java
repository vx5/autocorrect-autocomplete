package edu.brown.cs.vnaraya2.common;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import ac.AcCoordinator;
import ac.AcOperator;

public class GUITest {

  @Test
  public void testConstruction() {
    // Add operator to AcCoordinator
    AcCoordinator c = new AcCoordinator();
    c.addOp(new AcOperator());
    GUI sg = new GUI(c);
    // Tests construction
    assertNotNull(sg);
  }
}
