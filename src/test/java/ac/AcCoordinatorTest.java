package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcCoordinatorTest {
  private AcCoordinator c;

  @Test
  public void testConstruction() {
    assertNotNull(new AcCoordinator());
  }

  @Test
  public void testAddGetOp() {
    AcOperator o = new AcOperator();
    c.addOp(o);
    assertEquals(c.getOp(0), o);
  }

  @Before
  public void setUp() {
    c = new AcCoordinator();
  }

  @After
  public void tearDown() {
    c = null;
  }

}
