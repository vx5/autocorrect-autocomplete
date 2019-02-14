package ac;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import filereader.StringOps;
import spark.QueryParamsMap;

/**
 * Handler that manages changes to the Autocorrect GUI, which it does through a
 * reference to the AcCoordinator object that this Autocorrect implementation is
 * using to manage all of the different "Autocorrecting fields", which is, all
 * of the different use cases that require their own, distinct, Autocorrects.
 *
 * @author vx5
 */
public class AcGUIHandler {
  // Stores AcCoordinator instance that empowers this handler
  private AcCoordinator c;
  // Stores main page variables
  private ArrayList<String> suggestions;
  private String mainErrorStr;
  // Stores settings page variables
  private String setTitleStr;
  private String setErrorStr;
  private String setWinStr;
  // Stores list of filepaths
  private ArrayList<String> filepaths;

  /**
   * Constructs AcGUIHandler using the coordinator that is being used in this
   * implementation to manage one or more AcOperators.
   *
   * @param coord AcCoordinator to be stored in this class
   */
  public AcGUIHandler(AcCoordinator coord) {
    // Makes required assignment
    c = coord;
    // Initialize all String variables
    this.resetMainVars();
    this.resetSetVars();
  }

  public void resetMainVars() {
    suggestions = new ArrayList<String>();
    suggestions.add("");
    suggestions.add("");
    suggestions.add("");
    suggestions.add("");
    suggestions.add("");
    mainErrorStr = "";
  }

  public Map<String, Object> getMainMap() {
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("oneRow", suggestions.get(0)).put("twoRow", suggestions.get(1))
        .put("threeRow", suggestions.get(2)).put("fourRow", suggestions.get(3))
        .put("fiveRow", suggestions.get(4)).put("mainError", mainErrorStr)
        .build();
    return m;
  }

  public void resetSetVars() {
    setTitleStr = "Settings";
    setErrorStr = "";
    setWinStr = "A message will display here after you attempt to change "
        + "the settings";
    filepaths = new ArrayList<String>();
  }

  public Map<String, Object> getSetMap() {
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("title", setTitleStr).put("corpora", filepaths).build();
    return m;
  }

  public Map<String, Object> correct(String toCorrect) {
    try {
      String[] toCorrectSplit = StringOps.cleanInput(toCorrect).split(" ");
      LinkedList<String> output = new LinkedList<String>();
      if (toCorrect.length() > 0) {
        try {
          output = c.getOp(0).ac(toCorrectSplit);
          // Correct error, if necessary
          mainErrorStr = "";
        } catch (Exception e) {
          mainErrorStr = "Please load a corpus before typing -- you can load a corpus"
              + " by clicking <i>settings</i> or via the REPL";
          return this.getMainMap();
        }
      }
      suggestions = new ArrayList<String>();
      for (String s : output) {
        suggestions.add(s);
      }
      while (suggestions.size() < 5) {
        suggestions.add("");
      }
      return this.getMainMap();

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
  }

  public Map<String, Object> fixSettings(QueryParamsMap qm) {
    // Extracts all raw Strings of settings
    String prefixSet = qm.value("prefix");
    String wsSet = qm.value("whitespace");
    String ledStr = qm.value("led");
    String smartSet = qm.value("smart");
    // Gets operator being manipulated in this implementation
    AcOperator o = c.getOp(0);
    // Parses integer from led, prints error if invalid
    String parseErr = "Please enter a positive integer in the LED field";
    try {
      int newLed = Integer.parseInt(ledStr);
      if (newLed < 0) {
        setErrorStr = parseErr;
      } else {
        o.setLedSetting(newLed);
      }
    } catch (NumberFormatException e) {
      setErrorStr = parseErr;
    }
    // If case of error, return after this point
    if (setErrorStr.length() > 0) {
      // Eliminates win String
      setWinStr = "";
      return this.getSetMap();
    }
    // Otherwise, update all settings accordingly
    // Updates prefix setting
    boolean newPrefixStatus = false;
    if (prefixSet.contentEquals("on")) {
      newPrefixStatus = true;
    }
    o.setPrefixStatus(newPrefixStatus);
    // Updates whitespace setting
    boolean newWsStatus = false;
    if (wsSet.contentEquals("on")) {
      newWsStatus = true;
    }
    o.setWsStatus(newWsStatus);
    // Updates Smart setting
    boolean newSmartStatus = false;
    if (smartSet.contentEquals("on")) {
      newSmartStatus = true;
    }
    o.setSmartStatus(newSmartStatus);
    // Reads string containing

    // Sets the win String to an affirming message
    setWinStr = "All settings up-to-date!";
    // Returns the current variable map
    return this.getSetMap();
  }

}
