package ac;

import java.util.HashSet;

import com.google.common.collect.HashMultiset;

/**
 * @author vx5
 *
 *         Class that houses static methods related to the generation of
 *         suggestions for Autocorrect.
 */
public class Generators {

  /**
   * Generates Suggestions by attempting to add a single space in each possible
   * position in the given word, and using suggestions where both resulting
   * words are in the given corpora
   *
   * @param corpusWords set of all the words contained in the input corpora
   * @param givenWord   the word to be used as the basis for new Suggestions
   * @return a set of all valid Suggestions generated through the whitespace
   *         method
   */
  public static HashSet<Suggestion> whiteSpace(HashMultiset<String> corpusWords,
      String givenWord) {
    // Instantiates HashSet to be returned
    HashSet<Suggestion> suggestions = new HashSet<Suggestion>();
    // Iterates through all positions that can be used as dividers
    for (int i = 1; i < givenWord.length(); i++) {
      // Generates two words on either side of divide
      String wordOne = givenWord.substring(0, i);
      String wordTwo = givenWord.substring(i);
      // Checks whether both words are in corpora
      if (corpusWords.contains(wordOne) && corpusWords.contains(wordTwo)) {
        // Generates and adds new Suggestion
        Suggestion s = new Suggestion(wordOne);
        s.setSecondWord(wordTwo);
        suggestions.add(s);
      }
    }
    // Returns the relevant generated suggestions
    return suggestions;
  }

  /**
   * Generate Suggestions by performing valid Levenshtein edits on a given word
   *
   * @param corpusWords set of all the words contained in the input corpora
   * @param givenWord   the word to be used as the basis for new Suggestions
   * @param dist        the maximum number of edits a suggested word can be from
   *                    the given word
   * @return a set of all the valid Suggestions generated
   */
  public static HashSet<Suggestion> getLeds(HashMultiset<String> corpusWords,
      String givenWord, int dist) {
    // Create generic HashSet to hold all current suggestions
    HashSet<Suggestion> ideas = new HashSet<Suggestion>();
    // Use the valid alphabet as follows:
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    // Generate all possible updates using insertions
    for (int i = 0; i <= givenWord.length(); i++) {
      // Constructs before and after components
      String before = givenWord.substring(0, i);
      String after = "";
      if (i < givenWord.length()) {
        after = givenWord.substring(i);
      }
      // Iterate through all possible alphabet additions
      for (int j = 0; j < alphabet.length(); j++) {
        String insertion = alphabet.substring(j, j + 1);
        // Check whether word with given insertion is present
        String suggest = before + insertion + after;
        // Add Suggestion if final leg of LED calculation
        if (dist == 1 && corpusWords.contains(suggest)) {
          ideas.add(new Suggestion(suggest));
        } else if (dist != 1) {
          ideas.add(new Suggestion(suggest));
        }
      }
    }
    // Generate all possible updates using deletions (if possible)
    if (givenWord.length() > 1) {
      for (int i = 0; i < givenWord.length(); i++) {
        // Constructs before and after components
        String before = givenWord.substring(0, i);
        String after = "";
        if (i + 1 < givenWord.length()) {
          after = givenWord.substring(i + 1);
        }
        // Joins components into new words
        String suggest = before + after;
        // Add Suggestion if final leg of LED calculation
        if (dist == 1 && corpusWords.contains(suggest)) {
          ideas.add(new Suggestion(suggest));
        } else if (dist != 1) {
          ideas.add(new Suggestion(suggest));
        }
      }
    }
    // Generate all possible updates using substitutions
    for (int i = 0; i < givenWord.length(); i++) {
      // Constructs before and after components
      String before = givenWord.substring(0, i);
      String after = "";
      if (i + 1 < givenWord.length()) {
        after = givenWord.substring(i + 1);
      }
      // Iterates through all substitutions
      for (int j = 0; j < alphabet.length(); j++) {
        String sub = alphabet.substring(j, j + 1);
        // Joins components into new word
        String suggest = before + sub + after;
        // Add Suggestion if final leg of LED calculation
        if (dist == 1 && corpusWords.contains(suggest)) {
          ideas.add(new Suggestion(suggest));
        } else if (dist != 1) {
          ideas.add(new Suggestion(suggest));
        }
      }
    }
    // Instantiates set of new ideas found after recursion
    HashSet<Suggestion> newIdeas = new HashSet<Suggestion>();
    // If relevant, recur on all suggestions
    if (dist > 1) {
      for (Suggestion s : ideas) {
        newIdeas.addAll(getLeds(corpusWords, s.getFirstWord(), dist - 1));
      }
    }
    // Add all new suggestions
    ideas.addAll(newIdeas);
    // Scan all ideas for presence in corpus
    HashSet<Suggestion> scannedIdeas = new HashSet<Suggestion>();
    HashSet<String> scannedStrings = new HashSet<String>();
    for (Suggestion s : ideas) {
      if (corpusWords.contains(s.getFirstWord())
          && !scannedStrings.contains(s.getFirstWord())) {
        scannedIdeas.add(s);
        scannedStrings.add(s.getFirstWord());
      }
    }
    // Return all suggestion ideas
    return scannedIdeas;
  }

}
