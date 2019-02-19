package stringmanipulation;

public class StringOps {

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

    return input;
  }

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
