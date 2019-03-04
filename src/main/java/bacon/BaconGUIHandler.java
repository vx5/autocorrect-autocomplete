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

  public BaconGUIHandler() {
    resetMainVars();
    corpusLoaded = false;
  }

  public Map<String, Object> correct(int box, String toCorrect) {
    try {
      resetMainVars();
      if (!corpusLoaded) {
        // Loads actor corpus
        aCop.addWordsCorpus(op.getActorNames());
        aCop.setPrefixStatus(true);
      }
      // Funnel actual corrections
      ArrayList<String> suggestions = aCop.ac(toCorrect.split(" "));
      // Fills remainder
      while (suggestions.size() < 5) {
        suggestions.add("");
      }
      if (box == 0) {
        topAc = suggestions;
      } else {
        bottomAc = suggestions;
      }
    } catch (Exception e) {
      if (e.getMessage() == null) {
        pathError = "Please load a database of actors through the REPL!";
      } else {
        pathError = e.getMessage();
      }
    }
    return getMainMap();
  }

  public void resetMainVars() {
    pathError = "";
    pathResults = "";
    topAc = new ArrayList<String>();
    bottomAc = new ArrayList<String>();
    paths = new ArrayList<ArrayList<String>>();
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

  public Map<String, Object> loadActorPage(String actorEncodedId) {
    resetBPVars();
    try {
      targetCounterType = "Films";
      String targetId = URLDecoder.decode(actorEncodedId, "UTF-8");
      targetName = op.actorIdToName(targetId);
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
    return getBPMap();
  }

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
        // Adds all important elements to row
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
      pathError = e.getMessage();
    }
    // Finally, return required object
    return getPathObj();
  }

}
