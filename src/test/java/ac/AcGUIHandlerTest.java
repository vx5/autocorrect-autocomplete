package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vx5
 *
 *         Tester class for the AcGUIHandler class. Please note that the
 *         fixSettings() method could not be easily tested individually here
 *         because the QueryParamsMap class does not have an easy-to-access and
 *         manipulate construction means.
 */
public class AcGUIHandlerTest {
  // Instance of AcGUIHandler to be used for all tests
  private AcGUIHandler ah;

  @Test
  public void testConstruction() {
    // Initializes AcGUIHandler instance
    AcCoordinator ac = new AcCoordinator();
    ac.addOp(new AcOperator());
    AcGUIHandler ahConstruct = new AcGUIHandler(ac);
    // Tests for valid construction
    assertNotNull(ahConstruct);
  }

  @Test
  public void testGetMainMap() {
    Map<String, Object> m = ah.getMainMap();
    // Checks that all fields designated for "acmain.ftl" template are
    // initialized as expected
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
    // Checks that all fields designated for "acsettings.ftl" template are
    // initialized as expected
    assertEquals(m.get("win"),
        "[Message will display here after you attempt to change "
            + "the settings]");
    assertEquals(m.get("err"), "");
    assertEquals(m.get("corpora").getClass(), ArrayList.class);
  }

  @Test
  public void testGetSettings() {
    Map<String, Object> m = ah.getSettings();
    // Checks that all retrieved settings values for Autocorrect are as expected
    assertEquals(m.get("prefixVal"), "off");
    assertEquals(m.get("whitespaceVal"), "off");
    assertEquals(m.get("smartVal"), "off");
    assertEquals(m.get("ledVal"), "0");
  }

  /**
   * Note that, because of the initialization involved, testing of the actual
   * correct method's output is delegated to the system tests and, in part, to
   * the AcOperatorTest.java file. This test aims merely to ensure that no
   * Exceptions are thrown
   */
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
    // Initializes new AcGUIHandler instance
    AcCoordinator ac = new AcCoordinator();
    ac.addOp(new AcOperator());
    AcGUIHandler ahNew = new AcGUIHandler(ac);
    // Tests that appropriate Exception is thrown when correct() is run without
    // a loaded corpus
    try {
      ahNew.correct("hello");
    } catch (Exception e) {
      assertEquals(e.getMessage(),
          "Please load a corpus before typing — you can load a corpus"
              + " by clicking <i>settings</i> or via the REPL");
    }
  }

  @Before
  public void setUp() {
    // Fully initializes functional AcHUIHandler instance
    AcCoordinator ac = new AcCoordinator();
    ac.addOp(new AcOperator());
    ah = new AcGUIHandler(ac);
    // Loads sherlock.txt corpus as basis for the above tests
    try {
      ac.getOp(0).addCorpus("data/autocorrect/sherlock.txt");
    } catch (Exception e) {
      fail("Could not load \"sherlock.txt\"");
    }
  }

  @After
  public void tearDown() {
    // Clears instance variable for reset
    ah = null;
  }

}
