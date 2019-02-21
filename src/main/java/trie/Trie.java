package trie;

import java.util.HashSet;

/**
 * @author vx5
 *
 *         Implementation of trie, or prefix tree, data structure, that allows
 *         for the space-efficient storage of many words in a tree, and
 *         efficient calculation of suffixes of certain words in the tree.
 */
public class Trie {
  // Stores the root node
  private TrieNode root;

  /**
   * Constructor that initializes the Trie by initializing the root node.
   */
  public Trie() {
    // Populates root with TrieNode's root constructor
    root = new TrieNode();
  }

  /**
   * Adds a given String to the Trie.
   *
   * @param s Word to be added to Trie, in String form
   */
  public void add(String s) {
    // Passes to helper function, starting at root
    this.addHelper(root, s);
  }

  private void addHelper(TrieNode n, String s) {
    // Isolates first character of s
    char firstChar = s.charAt(0);
    // Starts with assumption that node does not have a child
    // that matches first letter
    boolean firstCharHasNode = false;
    // Checks all of node's children for one that matches first letter
    for (TrieNode child : n.getChildren()) {
      if (child.getChar() == firstChar) {
        // Notes that required node was found
        firstCharHasNode = true;
        // If this is the end of the recursive call,
        // mark that node as terminal
        if (s.length() == 1) {
          child.makeTerminal();
        } else {
          // If it is not, initiate a recursive call
          this.addHelper(child, s.substring(1));
        }
      }
    }
    // If no child was found, create a new one
    if (!firstCharHasNode) {
      TrieNode newChild = new TrieNode(firstChar);
      n.addChild(newChild);
      // And if this is the end of the recursive call,
      // mark the node as terminal
      if (s.length() == 1) {
        newChild.makeTerminal();
      } else {
        // If it is not, initiate a recursive call
        this.addHelper(newChild, s.substring(1));
      }
    }
  }

  /**
   * Checks whether a given word is contained in this Trie.
   *
   * @param s the word to be checked in the Trie
   * @return boolean that describes whether the word is present in the Trie
   */
  public boolean contains(String s) {
    // Passes to helper function
    return this.containsHelper(root, s);
  }

  private boolean containsHelper(TrieNode n, String s) {
    char firstChar = s.charAt(0);
    // Check all children of n to see if any match first character
    for (TrieNode child : n.getChildren()) {
      if (child.getChar() == firstChar) {
        if (s.length() == 1) {
          // If this is end of call, check for terminal decoration
          return child.isTerminal();
        } else {
          // If not, perform recursive call from child
          return containsHelper(child, s.substring(1));
        }
      }
    }
    // If no children match first character, then by default
    // search as failed, and we return false
    return false;
  }

  /**
   * Returns all the words in the Trie for which a given word in the Trie is a
   * prefix.
   *
   * @param s word that is to be checked as a prefix for other words in the Trie
   * @return a set of all of the words in the Trie for which the given word is a
   *         prefix
   */
  public HashSet<String> getPrefixEnds(String s) {
    // Obtains the node at which the string ends
    TrieNode subRoot = this.getEndNode(root, s);
    // Instantiates HashSet that stores all suggestions
    HashSet<String> suggestions = new HashSet<String>();
    // IF end node was found,
    // Iterates through all possible suffixes, adds to suggestions
    if (subRoot != null) {
      this.suggest(suggestions, s, subRoot);
    }
    // Returns set of suggestions
    return suggestions;
  }

  private TrieNode getEndNode(TrieNode n, String s) {
    // Isolate first character of s
    char firstChar = s.charAt(0);
    // Check for child that matches first character
    for (TrieNode child : n.getChildren()) {
      if (child.getChar() == firstChar) {
        // If this is end of search, return this child as end node
        if (s.length() == 1) {
          return child;
        } else {
          // Else, recur from that child from the next letter
          return getEndNode(child, s.substring(1));
        }
      }
    }
    // Return null if whole word is not found
    return null;
  }

  private void suggest(HashSet<String> suggestions, String current,
      TrieNode subRoot) {
    // Looks at all of the given node's children
    for (TrieNode child : subRoot.getChildren()) {
      // Constructs partial String at each child
      String newCurrent = current + child.getChar();
      // If child has terminal decoration, word has been found
      if (child.isTerminal()) {
        suggestions.add(newCurrent);
      }
      // In all cases, recur from that child with the new, larger word
      this.suggest(suggestions, newCurrent, child);
    }
  }
}
