package ac;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import com.google.common.collect.HashMultiset;

import filereader.TXTReader;
import trie.RadixTrie;

/**
 * @author vx5
 *
 *         Main operating class that performs commands on behalf of the GUI and
 *         REPL interfaces, including commands to change settings, load corpora,
 *         and make autocorrect requests.
 */
public class AcOperator {
  // Stores set of all words in corpus
  private HashMultiset<String> corpusWords;
  // Stores set of all words in dictionary
  private HashSet<String> dictionary;
  // Stores Trie for given corpora
  private RadixTrie trie;
  // Stores Bigram Map for corpora
  private BigramMap bmap;
  // Stores all settings
  private boolean prefix;
  private boolean whitespace;
  private int led;
  private boolean smart;
  // Stores whether corpus has been successfully loaded
  private boolean corpusLoaded;
  // Stores list of corpus filepaths that have been loaded
  private ArrayList<String> filePaths;

  /**
   * Constructor that initializes all of the instance fields used in the core
   * operations and all of the settings instance fields.
   */
  public AcOperator() {
    // Uses reset function to populate instance functions and
    // reset settings fields
    this.reset();
    // Loads given dictionary's words into a HashSet
    ArrayList<String> words;
    try {
      // Adds all valid words into dictionary set
      words = TXTReader.readFile("data/autocorrect/dictionary.txt", " ");
      for (String word : words) {
        if (word.length() != 0) {
          dictionary.add(word);
        }
      }
    } catch (Exception e) {
      // This Exception should never happen so long as the dictionary.txt file
      // is not tampered with. If it is, an error message will be printed
      // directly to the REPL
      System.out.println(
          "ERROR: Please ensure that the file \"data/autocorrect/dictionary.txt\" is present");
    }
  }

  /**
   * Resets all the core instance variables that handle the execution of the
   * AcOperator, and resets all of the settings instance variables that dictate
   * the generation and sorting of autocorrect options
   */
  public void reset() {
    // Resets core variables
    corpusWords = HashMultiset.create();
    trie = new RadixTrie();
    corpusLoaded = false;
    bmap = new BigramMap();
    filePaths = new ArrayList<String>();
    dictionary = new HashSet<String>();
    // Reset all settings
    prefix = false;
    whitespace = false;
    led = 0;
    smart = false;
  }

  /**
   * Returns the current prefix setting
   *
   * @return a String that reads either "on" or "off"
   */
  public String getPrefixStatus() {
    if (prefix) {
      return "on";
    } else {
      return "off";
    }
  }

  /**
   * Alters the current prefix setting
   *
   * @param newStatus boolean value that is true to represent "on", and false to
   *                  represent "off"
   */
  public void setPrefixStatus(boolean newStatus) {
    prefix = newStatus;
  }

  /**
   * Returns the current whitespace setting
   *
   * @return a String that reads either "on" or "off"
   */
  public String getWsStatus() {
    if (whitespace) {
      return "on";
    } else {
      return "off";
    }
  }

  /**
   * Alters the current whitespace setting
   *
   * @param newStatus boolean value that is true to represent "on", and false to
   *                  represent "off"
   */
  public void setWsStatus(boolean newStatus) {
    whitespace = newStatus;
  }

  /**
   * Returns the current LED setting
   *
   * @return a String that reads either "on" or "off"
   */
  public int getLedSetting() {
    return led;
  }

  /**
   *
   * Alters the current whitespace setting
   *
   * @param newStatus Integer value that represents new maximum Levenshtein edit
   *                  distance to be used in generating suggestions via edits
   */
  public void setLedSetting(int newSetting) {
    led = newSetting;
  }

  /**
   * Returns the current Smart sorting setting
   *
   * @return a String that reads either "on" or "off"
   */
  public String getSmartStatus() {
    if (smart) {
      return "on";
    } else {
      return "off";
    }
  }

  /**
   *
   * Alters the current smart sorting
   *
   * @param newStatus boolean value that is true to represent "on", and false to
   *                  represent "off"
   */
  public void setSmartStatus(boolean newStatus) {
    smart = newStatus;
  }

  /**
   * Adds a corpus to be used in an Autocorrect request
   *
   * @param filepath A filepath that describes the location of a .txt file whose
   *                 words will be used as a corpus for Autocorrect requests
   *
   * @throws Exception if the suggested filepath has already been added to the
   *                   collective corpora or if there is an error in reading
   *                   from the given filepath, such as because the file does
   *                   not exist, or the file is empty
   */
  public void addCorpus(String filepath) throws Exception {
    // Checks to avoid duplicate corpuses
    for (String x : filePaths) {
      if (x.contentEquals(filepath.toLowerCase())) {
        throw new Exception(
            "file " + filepath + " has already been loaded to corpora");
      }
    }
    ArrayList<String> words = TXTReader.readFile(filepath, " ");
    // Adds words to the Bigram Map
    bmap.addSequence(words);
    // Adds words to the trie
    Iterator<String> iterate = words.iterator();
    // Adds filepath to filepath-checker
    filePaths.add(filepath.toLowerCase());
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

  /**
   * Returns the filepaths of all the corpora currently being used for
   * Autocorrect suggestions
   *
   * @return an ArrayList of the String filepaths of all the current corpora
   */
  public ArrayList<String> getCorpuses() {
    return filePaths;
  }

  /**
   * Performs an Autocorrect request, generating a maximum of 5 suggestions
   *
   * @param sequence an array of Strings where each String represents a word in
   *                 the sentence passed to Autocorrect
   * @return a LinkedList of Strings where each String represents a suggestion
   *         from Autocorrect
   * @throws Exception if a corpus has not yet been loaded
   */
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
      // Choose comparator based on whether smart setting is set
      if (smart) {
        chosenComp = new SmartComparator(corpusWords, dictionary);
      } else {
        chosenComp = new SuggestComparator(lastWord, corpusWords);
      }
    } else {
      // Choose comparator based on whether smart setting is set
      if (smart) {
        chosenComp = new SmartComparator(sequence[sequence.length - 2], bmap,
            corpusWords, dictionary);
      } else {
        chosenComp = new SuggestComparator(sequence[sequence.length - 2], bmap,
            lastWord, corpusWords);
      }
    }
    // Instantiates HashSet that will hold all suggestions
    HashSet<Suggestion> ideas = new HashSet<Suggestion>();
    // Check for word's perfect match
    if (corpusWords.contains(lastWord)) {
      ideas.add(new Suggestion(lastWord));
    }
    // If prefix matching is on, use it to generate suggestions
    if (prefix) {
      HashSet<String> prefixEnds = trie.getPrefixEnds(lastWord);
      for (String s : prefixEnds) {
        ideas.add(new Suggestion(s));
      }
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
