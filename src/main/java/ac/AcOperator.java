package ac;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import com.google.common.collect.HashMultiset;

import filereader.TXTReader;

public class AcOperator {
  // Stores set of all words in corpus
  private HashMultiset<String> corpusWords;
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
    corpusWords = HashMultiset.create();
    trie = new Trie();
    corpusLoaded = false;
    bmap = new BigramMap();
  }

  public String getPrefixStatus() {
    if (prefix) {
      return "on";
    } else {
      return "off";
    }
  }

  public void setPrefixStatus(boolean newStatus) {
    prefix = newStatus;
  }

  public String getWsStatus() {
    if (whitespace) {
      return "on";
    } else {
      return "off";
    }
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

  public String getSmartStatus() {
    if (prefix) {
      return "on";
    } else {
      return "off";
    }
  }

  public void setSmartStatus(boolean newStatus) {
    smart = newStatus;
  }

  public void addCorpus(String filepath) throws Exception {
    ArrayList<String> words = TXTReader.readFile(filepath, " ");
    // Adds words to the Bigram Map
    bmap.addSequence(words);
    // Adds words to the trie
    Iterator<String> iterate = words.iterator();
    while (iterate.hasNext()) {
      String s = iterate.next();
      // If non-empty String passed, add
      if (s.length() != 0) {
        trie.add(s);
        corpusWords.add(s);
      }
    }
    // Signal that corpus has been loaded
    corpusLoaded = true;
  }

  public LinkedList<String> ac(String[] sequence) throws Exception {
    // Throw Exception if no corpus has been loaded yet
    if (!corpusLoaded) {
      throw new Exception("no corpus has been loaded yet");
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
    // Instantiates HashSet that will hold all suggestions
    HashSet<Suggestion> ideas = new HashSet<Suggestion>();
    // Check for word's perfect match
    if (corpusWords.contains(lastWord)) {
      ideas.add(new Suggestion(lastWord));
    }
    // If prefix matching is on, use it to generate suggestions
    if (prefix) {
      ideas.addAll(trie.getPrefixEnds(lastWord));
    }
    // If led is on, use it to generate suggestions
    if (led > 0) {
      ideas.addAll(Generators.getLeds(corpusWords, lastWord, led));
    }
    // Instantiates HashSet that will hold all corresponding Strings
    HashSet<String> ideaStrings = new HashSet<String>();
    // Instantiates PriorityQueue that will hold all suggestions
    PriorityQueue<Suggestion> pq = new PriorityQueue<Suggestion>(10,
        chosenComp);
    // Switches from HashSet to PriorityQueue, while checking for
    // no duplicates based on first String
    for (Suggestion s : ideas) {
      if (!ideaStrings.contains(s.getFirstWord())) {
        ideaStrings.add(s.getFirstWord());
        pq.add(s);
      }
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
