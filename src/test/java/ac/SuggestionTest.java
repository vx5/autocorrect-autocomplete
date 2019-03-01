package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SuggestionTest {
  // Stores instances of suggestion
  private Suggestion s;

  @Test
  public void testConstruction() {
    // Test for non-null construction
    Suggestion s1 = new Suggestion("wordOne");
    assertNotNull(s1);
  }

  @Test
  public void testGetFirstWord() {
    assertEquals(s.getFirstWord(), "wordOne");
  }

  /**
   * Methods to set and get second word are tested jointly, as each is needed to
   * evaluate the other
   */
  @Test
  public void testSetGetSecondWord() {
    s.setSecondWord("wordTwo");
    assertEquals(s.getSecondWord(), "wordTwo");
  }

  @Before
  public void setUp() {
    // Initializes new Suggestion with default first word
    s = new Suggestion("wordOne");
  }

  @After
  public void tearDown() {
    // Clears instance variable
    s = null;
  }
}
