package trie;

import java.util.ArrayList;
import java.util.HashSet;

public class RadixTrie {
  private RadixTrieNode root;
  private String startPrefixEnds;

  public RadixTrie() {
    root = new RadixTrieNode("root");
    startPrefixEnds = "";
  }

  public void add(String word) {
    for (RadixTrieNode child : root.getChildren()) {
      if (child.getFirstChar() == word.charAt(0)) {
        child.addWord(word);
        return;
      }
    }
    // In case child is not find, make new one
    RadixTrieNode newChild = new RadixTrieNode(word);
    newChild.makeTerminal(true);
    root.addChild(newChild);
  }

  public boolean contains(String word) {
    for (RadixTrieNode child : root.getChildren()) {
      if (child.getFirstChar() == word.charAt(0)) {
        return child.contains(word);
      }
    }
    return false;
  }

  public HashSet<String> getPrefixEnds(String word) {
    RadixTrieNode endNode = null;
    // Obtains the relevant node for the start of the recursive search
    for (RadixTrieNode child : root.getChildren()) {
      if (child.getFirstChar() == word.charAt(0)) {
        endNode = this.getEndNode(word, child);
      }
    }
    // Check for case of word outside tree
    if (endNode == null) {
      return new HashSet<String>();
    }
    // Iterate from the endNode to recover Strings
    return this.prefixEndHelp(word, startPrefixEnds, endNode);
  }

  private HashSet<String> prefixEndHelp(String givenWord, String current,
      RadixTrieNode node) {
    // Now the storage variable can be cleared
    startPrefixEnds = "";
    //
    HashSet<String> ideas = new HashSet<String>();
    // Constructs current word
    ArrayList<Character> chars = node.getContents();
    String charsWord = "";
    for (char c : chars) {
      charsWord += c;
    }
    String hereWord = current + charsWord;
    if (node.isTerminal() && !hereWord.contentEquals(givenWord)) {
      ideas.add(hereWord);
    }
    //
    for (RadixTrieNode child : node.getChildren()) {
      ideas.addAll(this.prefixEndHelp(givenWord, hereWord, child));
    }
    //
    return ideas;
  }

  private RadixTrieNode getEndNode(String word, RadixTrieNode node) {
    // If the word is wholly contained
    if (word.length() <= node.getContents().size()) {
      for (int i = 0; i < word.length(); i++) {
        // Checks for any mismatch
        if (word.charAt(i) != node.getContents().get(i)) {
          return null;
        }
      }
      return node;
      // If word is only partially contained
    } else {
      // Establishes leftover word
      String leftoverWord = word.substring(node.getContents().size());
      startPrefixEnds += word.substring(0, node.getContents().size());
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
