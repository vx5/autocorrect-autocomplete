package trie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @author vx5
 *
 *         Node within a RadixTrie that stores the relevant "coalesced" nodes by
 *         representing them as a list of characters.
 */
public class RadixTrieNode {
  // Stores the "coalesced" nodes through a list of characters
  private ArrayList<Character> contents;
  // Stores all children of this node
  private HashSet<RadixTrieNode> children;
  // Decoration that represents whether this node is terminal
  private boolean terminal;

  /**
   * Constructor that initializes a node with its "coalesced" nodes, via a
   * string
   *
   * @param str String used as basis for the "coalesced" nodes inside this node
   */
  public RadixTrieNode(String str) {
    // Reads given String into contents
    contents = new ArrayList<Character>();
    for (int i = 0; i < str.length(); i++) {
      contents.add(str.charAt(i));
    }
    // Initializes with no children, and not (necessarily) representing a word
    children = new HashSet<RadixTrieNode>();
    terminal = false;
  }

  /**
   * Adds a given RadixTrieNode as a child of this RadixTrieNode
   *
   * @param newNode RadixTrieNode to be added as a child
   */
  public void addChild(RadixTrieNode newNode) {
    children.add(newNode);
  }

  /**
   * Returns reference to this node's children
   *
   * @return set of RadixTrieNode children of this node
   */
  public HashSet<RadixTrieNode> getChildren() {
    return children;
  }

  /**
   * Returns reference to this node's content characters
   *
   * @return a list of Characters that represent this node's contents
   */
  public ArrayList<Character> getContents() {
    return contents;
  }

  /**
   * Returns the first character, which in essence, represents the first of the
   * "coalesced" nodes stored in this RadixTrieNode
   *
   * @return the first character stored in the contents of this node
   */
  public char getFirstChar() {
    return contents.get(0);
  }

  /**
   * Mutator method that allows the changing of this node's terminal decoration,
   * which is meant to signal whether a word stored in the RadixTrie ends at
   * this node
   *
   * @param newSetting the new state that the terminal decoration should be
   *                   changed to
   */
  public void makeTerminal(boolean newSetting) {
    terminal = newSetting;
  }

  /**
   * Checks whether this RadixTrieNode represents the end of a word in the
   * RadixTrie
   *
   * @return a boolean that represents whether this TrieNode represents the end
   *         of a word in the RadixTrie
   */
  public boolean isTerminal() {
    return terminal;
  }

