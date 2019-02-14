package ac;

public class Suggestion {
  private String firstWord;
  private String secondWord;

  public Suggestion(String word) {
    firstWord = word;
    secondWord = null;
  }

  public String getFirstWord() {
    return firstWord;
  }

  public void setSecondWord(String s) {
    secondWord = s;
  }

  public String getSecondWord() {
    return secondWord;
  }

}
