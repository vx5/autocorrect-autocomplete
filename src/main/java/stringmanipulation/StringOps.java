package stringmanipulation;

/**
 * @author vx5
 *
 *         Class that houses functions that involve string alteration methods
 *         cleanInput() and removeWs().
 */
public final class StringOps {

  /**
   * Private, empty constructor, since this is a utility class.
   */
  private StringOps() {
  }

  /**
   * Removes numbers and punctuation from a String, as well as any repeat
   * spaces.
   *
   * @param input String to be cleaned
   * @return cleaned version of input String, in String form
   */
  public static String cleanInput(String input) {
    // Corrects all characters to their lower case versions
    input = input.toLowerCase();
    // Instantiates String of invalid characters
    String invalidChars = "!@#()'0123456789";
    // Iterate through input, replace invalid characters with spaces
    for (int i = 0; i < input.length(); i++) {
      //
      String oneCharStr = input.substring(i, i + 1); //
      if (invalidChars.contains(oneCharStr)) {
        input = input.replaceFirst(oneCharStr, " ");
      }
    } // Locates all dual-spaces
    while (input.indexOf("  ") != -1) {
      input = input.replaceAll("  ", " ");
    }
    // Returns adjusted String
    return input;
  }

  /**
   * Removes whitespace from either end of a String.
   *
   * @param input String whose whitespace should be removed
   * @return input String with before- and after-whitespace removed
   */
  public static String removeWs(String input) {
    // In valid cases of Strings
    if (input.length() > 0) {
      // Remove all beginning whitespace
      while (input.charAt(0) == ' ' && input.length() > 0) {
        input = input.substring(1);
      }
      // Remove all trailing whitespace
      while (input.charAt(input.length() - 1) == ' ' && input.length() > 0) {
        input = input.substring(0, input.length() - 1);
      }
    }
    // Return String with whitespace removed
    return input;
  }

}
