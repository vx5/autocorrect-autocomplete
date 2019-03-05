package bacon;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import ac.AcOperator;
import paths.PathNode;

/**
 * @author vx5
 *
 *         Class that handles all variables used for population of .ftl
 *         templates relevant to Bacon GUI.
 */
public class BaconGUIHandler {
  // Stores BaconOperator instance that empowers this handler
  private final BaconOperator op = BaconOperator.getInst();
  // Stores AcCoordinator that manages autocorrecting functionality
  private AcOperator aCop = new AcOperator();
  private boolean corpusLoaded;
  // Stores main page variables
  private ArrayList<String> topAc;
  private ArrayList<String> bottomAc;
  // Stores path-contingent variables
  private String pathError;
  private String pathResults;
  // Stores path-contingent JSON variables
  private ArrayList<ArrayList<String>> paths;
  // Stores back page variables
  private String targetName;
  private String targetLinks;
  private String targetCounterType;

  /**
   * Constructor that initializes all variables on main page, and stores that
   * corpus has not yet been loaded.
   */
  public BaconGUIHandler() {
    resetMainVars();
    corpusLoaded = false;
  }

  /**
   * Process all autocorrecting functionality by directing String to be
   * autocorrected to AcOperator instance.
   *
   * @param box       integer represeting which box is being autocorrected, 0
   *                  for the first box, 1 for the second box
   * @param toCorrect the String to be autocorrected
   * @return appropriate set of variable suggestions from AcOperator
   */
  public Map<String, Object> correct(int box, String toCorrect) {
    try {
      // Re-checks all variables on main page
      resetMainVars();
      // If corpus is not loaded, perform loading operation
      if (!corpusLoaded) {
        // Loads actor corpus
        aCop.addWordsCorpus(op.getActorNames());
        // This autocorrect will only use the prefix method
        aCop.setPrefixStatus(true);
      }
      // Funnel actual corrections
      ArrayList<String> suggestions = aCop.ac(toCorrect.split(" "));
      // Fills remainder
      while (suggestions.size() < 5) {
        suggestions.add("");
      }
      // Assigns suggestions to appropriate box's variable
      if (box == 0) {
        topAc = suggestions;
      } else {
        bottomAc = suggestions;
      }
    } catch (Exception e) {
      // Handles exceptions appropriately, by submitting most errors, but by
      // recognizing the null error as corresponding to the absence of a
      // database
      if (e.getMessage() == null) {
        pathError = "Please load a database of actors through the REPL!";
      } else {
        pathError = e.getMessage();
      }
    }
    // Returns requisite main page map
    return getMainMap();
  }

  /**
   * Resets all variables pertinent to main Bacon page.
   */
  public void resetMainVars() {
    pathError = "";
    pathResults = "";
    // Clears ArrayList variables
    topAc = new ArrayList<String>();
    bottomAc = new ArrayList<String>();
    paths = new ArrayList<ArrayList<String>>();
    // Populates boths suggestion variables with empty Strings
    for (int i = 0; i < 5; i++) {
      topAc.add("");
      bottomAc.add("");
    }
  }

  private void resetBPVars() {
    targetName = "";
    targetLinks = "";
    targetCounterType = "";
  }

  /**
   * Loads an actor page (that lists all films the actor was in).
   *
   * @param actorEncodedId the id of the actor to be used as the basis for this
   *                       page, in encoded form (from the page URL)
   * @return map that specifies appropriate variable values for page
   */
  public Map<String, Object> loadActorPage(String actorEncodedId) {
    // Resets variables for page
    resetBPVars();
    try {
      // Specifies type of page being loaded
      targetCounterType = "Films";
      // Decodes the URL to the actor's id, name
      String targetId = URLDecoder.decode(actorEncodedId, "UTF-8");
      targetName = op.actorIdToName(targetId);
      // Accesses and stores all references to films as links in HTML
      HashSet<String> filmIds = op.getFilmIds(targetId);
      Iterator<String> i = filmIds.iterator();
      while (i.hasNext()) {
        String filmId = i.next();
        String filmName = op.filmIdToName(filmId);
        String filmEncodedId = URLEncoder.encode(filmId, "UTF-8");
        targetLinks += "<a href=/bacon/film/" + filmEncodedId + ">" + filmName
            + "</a><br><br>";
      }
    } catch (SQLException | UnsupportedEncodingException e) {
      // No exception should ever occur here
    }
    // Returns requisite backpage map
    return getBPMap();
  }

