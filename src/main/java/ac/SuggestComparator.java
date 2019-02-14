package ac;

import java.util.Comparator;

import com.google.common.collect.HashMultiset;

public class SuggestComparator implements Comparator<Suggestion> {
  // Stores "before" word, if it is given
  private String before;
  // Stores given word
  private String given;
  // Stores bigram map
  private BigramMap bmap;
  // Stores full set of corpus words
  private HashMultiset<String> corpusWords;

  public SuggestComparator(String beforeWord, BigramMap newMap,
      String givenWord, HashMultiset<String> givenCorpusWords) {
    before = beforeWord;
    bmap = newMap;
    given = givenWord;
    corpusWords = givenCorpusWords;
  }

  public SuggestComparator(String givenWord,
      HashMultiset<String> givenCorpusWords) {
    before = null;
    bmap = null;
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
