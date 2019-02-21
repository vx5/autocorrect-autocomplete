package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;

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
    // Tests for initial settings
    assertEquals(o.getPrefixStatus(), "off");
    assertEquals(o.getWsStatus(), "off");
    assertEquals(o.getSmartStatus(), "off");
    assertEquals(o.getLedSetting(), 0);
    // Tests for setting changes
    o.setPrefixStatus(true);
    o.setWsStatus(true);
    o.setSmartStatus(true);
    o.setLedSetting(1);
    // Tests that new setting statuses are returned
    assertEquals(o.getPrefixStatus(), "on");
    assertEquals(o.getWsStatus(), "on");
    assertEquals(o.getSmartStatus(), "on");
    assertEquals(o.getLedSetting(), 1);
  }

  @Test
  public void testAddCorpus() {
    // Test adding a new corpus
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
    // Tests for whether adding a repeat corpus draws an Exception
    String filepath = "data/autocorrect/sherlock.txt";
    try {
      o.addCorpus(filepath);
    } catch (Exception e) {
      assertEquals(e.getMessage(),
          "file " + filepath + " has already been loaded to corpora");
    }
    // Tests for whether attempting to add a nonexistent corpus draws an
    // Exception
    filepath = "data/autocorrect/shirlox.txt";
    try {
      o.addCorpus(filepath);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "file " + filepath + " not found");
    }
  }

  /**
   * Because the cleaning of input (in the handling of punctuation and
   * capitalization) takes place in the GUI and REPL handlers, the only testing
   * to be done here is with the various settings engaged
   */
  @Test
  public void testAcOneWordSeq() {
    // Initializes searches with words that are and are not in the corpus
    String[] seqOne = {
        "the"
    };
    String[] seqTwo = {
        "thisisnotarealword"
    };
    try {
      // Tests that word in corpus is returned as suggestion
      ArrayList<String> results = o.ac(seqOne);
      assertEquals(results.size(), 1);
      assertEquals(results.get(0), "the");
      // Tests that no suggestions are made
      results = o.ac(seqTwo);
      assertEquals(results.size(), 0);
      // Tests for all settings on
      o.setPrefixStatus(true);
      o.setWsStatus(true);
      o.setLedSetting(1);
      results = o.ac(seqOne);
      assertEquals(results.get(0), "the");
      assertEquals(results.get(1), "he");
      assertEquals(results.get(2), "there");
      assertEquals(results.get(3), "she");
      assertEquals(results.get(4), "then");
    } catch (Exception e) {
      fail("Exception was thrown");
    }
  }

  /**
   * Because the cleaning of input (in the handling of punctuation and
   * capitalization) takes place in the GUI and REPL handlers, the only testing
   * to be done here is with the various settings engaged
   */
  @Test
  public void testAcMultiWordSeq() {
    // Initializes sample to try
    String[] seq = {
        "the", "red"
    };
    try {
      // Tests for all settings off
      ArrayList<String> results = o.ac(seq);
      assertEquals(results.size(), 1);
      assertEquals(results.get(0), "the red");
      // Tests for all settings on
      o.setPrefixStatus(true);
      o.setWsStatus(true);
      o.setLedSetting(1);
      results = o.ac(seq);
      assertEquals(results.get(0), "the red");
      assertEquals(results.get(1), "the bed");
      assertEquals(results.get(2), "the rd");
      assertEquals(results.get(3), "the read");
      assertEquals(results.get(4), "the led");
    } catch (Exception e) {
      fail("Exception was thrown");
    }
  }

  @Test
  public void testAcExceptionMsg() {
    // Tests that Exception is thrown when a corpus has not yet been loaded
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
    // Initializes AcOperator object, adds the sherlock.txt corpus for the
    // purposes of the above tests
    o = new AcOperator();
    try {
      o.addCorpus("data/autocorrect/sherlock.txt");
    } catch (Exception e) {
      fail("Could not load \"sherlock.txt\"");
    }
  }

  @After
  public void tearDown() {
    // Clears the instance field for re-initialization
    o = null;
  }
}
