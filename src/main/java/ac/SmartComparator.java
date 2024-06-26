package ac;

import java.util.Comparator;
import java.util.HashSet;

import com.google.common.collect.HashMultiset;

/**
 * @author vx5
 *
 *         Comparator that helps compare Suggestions according to the Smart
 *         ranking system.
 */
public class SmartComparator implements Comparator<Suggestion> {
  // Stores "before" word, if it is given
  private String before;
  // Stores bigram map
  private BigramMap bmap;
  // Stores previously chosen word
  private HashSet<String> selected;
  // Stores full set of corpus words
  private HashMultiset<String> corpusWords;
  // Stores full set of dictionary words
  private HashSet<String> dictionary;
  // Stores constants relevant to comparison process
  private final float bigramCutoff = (float) 0.1;
  private final int largePointInc = 10;
  private final int smallPointInc = 5;

  /**
   * Constructor used in case when the sorting order should use the bigram map.
   *
   * @param beforeWord       the word occurring before the would-be suggestions,
   *                         to be used with the bigram map
   * @param newMap           the BigramMap used with the given "before word"
   * @param selectedWords    the set of all words the user has selected so far
   * @param givenCorpusWords set of all the words found in the corpora
   * @param givenDiction     set of all words found in the given dictionary
   */
  public SmartComparator(String beforeWord, BigramMap newMap,
      HashSet<String> selectedWords, HashMultiset<String> givenCorpusWords,
      HashSet<String> givenDiction) {
    // Stores parameters in Comparator's instance fields
    before = beforeWord;
    bmap = newMap;
    selected = selectedWords;
    corpusWords = givenCorpusWords;
    dictionary = givenDiction;
  }

  /**
   * Constructor used in case when the sorting order should not use the bigram
   * map.
   *
   * @param selectedWords    set of all words the user has selected so far
   * @param givenCorpusWords set of all the words found in the corpora
   * @param givenDiction     set of all words found in the given dictionary
   */
  public SmartComparator(HashSet<String> selectedWords,
      HashMultiset<String> givenCorpusWords, HashSet<String> givenDiction) {
    // Signals through instance variables that bigram map should not be used
    before = null;
    bmap = null;
    // Stores parameters in Comparator's instance fields
    selected = selectedWords;
    corpusWords = givenCorpusWords;
    dictionary = givenDiction;
  }

  @Override
  public int compare(Suggestion s1, Suggestion s2) {
    // Checks whether each suggestion is (if two words are present,
    // doubly) in the dictionary, the corpus, and is of length above 1
    boolean oneInDiction, twoInDiction, oneInCorpus, twoInCorpus, oneLong,
        twoLong, oneSecLong, twoSecLong;
    oneInDiction = dictionary.contains(s1.getFirstWord());
    twoInDiction = dictionary.contains(s2.getFirstWord());
    oneInCorpus = corpusWords.contains(s1.getFirstWord());
    twoInCorpus = corpusWords.contains(s2.getFirstWord());
    oneLong = s1.getFirstWord().length() > 1;
    twoLong = s2.getFirstWord().length() > 1;
    // Checks length of second words in suggestions, where they exist
    if (s1.getSecondWord() == null) {
      oneSecLong = true;
    } else if (s1.getSecondWord().length() > 1) {
      oneSecLong = true;
    } else {
      oneSecLong = false;
    }
    // Checks length of second words in suggestions, where they exist
    if (s2.getSecondWord() == null) {
      twoSecLong = true;
    } else if (s2.getSecondWord().length() > 1) {
      twoSecLong = true;
    } else {
      twoSecLong = false;
    }
    // Checks for case of one suggestion dominating the other,
    // then checks for case of one suggestion being in corpus,
    // but not in dictionary
    if (oneInDiction && oneInCorpus && oneLong && oneSecLong
        && !(twoInDiction && twoInCorpus && twoLong && twoSecLong)) {
      return -1;
    } else if (twoInDiction && twoInCorpus && twoLong && twoSecLong
        && !(oneInDiction && oneInCorpus && oneLong && oneSecLong)) {
      return 1;
    } else if (oneInCorpus && oneLong && oneSecLong
        && !(twoInCorpus && twoLong && twoSecLong)) {
      return -1;
    } else if (twoInCorpus && twoLong && twoSecLong
        && !(oneInCorpus && oneLong && oneSecLong)) {
      return 1;
    }
    // Ascribes points to each suggestion
    int oneScore = 0;
    int twoScore = 0;
    // Assigns points based on bigram probabilities, if relevant
    if (before != null) {
      if (bmap.getProb(before, s1.getFirstWord()) > bigramCutoff) {
        oneScore += largePointInc;
      }
      if (bmap.getProb(before, s2.getFirstWord()) > bigramCutoff) {
        twoScore += largePointInc;
      }
    }
    // Assigns points based on unigram probabilities
    if (corpusWords.count(s1.getFirstWord()) > 1) {
      oneScore += largePointInc;
    } else if (corpusWords.count(s1.getFirstWord()) == 1) {
      oneScore += smallPointInc;
    }
    if (corpusWords.count(s2.getFirstWord()) > 1) {
      oneScore += largePointInc;
    } else if (corpusWords.count(s2.getFirstWord()) == 1) {
      oneScore += smallPointInc;
    }
    // Assigns points based on length
    if (s1.getFirstWord().length() > 2) {
      oneScore += largePointInc;
    }
    if (s2.getFirstWord().length() > 2) {
      twoScore += largePointInc;
    }
    // Assigns points based on previous user input
    if (s1.getSecondWord() == null) {
      if (selected.contains(s1.getFirstWord())) {
        oneScore += largePointInc;
      }
    } else {
      if (selected.contains(s1.getSecondWord())) {
        oneScore += largePointInc;
      }
    }
    if (s2.getSecondWord() == null) {
      if (selected.contains(s2.getFirstWord())) {
        twoScore += largePointInc;
      }
    } else {
      if (selected.contains(s2.getSecondWord())) {
        twoScore += largePointInc;
      }
    }
    // Compares points
    if (oneScore != twoScore) {
      return twoScore - oneScore;
    }
    // If point method does not differentiate, attempt to use length
    int lengthOne = s1.getFirstWord().length();
    int lengthTwo = s2.getFirstWord().length();
    if (lengthOne != lengthTwo) {
      return lengthTwo - lengthOne;
    }
    // If length method does not differentiate, then compare lexicographically
    return s1.getFirstWord().compareTo(s2.getFirstWord());
  }

}