  /**
   * Adds a word to the RadixTrie as a whole, starting at this particular
   * RadixTrieNode. Note that it is assumed that the word and contents of this
   * node share at least the first character in common
   *
   * @param word String form of the word to be added to the RadixTrie at the
   *             particular location of this RadixTrieNode
   */
  public void addWord(String word) {
    // Identifies whether word is already contained
    if (this.contains(word)) {
      // If so, do not attempt to add the word
      return;
    }
    // Locates soonest divergence point between word and content of this node
    int divergeLoc = this.findDivergence(word);
    // If diverging point is before the end of the word or the full contents of
    // the node, then we must split the current node into a parent and child,
    // and add the new word node as a new child of that parent
    if (divergeLoc < Math.min(word.length(), contents.size())) {
      // Remember that we know that at least the first character is common
      // Creates new continuation node that will become a child of this node
      String continueNodeStr = "";
      // Populates the non-overlapping String content that must be passed to the
      // child
      for (int i = divergeLoc; i < contents.size(); i++) {
        continueNodeStr += contents.get(i);
      }
      // Creates new node
      RadixTrieNode continueNode = new RadixTrieNode(continueNodeStr);
      // Shifts children to new continuation node
      Iterator<RadixTrieNode> childIterator = children.iterator();
      while (childIterator.hasNext()) {
        RadixTrieNode child = childIterator.next();
        continueNode.addChild(child);
        childIterator.remove();
      }
      // Alters terminal decorations accordingly
      if (this.isTerminal()) {
        this.makeTerminal(false);
        continueNode.makeTerminal(true);
      }
      // Creates new diverging node (based on the non-overlapping part of the
      // word)
      RadixTrieNode divergeNode = new RadixTrieNode(word.substring(divergeLoc));
      // Decorates that this new node does represent the end of the word
      divergeNode.makeTerminal(true);
      // Sets the new nodes as the new children
      this.addChild(continueNode);
      this.addChild(divergeNode);
      // Reduces the current contents to what is common between the word and old
      // set of contents
      ArrayList<Character> newContents = new ArrayList<Character>();
      for (int i = 0; i < divergeLoc; i++) {
        newContents.add(contents.get(i));
      }
      contents = newContents;
      // If the word is shorter than the contents (and does not diverge)
    } else if (divergeLoc == word.length()) {
      // If word fits into existing contents
      // Make a new node with the later contents
      String continueNodeStr = "";
      for (int i = divergeLoc; i < contents.size(); i++) {
        continueNodeStr += contents.get(i);
      }
      RadixTrieNode continueNode = new RadixTrieNode(continueNodeStr);
      // Shifts children to new continuation node
      Iterator<RadixTrieNode> childIterator = children.iterator();
      while (childIterator.hasNext()) {
        RadixTrieNode child = childIterator.next();
        continueNode.addChild(child);
        childIterator.remove();
      }
      // Alters terminal decoration accordingly
      if (this.isTerminal()) {
        continueNode.makeTerminal(true);
      }
      this.makeTerminal(true);
      // Adds the continuation node as a child of this node
      this.addChild(continueNode);
      // Reduces the current contents to the overlapping region
      ArrayList<Character> newContents = new ArrayList<Character>();
      for (int i = 0; i < divergeLoc; i++) {
        newContents.add(contents.get(i));
      }
      contents = newContents;
      // If the word is longer than the contents
    } else {
      // Identify leftover String
      String leftoverWord = word.substring(divergeLoc);
      // Iterates through children to look for one that fits the first leftover
      // character
      for (RadixTrieNode child : children) {
        if (child.getFirstChar() == leftoverWord.charAt(0)) {
          child.addWord(leftoverWord);
          return;
        }
      }
      // If no child found, make new node
      RadixTrieNode newChild = new RadixTrieNode(leftoverWord);
      newChild.makeTerminal(true);
      this.addChild(newChild);
    }
  }

  private int findDivergence(String word) {
    // Sets maximum to be used for search of divergence between word and this
    // node's contents
    int maxID = Math.min(word.length(), contents.size());
    // Iterates through word to find divergence
    int iterateID = 1;
    while (iterateID < maxID) {
      if (word.charAt(iterateID) != contents.get(iterateID)) {
        return iterateID;
      }
      iterateID++;
    }
    // If no divergence is found, or the range has been exceeded, return the max
    // range
    return iterateID;
  }

  /**
   * Checks whether a given word is contained in the sub-RadixTrie rooted at
   * this node
   *
   * @param word String form of the word to be checked in the sub-RadixTrie
   * @return true if the word is contained in the sub-RadixTrie, false if not
   */
  public boolean contains(String word) {
    // If the word is wholly contained in this node
    if (word.length() <= contents.size()) {
      // Check for size mismatch (if there is a mismatch, then the given word
      // does not fill a terminal-decorated node)
      if (word.length() != contents.size()) {
        return false;
      }
      // Checks all elements for match
      for (int i = 0; i < word.length(); i++) {
        if (word.charAt(i) != contents.get(i)) {
          return false;
        }
      }
      return true;
      // If word is only partially contained
    } else {
      // Checks for contained portion
      for (int i = 0; i < contents.size(); i++) {
        if (word.charAt(i) != contents.get(i)) {
          return false;
        }
      }
      // Establishes leftover word
      String leftoverWord = word.substring(contents.size());
      // Looks for valid next child
      for (RadixTrieNode child : children) {
        if (child.getFirstChar() == leftoverWord.charAt(0)) {
          return child.contains(leftoverWord);
        }
      }
      // Returns false if no valid child could be found
      return false;
    }
  }

}
