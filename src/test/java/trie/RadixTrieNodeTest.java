package trie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RadixTrieNodeTest {
  private RadixTrieNode r;

  @Test
  public void testConstruction() {
    RadixTrieNode node = new RadixTrieNode("hello");
    assertNotNull(node);
  }

  @Test
  public void testAddGetChildren() {
    assertEquals(r.getChildren().size(), 0);
    RadixTrieNode a = new RadixTrieNode("a");
    RadixTrieNode b = new RadixTrieNode("b");
    r.addChild(a);
    r.addChild(b);
    HashSet<RadixTrieNode> children = r.getChildren();
    assertEquals(children.size(), 2);
    assertTrue(children.contains(a));
    assertTrue(children.contains(b));
  }

  @Test
  public void testGetContents() {
    ArrayList<Character> contents = r.getContents();
    assertTrue(contents.get(0) == 'a');
    assertTrue(contents.get(1) == 'p');
    assertTrue(contents.get(2) == 'p');
    assertTrue(contents.get(3) == 'l');
    assertTrue(contents.get(4) == 'e');
  }

  @Test
  public void testGetFirstChar() {
    assertTrue(r.getFirstChar() == 'a');
  }

  @Test
  public void testMakeIsTerminal() {
    assertFalse(r.isTerminal());
    r.makeTerminal(true);
    assertTrue(r.isTerminal());
    r.makeTerminal(false);
    assertFalse(r.isTerminal());
  }

  @Test
  public void testContains() {
    r.addWord("applesauce");
    r.addWord("appliance");
    assertFalse(r.contains("hello"));
    assertFalse(r.contains("app"));
    assertTrue(r.contains("apple"));
    assertFalse(r.contains("applesa"));
    assertTrue(r.contains("applesauce"));
    assertFalse(r.contains("applesauces"));
    assertTrue(r.contains("appliance"));
    assertFalse(r.contains("applian"));
    assertFalse(r.contains("appliances"));
  }

  @Test
  public void testAddWord() {
    r.addWord("app");
    r.addWord("app");
    r.addWord("applesauce");
    r.addWord("approach");
    r.addWord("appliance");
    assertEquals(r.getChildren().size(), 2);
    assertEquals(r.getContents().size(), 3);
    for (RadixTrieNode child : r.getChildren()) {
      if (child.getFirstChar() == 'l') {
        assertEquals(child.getChildren().size(), 2);
      } else if (child.getFirstChar() == 'r') {
        assertEquals(child.getChildren().size(), 0);
        assertEquals(child.getContents().size(), 5);
      } else {
        fail("Incorrect children found");
      }
    }
  }

  @Before
  public void setUp() {
    r = new RadixTrieNode("apple");
  }

  @After
  public void tearDown() {
    r = null;
  }

}
