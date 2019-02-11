package ac;

import java.util.HashSet;

public class TrieNode {
  private char nodeChar;
  private HashSet<TrieNode> children;
  private boolean terminal;

  public TrieNode() {
    children = new HashSet<TrieNode>();
  }

  public TrieNode(char c) {
    nodeChar = c;
    children = new HashSet<TrieNode>();
  }

  public char getChar() {
    return nodeChar;
  }

  public void addChild(TrieNode n) {
    children.add(n);
  }

  public HashSet<TrieNode> getChildren() {
    return children;
  }

  public void makeTerminal() {
    terminal = true;
  }

  public boolean isTerminal() {
    return terminal;
  }

}
