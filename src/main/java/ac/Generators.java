package ac;

import java.util.HashSet;

public class Generators {

  public static HashSet<Suggestion> whiteSpace(HashSet<String> corpusWords,
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
    return suggestions;
  }

  public static HashSet<Suggestion> getLeds(HashSet<String> corpusWords,
      String givenWord, int dist) {
    return null;
  }

}
