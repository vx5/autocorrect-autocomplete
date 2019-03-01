package trie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TrieNodeTest {
  // Instance variables for TrieNodes, one in root form, one in standard form
  private TrieNode rootNode;
  private TrieNode normNode;

  @Test
  public void testConstruction() {
    // Tests root TrieNode construction (no character stored)
    rootNode = new TrieNode();
    assertNotNull(rootNode);
    // Tests standard TrieNode construction
    normNode = new TrieNode('a');
    assertNotNull(normNode);
  }

  @Test
  public void testGetCharNorm() {
    // Tests that getChar() returns normNode's stored char
    assertEquals(normNode.getChar(), 'a');
  }

  /**
   * Tests both the makeTerminal() and isTerminal() methods in one test (since
   * the latter best verifies the former)
   */
  @Test
  public void testMakeIsTerminal() {
    // We arbitrarily test the normNode in this case
    assertFalse(normNode.isTerminal());
    // We then make the node terminal
    normNode.makeTerminal();
    // And test that the status has changed appropriately
    assertTrue(normNode.isTerminal());
  }

  /**
   * Tests both addChildren() and getChildren() method in one test (since the
   * latter best verifies the former)
   */
  @Test
  public void testAddGetChildren() {
    // We arbitrarily test the normNode in this case
    // Tests for no children, at first
    assertEquals(normNode.getChildren().size(), 0);
    // Adds two children, keeps instance references
    TrieNode new1 = new TrieNode();
    TrieNode new2 = new TrieNode();
    normNode.addChild(new1);
    normNode.addChild(new2);
    // Checks that exactly those two children are now present
    assertTrue(normNode.getChildren().contains(new1));
    assertTrue(normNode.getChildren().contains(new2));
    assertEquals(normNode.getChildren().size(), 2);
  }

  @Before
  public void setUp() {
    // Tests root TrieNode construction (no character stored)
    rootNode = new TrieNode();
    assertNotNull(rootNode);
    // Tests standard TrieNode construction
    normNode = new TrieNode('a');
    assertNotNull(normNode);
  }

  @After
  public void tearDown() {
    // Clears instance fields for testing
    rootNode = null;
    normNode = null;
  }

}
