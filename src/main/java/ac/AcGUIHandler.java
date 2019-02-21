package ac;

import java.util.ArrayList;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.QueryParamsMap;
import stringmanipulation.StringOps;

/**
 * @author vx5
 *
 *         Handler that manages changes to the Autocorrect GUI, which it does
 *         through a reference to the AcCoordinator object that this Autocorrect
 *         implementation is using to manage all of the different
 *         "Autocorrecting fields", which is, all of the different use cases
 *         that require their own, distinct, Autocorrects.
 */
public class AcGUIHandler {
  // Stores AcCoordinator instance that empowers this handler
  private AcCoordinator c;
  // Stores main page variables
  private ArrayList<String> suggestions;
  private String mainErrorStr;
  // Stores settings page variables
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

  /**
   * Resets all variables that represent variables on "acmain.ftl" template to
   * their default values (some of these values are altered farther in the
   * relevant JavaScript file, autocorrect.js).
   */
  public void resetMainVars() {
    // Resets and populates the suggestions list
    suggestions = new ArrayList<String>();
    suggestions.add("");
    suggestions.add("");
    suggestions.add("");
    suggestions.add("");
    suggestions.add("");
    // Clears the error message String on the main page
    mainErrorStr = "";
  }

  /**
   * Returns a map that a Spark route can use to populate the "acmain.ftl"
   * template which the current, desired values.
   *
   * @return Map that represents the server's current values for each of the
   *         variables to be represented on "acmain.ftl"
   */
  public Map<String, Object> getMainMap() {
    // Constructs map with all needed variables using ImmutableMap.Builder
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("oneRow", suggestions.get(0)).put("twoRow", suggestions.get(1))
        .put("threeRow", suggestions.get(2)).put("fourRow", suggestions.get(3))
        .put("fiveRow", suggestions.get(4)).put("mainError", mainErrorStr)
        .build();
    // Returns map
    return m;
  }

  /**
   * Resets all variables that represent variables on "acsettings.ftl" template
   * to their default values (some of these values are altered farther in the
   * relevant JavaScript file, acsettings.js).
   */
  public void resetSetVars() {
    // Resets everything at the core
    c.getOp(0).reset();
    // Resets all superficial display variables
    setErrorStr = "";
    setWinStr = "[Message will display here after you attempt to change "
        + "the settings]";
    filepaths = c.getOp(0).getCorpuses();
  }

  /**
   * Returns a map that a Spark route can use to populate the "acsettings.ftl"
   * template which the current, desired values.
   *
   * @return Map that represents the server's current values for each of the
   *         variables to be represented on "acsettings.ftl"
   */
  public Map<String, Object> getSetMap() {
    // Rechecks for any new corpuses
    filepaths = c.getOp(0).getCorpuses();
    // Builds map
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("win", setWinStr).put("err", setErrorStr).put("corpora", filepaths)
        .build();
    // Returns map
    return m;
  }

  /**
   * Returns a map that represents the relevant AcOperator's current settings.
   *
   * @return a map that stores all of the relevant AcOperator's current settings
   */
  public Map<String, Object> getSettings() {
    // Gets the operator for this coordinator
    AcOperator o = c.getOp(0);
    // Builds map
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("prefixVal", o.getPrefixStatus())
        .put("whitespaceVal", o.getWsStatus())
        .put("smartVal", o.getSmartStatus())
        .put("ledVal", Integer.toString(o.getLedSetting())).build();
    // Returns map
    return m;
  }

  /**
   * Processes an Autocorrect attempt into a map that represents all of the
   * output suggestions.
   *
   * @param toCorrect the String input to an Autocorrect attempt
   * @return a map representing the output suggestions
   */
  public Map<String, Object> correct(String toCorrect) {
    // Cleans the string to be Autocorrected
    String[] toCorrectSplit = StringOps.cleanInput(toCorrect).split(" ");
    ArrayList<String> output = new ArrayList<String>();
    // Attempts to autocorrect if a proper String was received
    if (toCorrect.length() > 0) {
      try {
        // Obtains autocorrect output
        output = c.getOp(0).ac(toCorrectSplit);
        // Correct error, if necessary
        mainErrorStr = "";
      } catch (Exception e) {
        // If corpus was not yet loaded, the
        mainErrorStr = "Please load a corpus before typing — "
            + "you can load a corpus by clicking <i>settings</i> "
            + "or via the REPL";
        return this.getMainMap();
      }
    }
    // Clears and replaces the suggestions
    suggestions = new ArrayList<String>();
    for (String s : output) {
      suggestions.add(s);
    }
    // Populates remainder of suggestions with empty String outputs
    // which will not be replaced
    while (suggestions.size() < 5) {
      suggestions.add("");
    }
    // Returns the updated map for "acmain.ftl"
    return this.getMainMap();
  }

  /**
   * Updates the settings of this implementation's relevant AcOperator instance.
   *
   * @param qm QueryParamsMap from request sent from GUI
   * @return updated map of variables for "acsettings.ftl"
   */
  public Map<String, Object> fixSettings(QueryParamsMap qm) {
    // Clear the error string again
    setErrorStr = "";
    // Extracts all raw Strings of settings
    String prefixSet = qm.value("prefix");
    String wsSet = qm.value("whitespace");
    String smartSet = qm.value("smart");
    String ledStr = qm.value("led");
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
    // Reads string containing all corpora paths
    String corporaPaths = qm.value("filepaths");
    // Split files by commas, as instructed in GUI
    String[] splitPaths = corporaPaths.split(",");
    for (String s : splitPaths) {
      String sTrue = StringOps.cleanInput(s);
      // For each valid string
      if (sTrue.length() > 0) {
        try {
          o.addCorpus("data/autocorrect/" + sTrue);
        } catch (Exception e) {
          setWinStr = "";
          setErrorStr = "Error in reading file: " + e.getMessage();
          return this.getSetMap();
        }
      }
    }
    // Reloads corpus list
    filepaths = c.getOp(0).getCorpuses();
    // Sets the win String to an affirming message
    setWinStr = "All settings up-to-date!";
    // Returns the current variable map
    return this.getSetMap();
  }

}
