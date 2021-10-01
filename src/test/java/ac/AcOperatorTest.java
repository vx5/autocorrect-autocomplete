package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AcOperatorTest {
  // Stores instance of AcOperator for tests
  private AcOperator o;

  @Test
  public void testConstruction() {
    try {
      AcOperator x = new AcOperator();
      // Tests that construction yields non-null object
      assertNotNull(x);
    } catch (Exception e) {
      fail("Exception not expected");
    }

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
  public void testAddWordsCorpus() {
    // Adds new set of words to the AcOperator
    HashSet<String> setWords = new HashSet<String>();
    setWords.add("ant");
    setWords.add("bear");
    setWords.add("cat");
    o.addWordsCorpus(setWords);
    // Tests that corpus has indeed been loaded
    String[] toCorrect = {
        "ant"
    };
    try {
      assertTrue(o.ac(toCorrect).size() > 0);
    } catch (Exception e) {
      fail("Exceptiont thrown");
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

  @Before
  public void setUp() {
    // Initializes AcOperator object, adds the sherlock.txt corpus for the
    // purposes of the above tests
    try {
      o = new AcOperator();
    } catch (Exception e) {
      fail("Could not initialize for tests");
    }
  }

  @After
  public void tearDown() {
    // Clears the instance field for re-initialization
    o = null;
  }
}
