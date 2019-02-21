package trie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RadixTrieTest {
  // Stores RadixTrie instance for below tests
  private RadixTrie t;

  @Test
  public void testConstruction() {
    // Test for non-null construction
    RadixTrie r = new RadixTrie();
    assertNotNull(r);
  }

  @Test
  public void testAddContains() {
    // Constructs complex RadixTrie, with multiple branch points at the root and
    // in lower layers of the RadixTrie
    t.add("apple");
    t.add("orange");
    t.add("appliance");
    t.add("app");
    t.add("applesauce");
    t.add("omron");
    t.add("oranges");
    // Tests for words that requires deeper dives
    assertTrue(t.contains("apple"));
    assertTrue(t.contains("orange"));
    // Test for words that require proper side-branches
    assertTrue(t.contains("appliance"));
    // Test for words that are terminal at their end node, but still have
    // successive nodes
    assertTrue(t.contains("app"));
    // Tests for nodes that require complete deep dives (all the way to a
    // terminal leaf node)
    assertTrue(t.contains("applesauce"));
    // Tests for presence of above cases on the other path from the root
    assertTrue(t.contains("omron"));
    assertTrue(t.contains("oranges"));
  }

  @Test
  public void testGetPrefixEnds() {
    // Constructs complex RadixTrie, with multiple branch points at the root and
    // in lower layers of the RadixTrie
    t.add("apple");
    t.add("orange");
    t.add("appliance");
    t.add("app");
    t.add("applesauce");
    t.add("appletart");
    t.add("omron");
    t.add("oranges");
    // Test for root-adjacent non-word results
    assertEquals(t.getPrefixEnds("a").size(), 5);
    assertEquals(t.getPrefixEnds("o").size(), 3);
    // Tests for results for word not on path from root
    assertEquals(t.getPrefixEnds("b").size(), 0);
    // Test for multi-character non-word results
    assertEquals(t.getPrefixEnds("ap").size(), 5);
    // Test for terminal but intermediate node results
    assertEquals(t.getPrefixEnds("app").size(), 4);
    // Test for results for word past an intermediate node
    assertEquals(t.getPrefixEnds("appl").size(), 4);
    assertEquals(t.getPrefixEnds("apple").size(), 2);
    // Tests for results for word not on path (past a leaf node)
    assertEquals(t.getPrefixEnds("applecore").size(), 0);
    // Test for results for word partially into terminal leaf node
    assertEquals(t.getPrefixEnds("appli").size(), 1);
    // Tests for results for word not on path (diverge in the middle of node)
    assertEquals(t.getPrefixEnds("ore").size(), 0);
  }

  @Before
  public void setUp() {
    // Initialize instance field
    t = new RadixTrie();
  }

  @After
  public void tearDown() {
    // Clear instance field
    t = null;
  }
}
