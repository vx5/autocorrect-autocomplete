package edu.brown.cs.vnaraya2.stars;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

import com.google.common.collect.ImmutableMap;

import kdtrees.KDNode;
import kdtrees.KDTree;
import kdtrees.Spatial;
import spark.QueryParamsMap;

/**
 * @author vx5
 *
 *         Object that interprets the text input to the GUI and passes
 *         instructions on what to do to the StarsOperator object.
 */
public class StarsGUIHandler {
  // Stores instances of AllStars class for
  // Star name <-> ID conversions
  private AllStars allStars;
  // Stores instance of StarsOperator for
  // the passing of core algorithm instructions
  private StarsOperator op;

  // Stores instances of every variable to populate in
  // query.ftl (and linked files)
  private String titlestr;
  private String defaultstr;
  private String loadconfirmstr;
  private String loaderrorstr;
  private String cmderrorstr;
  private String listheaderstr;
  private LinkedList<LinkedList<String>> cmdoutputlist;

  // Stores frequently used messages
  private final String title = "Stars Project";
  private final String empty = "";
  private final String defaultLoadLine = "This is where a confirmation/error"
      + " message will appear";
  private final String preCmd = "This is where the Stars will be printed";

  // Stores whether Stars have been loaded already

  /**
   * Constructor that initializes all instance fields, including instantiating a
   * StarsOperator that is connected to the StarsGUI class's instances of
   * AllStars and the k-d tree class.
   *
   * @param thisAllStars instance of AllStars used by this project
   * @param thisKDTree   instance of k-d tree used for searches
   */
  public StarsGUIHandler(AllStars thisAllStars, KDTree thisKDTree) {
    // Initialize AllStars, k-d Tree instance fields from input
    allStars = thisAllStars;
    // Initializes new StarsOperator based on arguments
    op = new StarsOperator(allStars, thisKDTree);
    // Sets all variables to their default (page-first-loaded)
    // contents
    this.resetVars();
  }

  /**
   * Resets all String values to what they should be when the GUI page first
   * loads.
   */
  public void resetVars() {
    // Initializes all other values to what they should be
    // at the page loading step
    titlestr = title;
    defaultstr = defaultLoadLine;
    loadconfirmstr = empty;
    loaderrorstr = empty;
    cmderrorstr = empty;
    listheaderstr = preCmd;
    cmdoutputlist = new LinkedList<LinkedList<String>>();
  }

  /**
   * Returns a Map that links all page String variables to the variables they
   * represent in the GUI.
   *
   * @return a Map that links page variables to variables they represent in the
   *         GUI
   */
  public Map<String, Object> getMap() {
    // Creates map with all current given variable values,
    // builder is used since more than 5 key-value pairs
    // are needed in the Map
    Map<String, Object> m = new ImmutableMap.Builder<String, Object>()
        .put("title", titlestr).put("default", defaultstr)
        .put("loadconfirm", loadconfirmstr).put("loaderror", loaderrorstr)
        .put("cmderror", cmderrorstr).put("listheader", listheaderstr)
        .put("cmdoutput", cmdoutputlist).build();
    // Returns map
    return m;
  }

  /**
   * Coordinates the loading of Stars from a .csv file.
   *
   * @param pathEnd the end of the filepath to the .csv file
   * @return the current Map of variables in this class to the variables they
   *         represent in the GUI
   */
  public Map<String, Object> loadNeighbors(String pathEnd) {
    // Completes the filepath specified in the form
    String path = "data/stars/" + this.killTrail(pathEnd);
    // Reset all strings
    this.resetVars();
    // Remove default string for loading
    defaultstr = empty;
    // Stores integer number of stars loaded
    // Attempts to load stars found at path
    try {
      // Reads Stars from .csv file, adds to tree
      // Embeds entire process in desired printline
      loadconfirmstr = "Read " + op.stars(path) + " stars from " + path;
    } catch (StarsLoadingException e) {
      // Sets the error message based on exception
      loaderrorstr = "Error in loading Stars:\n" + e.getMessage();
    } catch (FileNotFoundException e) {
      // Sets the error message based on exception
      loaderrorstr = "Error in reading file:\n" + e.getMessage();
    } catch (IOException e) {
      // Sets the error message to something generic
      loaderrorstr = "IOException has been caught "
          + "-- please contact this program's manager";
    }
    // Returns new Map based on instance field strings
    return this.getMap();
  }

