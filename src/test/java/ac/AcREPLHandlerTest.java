package ac;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AcREPLHandlerTest {

  @Test
  public void testConstruction() {
    AcREPLHandler a = new AcREPLHandler(new AcCoordinator());
    assertNotNull(a);
  }

}