  /**
   * Loads a film page (that lists all actors in the film).
   *
   * @param filmEncodedId the id of the film to be used as the page basis, in
   *                      encoded form (from the page URL)
   * @return map that specifies appropriate variable values for page
   */
  public Map<String, Object> loadFilmPage(String filmEncodedId) {
    resetBPVars();
    try {
      targetCounterType = "Actors";
      String filmId = URLDecoder.decode(filmEncodedId, "UTF-8");
      targetName = op.filmIdToName(filmId);
      HashSet<String> actorIds = op.getActorIds(filmId);
      Iterator<String> i = actorIds.iterator();
      while (i.hasNext()) {
        String actorId = i.next();
        String actorName = op.actorIdToName(actorId);
        String actorEncodedId = URLEncoder.encode(actorId, "UTF-8");
        targetLinks += "<a href=/bacon/actor/" + actorEncodedId + ">"
            + actorName + "</a><br><br>";
      }
    } catch (SQLException | UnsupportedEncodingException e) {
      // Should never be triggered
    }
    return getBPMap();
  }

  private Map<String, Object> getBPMap() {
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("targetName", targetName).put("targetLinks", targetLinks)
        .put("targetCounterType", targetCounterType).build();
    return m;
  }

  /**
   * Returns map of variables specific to main Bacon page.
   *
   * @return map of variables specific to main Bacon page
   */
  public Map<String, Object> getMainMap() {
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("pathError", pathError).put("pathResults", pathResults)
        .put("OneTrow", topAc.get(0)).put("TwoTrow", topAc.get(1))
        .put("ThreeTrow", topAc.get(2)).put("FourTrow", topAc.get(3))
        .put("FiveTrow", topAc.get(4)).put("OneBrow", bottomAc.get(0))
        .put("TwoBrow", bottomAc.get(1)).put("ThreeBrow", bottomAc.get(2))
        .put("FourBrow", bottomAc.get(3)).put("FiveBrow", bottomAc.get(4))
        .build();
    return m;
  }

  private Map<String, Object> getPathObj() {
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("pathError", pathError).put("paths", paths).build();
    return m;
  }

  /**
   * Returns map of variables in main page that represents the outcome of a
   * search for the path between two actors.
   *
   * @param firstActor  the name of the actor to be used as the start of the
   *                    path
   * @param secondActor the name of the actor the path should end at
   * @return map of variables in main page that represent a successful OR
   *         unsuccessful outcome of the path search
   */
  public Map<String, Object> path(String firstActor, String secondActor) {
    // Clear all existing main page variables
    resetMainVars();
    // Attempt to find path -- if any exception is found, store in pathError
    try {
      // Stores path of nodes
      ArrayList<PathNode> pathNodes = op.getPath(firstActor, secondActor);
      // Iterates through all pathNodes
      for (int i = pathNodes.size() - 1; i > 0; i--) {
        // Generates new ArrayList to store each row
        ArrayList<String> row = new ArrayList<String>();
        // Includes all information required to populate rows
        // with both actor's name, joint film name, and links to
        // their respective pages
        row.add(op.actorIdToName(pathNodes.get(i).getId()));
        row.add(URLEncoder.encode(pathNodes.get(i).getId(), "UTF-8"));
        row.add(op.actorIdToName(pathNodes.get(i - 1).getId()));
        row.add(URLEncoder.encode(pathNodes.get(i - 1).getId(), "UTF-8"));
        row.add(op.filmIdToName(pathNodes.get(i - 1).getPrevEdge().getId()));
        row.add(URLEncoder.encode(pathNodes.get(i - 1).getPrevEdge().getId(),
            "UTF-8"));
        // Adds row to overall path return variable
        paths.add(row);
      }
    } catch (Exception e) {
      // If Exception is thrown, populate pathError variable
      pathError = e.getMessage();
    }
    // Finally, return required object
    return getPathObj();
  }

}
