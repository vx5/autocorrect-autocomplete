package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcCoordinatorTest {
  // Stores instance of AcCoordinator to be used for tests
  private AcCoordinator c;

  @Test
  public void testConstruction() {
    // Checks for valid construction
    assertNotNull(new AcCoordinator());
  }

  /**
   * Tests addOp() and getOp() methods together, since they rely on one another
   * for verification
   */
  @Test
  public void testAddGetOp() {
    // Creates and adds instance of AcOperator
    try {
      AcOperator o = new AcOperator();
      c.addOp(o);
      // Checks that AcOperator is accessible via getOp() method
      assertEquals(c.getOp(0), o);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      fail("No exception expected");
    }
  }

  @Before
  public void setUp() {
    // Initializes AcCoordinator
    c = new AcCoordinator();
  }

  @After
  public void tearDown() {
    // Clears AcCoordinator instance variable
    c = null;
  }

}
