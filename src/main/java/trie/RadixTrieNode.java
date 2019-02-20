package trie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class RadixTrieNode {

  private ArrayList<Character> contents;

  private HashSet<RadixTrieNode> children;

  private boolean terminal;

  public RadixTrieNode(String str) {
    contents = new ArrayList<Character>();
    for (int i = 0; i < str.length(); i++) {
      contents.add(str.charAt(i));
    }
    children = new HashSet<RadixTrieNode>();
    terminal = false;
  }

  public void addChild(RadixTrieNode newNode) {
    children.add(newNode);
  }

  public HashSet<RadixTrieNode> getChildren() {
    return children;
  }

  public ArrayList<Character> getContents() {
    return contents;
  }

  public char getFirstChar() {
    return contents.get(0);
  }

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

  public void addWord(String word) {
    // Identifies whether word is already contained
    if (this.contains(word)) {
      // If so, take no action
      return;
    }
    // Locates soonest divergence point between word and content
    int divergeLoc = this.findDivergence(word);
    // If diverging point is in the standard range
    if (divergeLoc < Math.min(word.length(), contents.size())) {
      // Remember that we know that at least the first character is common
      // Creates new continuation node
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
      if (this.isTerminal()) {
        this.makeTerminal(false);
        continueNode.makeTerminal(true);
      }
      // Creates new diverging node
      RadixTrieNode divergeNode = new RadixTrieNode(word.substring(divergeLoc));
      divergeNode.makeTerminal(true);
      // Sets the new nodes as the new children
      this.addChild(continueNode);
      this.addChild(divergeNode);
      // Reduces the current contents
      ArrayList<Character> newContents = new ArrayList<Character>();
      for (int i = 0; i < divergeLoc; i++) {
        newContents.add(contents.get(i));
      }
      contents = newContents;
      // If the word is shorter than the contents
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
      if (this.isTerminal()) {
        continueNode.makeTerminal(true);
      }
      //
      this.makeTerminal(true);
      //
      this.addChild(continueNode);
      // Reduces the current contents
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
    // Sets maximum to be used for search
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

  public boolean contains(String word) {
    // If the word is wholly contained
    if (word.length() <= contents.size()) {
      // Check for size mismatch
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
