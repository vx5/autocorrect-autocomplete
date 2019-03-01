package ac;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AcREPLHandlerTest {

  @Test
  public void testConstruction() {
    // Simply test for non-null construction
    AcREPLHandler a = new AcREPLHandler(new AcCoordinator());
    assertNotNull(a);
  }

}
