package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcOperatorTest {
  // Stores instance of AcOperator for tests
  private AcOperator o;

  @Test
  public void testConstruction() {
    AcOperator x = new AcOperator();
    // Tests that construction yields non-null object
    assertNotNull(x);
  }

  @Test
  public void testReset() {
    // Tests that reset() is properly called and that it properly resets the
    // corpora storage (as designed)
    o.reset();
    assertEquals(o.getCorpuses().size(), 0);
  }

  @Test
  public void testGetCorpuses() {
    // Tests that getCorpuses() correctly returns filepaths of corpora currently
    // in use
    assertEquals(o.getCorpuses().size(), 1);
    assertEquals(o.getCorpuses().get(0), "data/autocorrect/sherlock.txt");
  }

  /**
   * Jointly tests the status accessors and mutators, as they rely on one
   * another for the purpose of proper verification
   */
  @Test
  public void testGetSetStatuses() {
    assertEquals(o.getPrefixStatus(), "off");
    assertEquals(o.getWsStatus(), "off");
    assertEquals(o.getSmartStatus(), "off");
    assertEquals(o.getLedSetting(), 0);
    o.setPrefixStatus(true);
    o.setWsStatus(true);
    o.setSmartStatus(true);
    o.setLedSetting(1);
    assertEquals(o.getPrefixStatus(), "on");
    assertEquals(o.getWsStatus(), "on");
    assertEquals(o.getSmartStatus(), "on");
    assertEquals(o.getLedSetting(), 1);
  }

  @Test
  public void testAddCorpus() {
    try {
      o.addCorpus("data/autocorrect/great_expectations.txt");
      assertEquals(o.getCorpuses().get(1),
          "data/autocorrect/great_expectations.txt");
    } catch (Exception e) {
      fail("Issue reading great_expectations.txt");
    }
  }

  @Test
  public void testAddCorpusExceptionMsg() {
    String filepath = "data/autocorrect/sherlock.txt";
    try {
      o.addCorpus(filepath);
    } catch (Exception e) {
      assertEquals(e.getMessage(),
          "file " + filepath + " has already been loaded to corpora");
    }
    filepath = "data/autocorrect/shirlox.txt";
    try {
      o.addCorpus(filepath);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "file " + filepath + " not found");
    }
  }

  // Largely delegated away to system tests
  @Test
  public void testAcOneWordSeq() {
    String[] seq = {
        "the"
    };
    try {
      LinkedList<String> results = o.ac(seq);
      assertEquals(results.size(), 1);
      assertEquals(results.get(0), "the");
    } catch (Exception e) {
      fail("Exception was thrown");
    }
  }

  @Test
  public void testAcMultiWordSeq() {
    String[] seq = {
        "the", "red"
    };
    try {
      LinkedList<String> results = o.ac(seq);
      assertEquals(results.size(), 1);
      assertEquals(results.get(0), "the red");
    } catch (Exception e) {
      fail("Exception was thrown");
    }
  }

  @Test
  public void testAcExceptionMsg() {
    String[] seq = {
        "one", "two", "three"
    };
    AcOperator oNew = new AcOperator();
    try {
      oNew.ac(seq);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "no corpus has been loaded yet");
    }
  }

  @Before
  public void setUp() {
    o = new AcOperator();
    try {
      o.addCorpus("data/autocorrect/sherlock.txt");
    } catch (Exception e) {
      fail("Could not load \"sherlock.txt\"");
    }
  }

  @After
  public void tearDown() {
    o = null;
  }
}
