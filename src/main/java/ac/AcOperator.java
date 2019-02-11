package ac;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class AcOperator {
  // Stores set of all words in corpus
  private HashSet<String> corpusWords;
  // Stores Trie for given corpora
  private Trie trie;
  // Stores Bigram Map for corpora
  private BigramMap bmap;
  // Stores all settings
  private boolean prefix;
  private boolean whitespace;
  private int led;
  private boolean smart;
  // Stores whether corpus has been successfully loaded
  private boolean corpusLoaded;

  /**
   * Constructor.
   */
  public AcOperator() {
    corpusWords = new HashSet<String>();
    trie = new Trie();
    corpusLoaded = false;
    bmap = new BigramMap();
  }

  public boolean getPrefixStatus() {
    return prefix;
  }

  public void setPrefixStatus(boolean newStatus) {
    prefix = newStatus;
  }

  public boolean getWsStatus() {
    return whitespace;
  }

  public void setWsStatus(boolean newStatus) {
    whitespace = newStatus;
  }

  public int getLedSetting() {
    return led;
  }

  public void setLedSetting(int newSetting) {
    led = newSetting;
  }

  public boolean getSmartStatus() {
    return smart;
  }

  public void setSmartStatus(boolean newStatus) {
    smart = newStatus;
  }

  public void addCorpus(String filepath) {
    // TODO: populate this list
    LinkedList<String> words = new LinkedList<String>();
    // Adds words to the Bigram Map
    bmap.addSequence(words);
    // Adds words to the trie
    for (String s : words) {
      trie.add(s);
    }
    // Adds words to the overall corpora words
    corpusWords.addAll(words);
    // Signal that corpus has been loaded
    corpusLoaded = true;
  }

  public LinkedList<String> ac(String[] sequence) throws Exception {
    // Throw Exception if no corpus has been loaded yet
    if (!corpusLoaded) {
      // TODO
    }
    // Stores last word
    String lastWord = sequence[sequence.length - 1];
    // Instantiates default comparator to be used
    Comparator<Suggestion> chosenComp;
    // Feeds different arguments to comparator depending on whether a "before"
    // word exists or not
    if (sequence.length == 1) {
      chosenComp = new SuggestComparator(lastWord, corpusWords);
    } else {
      chosenComp = new SuggestComparator(sequence[sequence.length - 2], bmap,
          lastWord, corpusWords);
    }
    // Instantiates PriorityQueue that will hold all suggestions
    PriorityQueue<Suggestion> pq = new PriorityQueue<Suggestion>(10,
        chosenComp);
    // If prefix matching is on, use it to generate suggestions
    if (prefix) {
      pq.addAll(trie.getPrefixEnds(lastWord));
    }
    // If led is on, use it to generate suggestions
    if (led > 0) {
      pq.addAll(Generators.getLeds(corpusWords, lastWord, led));
    }
    // If whitespace is on, use it to generate suggestions
    if (whitespace) {
      pq.addAll(Generators.whiteSpace(corpusWords, lastWord));
    }
    // Returns list of suggestions from PriorityQueue
    return this.pqToList(sequence, pq);
  }

  private LinkedList<String> pqToList(String[] sequence,
      PriorityQueue<Suggestion> pq) {
    // Instantiates list to be returned
    LinkedList<String> suggestions = new LinkedList<String>();
    // Generate proper start to each recommendation from sequence
    String recStart = "";
    for (int i = 0; i < sequence.length - 1; i++) {
      recStart += sequence[i] + " ";
    }
    // Iterates through entire PQ, stores suggestions in LinkedList
    while (pq.peek() != null) {
      // Get overall suggestion given
      Suggestion given = pq.poll();
      // Get suggestion's words
      String firstWord = given.getFirstWord();
      String secondWord = given.getSecondWord();
      // Construct correct compound suggestion
      String compound = firstWord;
      if (secondWord != null) {
        compound += " " + secondWord;
      }
      // Adds proper start
      String rec = recStart + compound;
      // Adds recommendation to list
      suggestions.add(rec);
    }
    // Returns list
    return suggestions;
  }

}
