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
  // Stores full set of corpus words
  private HashMultiset<String> corpusWords;
  // Stores full set of dictionary words
  private HashSet<String> dictionary;

  /**
   * Constructor used in case when the sorting order should use the bigram map
   *
   * @param beforeWord       the word occurring before the would-be suggestions,
   *                         to be used with the bigram map
   * @param newMap           the BigramMap used with the given "before word"
   * @param givenCorpusWords set of all the words found in the corpora
   * @param givenDiction     set of all words found in the given dictionary
   */
  public SmartComparator(String beforeWord, BigramMap newMap,
      HashMultiset<String> givenCorpusWords, HashSet<String> givenDiction) {
    // Stores parameters in Comparator's instance fields
    before = beforeWord;
    bmap = newMap;
    corpusWords = givenCorpusWords;
    dictionary = givenDiction;
  }

  /**
   * Constructor used in case when the sorting order should not use the bigram
   * map
   *
   * @param givenCorpusWords set of all the words found in the corpora
   * @param givenDiction     set of all words found in the given dictionary
   */
  public SmartComparator(HashMultiset<String> givenCorpusWords,
      HashSet<String> givenDiction) {
    // Signals through instance variables that bigram map should not be used
    before = null;
    bmap = null;
    // Stores parameters in Comparator's instance fields
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
      if (bmap.getProb(before, s1.getFirstWord()) > 0.1) {
        oneScore += 10;
      }
      if (bmap.getProb(before, s2.getFirstWord()) > 0.1) {
        twoScore += 10;
      }
    }
    // Assigns points based on unigram probabilities
    if (corpusWords.count(s1.getFirstWord()) > 1) {
      oneScore += 10;
    } else if (corpusWords.count(s1.getFirstWord()) == 1) {
      oneScore += 5;
    }
    if (corpusWords.count(s2.getFirstWord()) > 1) {
      oneScore += 10;
    } else if (corpusWords.count(s2.getFirstWord()) == 1) {
      oneScore += 5;
    }
    // Assigns points based on length
    if (s1.getFirstWord().length() > 2) {
      oneScore += 10;
    }
    if (s2.getFirstWord().length() > 2) {
      twoScore += 10;
    }
    // Compares points
    if (oneScore != twoScore) {
      return twoScore - oneScore;
    }
    // If point method failed, defaults to returning based on length
    return s2.getFirstWord().length() - s1.getFirstWord().length();
  }

}
