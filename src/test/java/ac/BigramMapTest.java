package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BigramMapTest {
  private BigramMap b;

  @Test
  public void testConstruction() {
    BigramMap a = new BigramMap();
    assertNotNull(a);
  }

  @Test
  public void testAddZeroProb() {
    ArrayList<String> words = new ArrayList<String>();
    words.add("wordOne");
    words.add("wordTwo");
    b.addSequence(words);
    assertEquals(b.getProb("fakeWordOne", "fakeWordTwo"), 0, 0);
  }

  @Test
  public void testAddNonzeroProb() {
    ArrayList<String> words = new ArrayList<String>();
    words.add("wordOne");
    words.add("wordTwo");
    words.add("wordOne");
    words.add("wordThree");
    b.addSequence(words);
    assertEquals(b.getProb("wordOne", "wordTwo"), 0.5, 0);
  }

  @Before
  public void setUp() {
    b = new BigramMap();
  }

  @After
  public void tearDown() {
    b = null;
  }
}
