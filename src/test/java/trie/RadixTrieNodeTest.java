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
  // Stores RadixTrieNode used in various tests
  private RadixTrieNode r;

  @Test
  public void testConstruction() {
    // Tests for non-null construction
    RadixTrieNode node = new RadixTrieNode("hello");
    assertNotNull(node);
  }

  /**
   * addChild() and getChildren() are tested jointly since each is required to
   * best test the other
   */
  @Test
  public void testAddGetChildren() {
    // Test for initial no-child state
    assertEquals(r.getChildren().size(), 0);
    // Add children, test for their presence with getChildren()
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
    // Tests for proper contents, access
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
    // Tests ability to make nodes terminal, not terminal
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
    // Tests for completely off word
    assertFalse(r.contains("hello"));
    // Tests for non-word node
    assertFalse(r.contains("app"));
    // Tests for original words
    assertTrue(r.contains("apple"));
    // Test for words on their way to proper word
    assertFalse(r.contains("applesa"));
    // Test for word at end of change
    assertTrue(r.contains("applesauce"));
    // Test for overshooting word
    assertFalse(r.contains("applesauces"));
    // Test for words on branches
    assertTrue(r.contains("appliance"));
    // Test for words partially on branch
    assertFalse(r.contains("applian"));
    assertFalse(r.contains("appliances"));
  }

  @Test
  public void testAddWord() {
    // Builds complex sub-RadixTrie with multiple, layered branch points
    r.addWord("app");
    r.addWord("app");
    r.addWord("applesauce");
    r.addWord("approach");
    r.addWord("appliance");
    // Tests for preservation of contents through change, and addition of
    // children
    assertEquals(r.getChildren().size(), 2);
    assertEquals(r.getContents().size(), 3);
    // Test for proper children, contents of r's children
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
    // Initializes instance field
    r = new RadixTrieNode("apple");
  }

  @After
  public void tearDown() {
    // Clears instance field
    r = null;
  }

}
