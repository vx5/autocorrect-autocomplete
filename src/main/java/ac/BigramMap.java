package ac;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.HashMultiset;

public class BigramMap {
  // Stores core map
  private HashMap<String, HashMultiset<String>> map;

  /**
   * Empty constructor
   */
  public BigramMap() {
    map = new HashMap<String, HashMultiset<String>>();
  }

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

  public float getProb(String beforeWord, String afterWord) {
    // Gets set of toWords for beforeWord
    HashMultiset<String> toWords = map.get(beforeWord);
    // Constructs toWords in case it is null, in order to avert possible
    // NullPointerException from frequency()
    if (toWords == null) {
      toWords = HashMultiset.create();
    }
    // Divides frequency of afterWord by all words, float cast added to ensure
    // float result
    return toWords.count(afterWord) / (float) toWords.size();
  }

}
