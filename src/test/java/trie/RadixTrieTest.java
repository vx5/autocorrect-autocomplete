package trie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RadixTrieTest {
  private RadixTrie t;

  @Test
  public void testConstruction() {
    RadixTrie r = new RadixTrie();
    assertNotNull(r);
  }

  @Test
  public void testAddContains() {
    t.add("apple");
    t.add("orange");
    t.add("appliance");
    t.add("app");
    t.add("applesauce");
    t.add("omron");
    t.add("oranges");
    assertTrue(t.contains("apple"));
    assertTrue(t.contains("orange"));
    assertTrue(t.contains("appliance"));
    assertTrue(t.contains("app"));
    assertTrue(t.contains("applesauce"));
    assertTrue(t.contains("omron"));
    assertTrue(t.contains("oranges"));
  }

  @Test
  public void testGetPrefixEnds() {
    t.add("apple");
    t.add("orange");
    t.add("appliance");
    t.add("app");
    t.add("applesauce");
    t.add("appletart");
    t.add("omron");
    t.add("oranges");
    assertEquals(t.getPrefixEnds("a").size(), 5);
    assertEquals(t.getPrefixEnds("o").size(), 3);
    assertEquals(t.getPrefixEnds("b").size(), 0);
    assertEquals(t.getPrefixEnds("ap").size(), 5);
    assertEquals(t.getPrefixEnds("app").size(), 4);
    assertEquals(t.getPrefixEnds("appl").size(), 4);
    assertEquals(t.getPrefixEnds("apple").size(), 2);
    assertEquals(t.getPrefixEnds("applecore").size(), 0);
    assertEquals(t.getPrefixEnds("appli").size(), 1);
    assertEquals(t.getPrefixEnds("ore").size(), 0);
  }

  @Before
  public void setUp() {
    t = new RadixTrie();
  }

  @After
  public void tearDown() {
    t = null;
  }
}
