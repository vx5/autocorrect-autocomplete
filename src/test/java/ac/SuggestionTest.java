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
    Suggestion s1 = new Suggestion("wordOne");
    assertNotNull(s1);
  }

  @Test
  public void testGetFirstWord() {
    assertEquals(s.getFirstWord(), "wordOne");
  }

  @Test
  public void testSetGetSecondWord() {
    s.setSecondWord("wordTwo");
    assertEquals(s.getSecondWord(), "wordTwo");
  }

  @Before
  public void setUp() {
    s = new Suggestion("wordOne");
  }

  @After
  public void tearDown() {
    s = null;
  }
}
