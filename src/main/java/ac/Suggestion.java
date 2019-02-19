package ac;

/**
 * @author vx5
 *
 *         Holds the totality of an Autocorrect suggestion, including either one
 *         or two words (since some suggestion generation methods, like the
 *         whitespace method, may return multiple words in a suggestion).
 */
public class Suggestion {
  // Stores the two possible words of the Suggestion
  private String firstWord;
  private String secondWord;

  /**
   * Constructor that takes in the first word of the suggestion
   *
   * @param word the first (and possibly only) word in the Suggestion
   */
  public Suggestion(String word) {
    // Passes parameter to instance variable
    firstWord = word;
    // Initializes second word as null
    secondWord = null;
  }

  /**
   * Returns the first (and possibly only) word in the Suggestion
   *
   * @return the first (and possibly only) word in the Suggestion
   */
  public String getFirstWord() {
    return firstWord;
  }

  /**
   * Sets the second word in the Suggestion
   *
   * @param s a String that will be used as the second word in the Suggestion
   */
  public void setSecondWord(String s) {
    secondWord = s;
  }

  /**
   * Returns the second word in the Suggestion
   *
   * @return the String second word in the Suggestion or null, if the Suggestion
   *         has only one word
   */
  public String getSecondWord() {
    return secondWord;
  }

}
