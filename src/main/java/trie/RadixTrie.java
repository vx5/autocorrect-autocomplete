package trie;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author vx5
 *
 *         My implementation of the Radix Trie, or Compact Trie, which is the
 *         optional space-optimized version of the standard Trie described on
 *         the handout page.
 */
public class RadixTrie {
  // Stores the root node of the trie
  private RadixTrieNode root;
  // During the prefix-ends search, stores the portion of the given word that is
  // not covered by the node at which the given word "ends"
  private String startPrefixEnds;

  /**
   * Constructor that simply initializes the instance variables.
   */
  public RadixTrie() {
    root = new RadixTrieNode("root");
    startPrefixEnds = "";
  }

  /**
   * Adds a given word to the RadixTrie.
   *
   * @param word the word to be added to the RadixTrie, in String form
   */
  public void add(String word) {
    // Finds which path, from the root node, the given word would follow
    for (RadixTrieNode child : root.getChildren()) {
      if (child.getFirstChar() == word.charAt(0)) {
        // If the path beginning exists, delegate the add task to the relevant
        // child of the root node
        child.addWord(word);
        return;
      }
    }
    // In case relevant child is not found, make new one, and designate it as
    // representing a word
    RadixTrieNode newChild = new RadixTrieNode(word);
    newChild.makeTerminal(true);
    root.addChild(newChild);
  }

  /**
   * Checks whether the RadixTrie contains a given word.
   *
   * @param word the word to be checked for in the RadixTrie, in String form
   * @return true if the RadixTrie contains the word, false if not
   */
  public boolean contains(String word) {
    // Checks whether the word is possibly contained in a path from the root
    for (RadixTrieNode child : root.getChildren()) {
      if (child.getFirstChar() == word.charAt(0)) {
        // If so, delegates to the relevant child's contains() method
        return child.contains(word);
      }
    }
    // If not, return false
    return false;
  }

  /**
   * Returns a set of all the words in a RadixTrie that start with a given word
   * (in which the given word is a prefix) by recursively searching through the
   * RadixTrie.
   *
   * @param word the word to be used as the prefix to search from
   * @return a set of all the words in the RadixTrie for which the given word is
   *         a prefix
   */
  public HashSet<String> getPrefixEnds(String word) {
    // Initializes the node at/in which the given word ends
    RadixTrieNode endNode = null;
    // Obtains the relevant ending node, for the start of the recursive search
    for (RadixTrieNode child : root.getChildren()) {
      // If a path for the given word exists from the root node
      if (child.getFirstChar() == word.charAt(0)) {
        // Then check for the endNode (if it can be found)
        endNode = this.getEndNode(word, child);
      }
    }
    // Check for case of word outside tree
    if (endNode == null) {
      // Returns empty set
      return new HashSet<String>();
    }
    // Iterate from the endNode to recover Strings.
    // Passes startPrefixEnds, which stores the portion of the word that occurs
    // before the ending node
    return this.prefixEndHelp(word, startPrefixEnds, endNode);
  }

  private HashSet<String> prefixEndHelp(String givenWord, String current,
      RadixTrieNode node) {
    // Clear the storage variable, now that it has been passed (and is
    // accessible) as a parameter of this method
    startPrefixEnds = "";
    // Initializes a set of the String ideas
    HashSet<String> ideas = new HashSet<String>();
    // Constructs current String that this method is at in the RadixTrie
    ArrayList<Character> chars = node.getContents();
    String charsWord = "";
    for (char c : chars) {
      charsWord += c;
    }
    // Combines current node with String up to this node
    String hereWord = current + charsWord;
    // Checks for a new, valid word, namely that the end of the String is
    // decorated as terminal and is different from the given word
    if (node.isTerminal() && !hereWord.contentEquals(givenWord)) {
      ideas.add(hereWord);
    }
    // Recursively iterates the search on all the children
    for (RadixTrieNode child : node.getChildren()) {
      ideas.addAll(this.prefixEndHelp(givenWord, hereWord, child));
    }
    // Returns the ideas at the end of the recursive search
    return ideas;
  }

  private RadixTrieNode getEndNode(String word, RadixTrieNode node) {
    // If the word is wholly contained in the given node
    if (word.length() <= node.getContents().size()) {
      for (int i = 0; i < word.length(); i++) {
        // Checks for any mismatch
        if (word.charAt(i) != node.getContents().get(i)) {
          // Return null to represent end node not found
          return null;
        }
      }
      // If complete match, then return the node
      return node;
      // If word is only partially contained in this node
    } else {
      // Check that the part of the word that is contained matches
      String containedWord = word.substring(0, node.getContents().size());
      for (int i = 0; i < containedWord.length(); i++) {
        // Checks for any mismatch
        if (containedWord.charAt(i) != node.getContents().get(i)) {
          // Return null to represent end node not found
          return null;
        }
      }
      // Establishes leftover word
      String leftoverWord = word.substring(node.getContents().size());
      startPrefixEnds += containedWord;
      // Looks for valid next child
      for (RadixTrieNode child : node.getChildren()) {
        if (child.getFirstChar() == leftoverWord.charAt(0)) {
          return this.getEndNode(leftoverWord, child);
        }
      }
      // If no relevant child found, word is not contained in tree
      return null;
    }
  }

}
