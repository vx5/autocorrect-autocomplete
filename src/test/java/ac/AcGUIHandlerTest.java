package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcGUIHandlerTest {
  private AcGUIHandler ah;

  @Test
  public void testConstruction() {
    AcCoordinator ac = new AcCoordinator();
    ac.addOp(new AcOperator());
    AcGUIHandler ahConstruct = new AcGUIHandler(ac);
    assertNotNull(ahConstruct);
  }

  @Test
  public void testGetMainMap() {
    Map<String, Object> m = ah.getMainMap();
    assertEquals(m.get("mainErrorStr"), null);
    assertEquals(m.get("oneRow"), "");
    assertEquals(m.get("twoRow"), "");
    assertEquals(m.get("threeRow"), "");
    assertEquals(m.get("fourRow"), "");
    assertEquals(m.get("fiveRow"), "");
  }

  @Test
  public void testGetSetMap() {
    Map<String, Object> m = ah.getSetMap();
    assertEquals(m.get("win"),
        "[Message will display here after you attempt to change "
            + "the settings]");
    assertEquals(m.get("err"), "");
    assertEquals(m.get("corpora").getClass(), ArrayList.class);
  }

  @Test
  public void testGetSettings() {
    Map<String, Object> m = ah.getSettings();
    assertEquals(m.get("prefixVal"), "off");
    assertEquals(m.get("whitespaceVal"), "off");
    assertEquals(m.get("smartVal"), "off");
    assertEquals(m.get("ledVal"), "0");
  }

  @Test
  public void testCorrect() {
    try {
      ah.correct("hello");
    } catch (Exception e) {
      fail("Failure of testCorrect()");
    }
  }

  @Test
  public void testCorrectExceptionMsg() {
    AcCoordinator ac = new AcCoordinator();
    ac.addOp(new AcOperator());
    AcGUIHandler ahNew = new AcGUIHandler(ac);
    try {
      ahNew.correct("hello");
    } catch (Exception e) {
      assertEquals(e.getMessage(),
          "Please load a corpus before typing — you can load a corpus"
              + " by clicking <i>settings</i> or via the REPL");
    }
  }

  // Can't test fixSettings directly, QueryParamsMap does not have
  // usable, accessible constructor

  @Before
  public void setUp() {
    AcCoordinator ac = new AcCoordinator();
    ac.addOp(new AcOperator());
    ah = new AcGUIHandler(ac);
    //
    try {
      ac.getOp(0).addCorpus("data/autocorrect/sherlock.txt");
    } catch (Exception e) {
      fail("Could not load \"sherlock.txt\"");
    }
  }

  @After
  public void tearDown() {
    ah = null;
  }

}
