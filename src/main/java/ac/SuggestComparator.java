package ac;

import java.util.Comparator;

import com.google.common.collect.HashMultiset;

/**
 * @author vx5
 *
 *         Comparator that helps compare Suggestions according to the default
 *         ranking system from the assignment handout.
 */
public class SuggestComparator implements Comparator<Suggestion> {
  // Stores "before" word, if it is given
  private String before;
  // Stores given word
  private String given;
  // Stores bigram map
  private BigramMap bmap;
  // Stores full set of corpus words
  private HashMultiset<String> corpusWords;

  /**
   * Constructor used in case when the sorting order should use the bigram map.
   *
   * @param beforeWord       the word occurring before the would-be suggestions,
   *                         to be used with the bigram map
   * @param newMap           the BigramMap used with the given "before word"
   * @param givenWord        the word used as the basis for new Suggestions
   * @param givenCorpusWords set of all the words found in the corpora
   */
  public SuggestComparator(String beforeWord, BigramMap newMap,
      String givenWord, HashMultiset<String> givenCorpusWords) {
    // Stores parameters in Comparator's instance fields
    before = beforeWord;
    bmap = newMap;
    given = givenWord;
    corpusWords = givenCorpusWords;
  }

  /**
   * Constructor used in case when the sorting order should not use the bigram
   * map.
   *
   * @param givenWord        the word used as the basis for new Suggestions
   * @param givenCorpusWords set of all the words found in the corpora
   */
  public SuggestComparator(String givenWord,
      HashMultiset<String> givenCorpusWords) {
    // Signals through instance variables that bigram map should not be used
    before = null;
    bmap = null;
    // Stores parameters in Comparator's instance fields
    given = givenWord;
    corpusWords = givenCorpusWords;
  }

  @Override
  public int compare(Suggestion s1, Suggestion s2) {
    // Obtain the Strings in each Suggestion
    String suggestOne = s1.getFirstWord();
    String suggestTwo = s2.getFirstWord();
    // Check whether either is equivalent to given
    boolean oneMatch = suggestOne.contentEquals(given);
    boolean twoMatch = suggestTwo.contentEquals(given);
    // Check for case of non-tie on perfect match
    if (oneMatch && !twoMatch) {
      return -1;
    } else if (twoMatch && !oneMatch) {
      return 1;
    }
    // If before-word was given, use BigramMap to calculate probabilities
    if (before != null) {
      // Calculate probabilities for each
      float bigramProbOne = bmap.getProb(before, suggestOne);
      float bigramProbTwo = bmap.getProb(before, suggestTwo);
      // Check for case of non-tie on bigram probability
      if (bigramProbOne > bigramProbTwo) {
        return -1;
      } else if (bigramProbTwo > bigramProbOne) {
        return 1;
      }
    }
    // Next, try to check unigram probability
    // Calculate occurrences of each word in all corpora
    int occursOne = corpusWords.count(suggestOne);
    int occursTwo = corpusWords.count(suggestTwo);
    // Check for case of non-tie on unigram frequency
    if (occursOne > occursTwo) {
      return -1;
    } else if (occursTwo > occursOne) {
      return 1;
    }
    // If all else fails, use lexicographic ordering
    return suggestOne.compareTo(suggestTwo);
  }

}
