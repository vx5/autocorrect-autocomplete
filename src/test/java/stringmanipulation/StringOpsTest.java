package stringmanipulation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringOpsTest {

  @Test
  public void testCleanInput() {
    String output = StringOps.cleanInput("!@The    Kooks");
    assertEquals(output, " the kooks");
  }

  @Test
  public void testRemoveWs() {
    String output = StringOps.removeWs("    ant    ");
    assertEquals(output, "ant");
  }

}
