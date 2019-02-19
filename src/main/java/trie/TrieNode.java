package trie;

import java.util.HashSet;

/**
 * @author vx5
 *
 *         A node inside a Trie, that stores a particular character, and
 *         connects to successive TrieNodes in the Trie.
 */
public class TrieNode {
  // Stores the node's character
  private char nodeChar;
  // Stores the node's children
  private HashSet<TrieNode> children;
  // Stores whether or not the node is terminal, which is to say, whether the
  // node represents the end of a word (note that it may still have further
  // children)
  private boolean terminal;

  /**
   * Constructor that creates a TrieNode without a character
   */
  public TrieNode() {
    children = new HashSet<TrieNode>();
  }

  /**
   * Constructor that creates a TrieNode with a given character
   *
   * @param c character to be stored in TrieNode
   */
  public TrieNode(char c) {
    nodeChar = c;
    children = new HashSet<TrieNode>();
  }

  /**
   * Returns the character stored in this TrieNode
   *
   * @return the character stored in this TrieNode
   */
  public char getChar() {
    return nodeChar;
  }

  /**
   * Adds another TrieNode as a child of this node
   *
   * @param n
   */
  public void addChild(TrieNode n) {
    children.add(n);
  }

  /**
   * Returns the children of this TrieNode
   *
   * @return a set of all the TrieNode children of this node
   */
  public HashSet<TrieNode> getChildren() {
    return children;
  }

  /**
   * Signals that a word does in fact end at this TrieNode
   */
  public void makeTerminal() {
    terminal = true;
  }

  /**
   * Checks whether this TrieNode represents the end of a word in the Trie
   *
   * @return a boolean that represents whether this TrieNode represents the end
   *         of a word in the Trie
   */
  public boolean isTerminal() {
    return terminal;
  }

}