  /**
   * Coordinates the execution of a neighbors or radius command based on the
   * inputs to the GUI's command form.
   *
   * @param qm QueryParamsMap that contains inputs to GUI's command form
   * @return the current Map of variables in this class to the variables they
   *         represent in the GUI
   */
  public Map<String, Object> doCommand(QueryParamsMap qm) {
    // Clears all relevant existing String variables
    cmderrorstr = empty;
    listheaderstr = empty;
    cmdoutputlist = new LinkedList<LinkedList<String>>();
    // Check that stars were successfully loaded
    if (!allStars.starsLoaded()) {
      cmderrorstr = "Stars must first be successfully loaded in Step 1 "
          + "or through the terminal REPL";
    } else {
      // Get which command was input
      String command = this.killTrail(qm.value("command choice"));
      // Get bound input
      String bound = this.killTrail(qm.value("bound"));
      // Gets the name
      String starName = this.killTrail(qm.value("starname"));
      // Gets all input coordinates
      String x = this.killTrail(qm.value("xcoord"));
      String y = this.killTrail(qm.value("ycoord"));
      String z = this.killTrail(qm.value("zcoord"));
      try {
        // Checks for proper bound
        if (bound.length() == 0) {
          // Uses error message
          cmderrorstr = "Bound must be entered";
        } else if (starName.length() != 0) {
          // Attempts to get list of nodes from Star name
          cmdoutputlist = this
              .queueToList(op.commandStarName(command, bound, starName));
          //
          int size = cmdoutputlist.size();
          if (size > 0) {
            size--;
          }
          // Adjust header message
          listheaderstr = size + " stars found; displayed from "
              + "closest to farthest";
        } else if (x.length() != 0 && y.length() != 0 && z.length() != 0) {
          // Else, uses coordinates
          cmdoutputlist = this
              .queueToList(op.commandStarCoord(command, bound, x, y, z));
          // Calculates the number of Stars displayed, which must be
          // decremented if the number is nonzero, because then the
          // cmdoutputlist variable includes a header row to be printed
          // to the GUI
          int size = cmdoutputlist.size();
          if (size > 0) {
            size--;
          }
          // Adjust header message
          listheaderstr = size + " stars found; displayed from "
              + "closest to farthest";
        } else {
          // Else, error message
          cmderrorstr = "Either star name or all coordinate fields "
              + "must be filled out";
        }
      } catch (Exception e) {
        cmderrorstr = "Error: " + e.getMessage();
      }
    }
    // Returns new Map based on instance field strings
    return this.getMap();
  }

  private LinkedList<LinkedList<String>> queueToList(PriorityQueue<KDNode> q) {
    // Instantiates new list
    LinkedList<LinkedList<String>> list = new LinkedList<LinkedList<String>>();
    // If queue has at least one element,
    // add key to table
    if (q.size() > 0) {
      LinkedList<String> topRow = new LinkedList<String>();
      topRow.add("<font size=\"4\">Star ID</font>");
      topRow.add("<font size=\"4\">Star Name</font>");
      topRow.add("<font size=\"4\">x</font>");
      topRow.add("<font size=\"4\">y</font>");
      topRow.add("<font size=\"4\">z</font>");
      list.add(topRow);
    }
    // Iterates through all nodes, adds IDs to list
    while (q.peek() != null) {
      LinkedList<String> sublist = new LinkedList<String>();
      Spatial s = q.remove().getNodeObject();
      sublist.add(s.getID());
      String name = "";
      try {
        name = allStars.idToName(s.getID());
      } catch (InputValidityException e) {
        // Let this remain empty, this use of
        // IDToName should never throw
        // an exception
      }
      sublist.add(name);
      float[] coords = s.getCoordinates();
      sublist.add(Float.toString(coords[0]));
      sublist.add(Float.toString(coords[1]));
      sublist.add(Float.toString(coords[2]));
      // Adds this Star's list to the overall list
      list.add(sublist);
    }
    // Returns list
    return list;
  }

  private String killTrail(String s) {
    // Reduces whitespace off of end of String until there is none
    // or all that is left of the String is a single space character,
    // whichever comes first
    while (s.length() > 1 && s.charAt(s.length() - 1) == ' ') {
      s = s.substring(0, s.length() - 1);
    }
    return s;
  }
}
