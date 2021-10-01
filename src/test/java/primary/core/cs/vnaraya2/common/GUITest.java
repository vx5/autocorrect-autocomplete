package primary.core.cs.vnaraya2.common;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import ac.AcCoordinator;
import ac.AcOperator;
import primary.core.cs.vx5.common.GUI;

public class GUITest {

  @Test
  public void testConstruction() {
    // Add operator to AcCoordinator
    AcCoordinator c = new AcCoordinator();
    try {
      c.addOp(new AcOperator());
    } catch (Exception e) {
      fail("Exception should not be thrown");
    }
    GUI sg = new GUI(c);
    // Tests construction
    assertNotNull(sg);
  }
}
