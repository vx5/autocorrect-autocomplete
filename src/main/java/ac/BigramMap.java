package ac;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.HashMultiset;

/**
 * @author vx5
 *
 *         Stores a representation of the bigram probabilities from corpora,
 *         which maps how frequently given words come after specified words in
 *         the given corpora.
 */
public class BigramMap {
  // Stores core map
  private HashMap<String, HashMultiset<String>> map;

  /**
   * Constructor that initializes the internal map
   */
  public BigramMap() {
    map = new HashMap<String, HashMultiset<String>>();
  }

  /**
   * Updates the bigram map (and, for all intents and purposes, probabilities)
   * with the contents of a corpus text
   *
   * @param list String list that contains all the words, in order, to be used
   *             in the updating of the bigram map
   */
  public void addSequence(List<String> list) {
    // Iterates through every 2 consecutive words
    for (int i = 0; i < list.size() - 1; i++) {
      // Isolates "from" and "to" words for map
      String fromWord = list.get(i);
      String toWord = list.get(i + 1);
      // Adds key for from word if one does not exist
      if (!map.keySet().contains(fromWord)) {
        // Instantiates value as new, empty HashMultiset
        map.put(fromWord, HashMultiset.create());
      }
      // Adds toWord to fromWord's HashSet
      map.get(fromWord).add(toWord);
    }
  }

  /**
   * Returns the probability that one word occurs after another word in all of
   * the input sequences
   *
   * @param beforeWord the "before word"
   * @param afterWord  the word whose probability of occurring after the "before
   *                   word" is to be calculated
   * @return the probability that the afterWord occurs after the beforeWord in
   *         all of the input sequences
   */
  public float getProb(String beforeWord, String afterWord) {
    // Gets set of toWords for beforeWord
    HashMultiset<String> toWords = map.get(beforeWord);
    // Constructs toWords in case it is null, in order to avert possible
    // NullPointerException from count()
    if (toWords == null) {
      toWords = HashMultiset.create();
    }
    // Divides frequency of afterWord by all words, float cast added to ensure
    // float result. Uses 1 as floor size, since 0 would lead to NaN value
    return toWords.count(afterWord) / (float) Math.max(toWords.size(), 1);
  }

}
