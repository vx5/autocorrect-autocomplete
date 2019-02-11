package ac;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TrieTest {
  // Test's instance of Trie
  private Trie trie;

  @Test
  public void testConstruction() {
    // Test for non-null construction
    trie = new Trie();
    assertNotNull(trie);
  }

  @Test
  public void testContains() {
    // Test for completely wrong word
    assertFalse(trie.contains("cake"));
    // Test for word that starts in trie
    assertFalse(trie.contains("app"));
    // Test for word that is contained separate
    assertTrue(trie.contains("banana"));
    // Test for word that is contained with following nodes
    assertTrue(trie.contains("apple"));
    // Test for word that is between contained words
    assertFalse(trie.contains("applesau"));
    // Test for word that is terminal through other words
    assertTrue(trie.contains("applesauces"));
  }

  @Test
  public void testGetPrefixEnds() throws Exception {
    // Stored returned HashSet
    HashSet<Suggestion> set = trie.getPrefixEnds("apple");
    // Initializes booleans related to true values
    boolean applesauceFound = false;
    boolean applesaucesFound = false;
    boolean appletopFound = false;
    // Checks for all suggestions
    for (Suggestion s : set) {
      // Store word from the suggestion
      String word = s.getFirstWord();
      // Check that totally wrong words not included
      assertNotEquals(word, "banana");
      // Check that given word not included
      assertNotEquals(word, "apple");
      // Check that partial word not included
      assertNotEquals(word, "applesau");
      // Check that all valid words are included
      if (word.contentEquals("applesauce")) {
        applesauceFound = true;
      }
      if (word.contentEquals("applesauces")) {
        applesaucesFound = true;
      }
      if (word.contentEquals("appletop")) {
        appletopFound = true;
      }
    }
    // Check that all desired words were found
    assertTrue(applesauceFound);
    assertTrue(applesaucesFound);
    assertTrue(appletopFound);
  }

  /**
   * Tests for Exception being thrown for invalid word being passed in.
   *
   * @throws Exception if word that is not part of prefix tree is passed to
   *                   getPrefixEnds()
   */
  @Test(expected = Exception.class)
  public void testGetPrefixEndsException() throws Exception {
    trie.getPrefixEnds("apricot");
  }

  /**
   * Because add() cannot be tested by itself, consider it tested through the
   * below Trie's being tested above
   */
  @Before
  public void setUp() {
    // Initializes new Trie
    trie = new Trie();
    // Adds a few words to Trie
    trie.add("apple");
    trie.add("appliance");
    trie.add("applesauce");
    trie.add("appletop");
    trie.add("applesauces");
    trie.add("banana");
  }

  @After
  public void tearDown() {
    // Resets instance fields
    trie = null;
  }

}
