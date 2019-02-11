package ac;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class BigramMap {
  // Stores core map
  private HashMap<String, HashSet<String>> map;

  /**
   * Empty constructor
   */
  public BigramMap() {
  }

  public void addSequence(List<String> list) {
    // Iterates through every 2 consecutive words
    for (int i = 0; i < list.size() - 1; i++) {
      // Isolates "from" and "to" words for map
      String fromWord = list.get(i);
      String toWord = list.get(i + 1);
      // Adds key for from word if one does not exist
      if (!map.keySet().contains(fromWord)) {
        // Instantiates value as new, empty HashSet
        map.put(fromWord, new HashSet<String>());
      }
      // Adds toWord to fromWord's HashSet
      map.get(fromWord).add(toWord);
    }
  }

  public float getProb(String beforeWord, String afterWord) {
    // Gets set of toWords for beforeWord
    HashSet<String> toWords = map.get(beforeWord);
    // Divides frequency of afterWord by all words, float cast added to ensure
    // float result
    return Collections.frequency(toWords, afterWord) / (float) toWords.size();
  }

}
