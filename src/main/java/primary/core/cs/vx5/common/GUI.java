package primary.core.cs.vx5.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.google.gson.Gson;

import ac.AcCoordinator;
import ac.AcGUIHandler;
import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * @author vx5
 *
 *         Object that manages parts of the GUI that the stencil had contained
 *         within the Main class. Includes all Spark Routes and related
 *         handlers, and delegates the actual processing of Stars GUI inputs to
 *         the StarsGUIHandler class and of Autocorrect GUI inputs to the
 *         AcGUIHandler class.
 */
public class GUI {
  // Stores the AcGUIHandler instance
  private static AcGUIHandler ah;
  // Stores the GSON instance for Autocorrect
  private static final Gson GSON = new Gson();

  /**
   * Constructor for GUI class, which simply takes in this project's instances
   * of AllStars and KDTree so that they may be passed to the constructor of the
   * StarsGUIHandler object, which actually processes GUI input.
   *
   * @param ac instance of AcCoordinator that can manage this instance of
   *           Autocorrect implementation
   */
  public GUI(AcCoordinator ac) {
    ah = new AcGUIHandler(ac);
  }

  /**
   * Method that was in Main class in stencil code, that handles the launching
   * of the Spark Server and the setup of Spark routes.
   *
   * @param port the port value to be used for the Spark server
   */
  @SuppressWarnings("unchecked")
  public void runSparkServer(int port) {
    Spark.port(Main.getHerokuAssignedPort());
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes for Autocorrect
    // Spark route for autocorrect front page
    Spark.get("/autocorrect", new AcMainBeginHandler(), freeMarker);
    // Spark route for corrections made through JS
    Spark.post("/correct", new AcCorrectHandler());
    // Spark route for tracking user selections
    Spark.post("/select", new AcSelectHandler());
    // Spark route that loads settings page
    Spark.get("/autocorrect/settings", new AcSetBeginHandler(), freeMarker);
    // Spark route for saving settings page
    Spark.post("/autocorrect/settings/save", new AcSetSaveHandler(),
        freeMarker);
    // Spark route for resetting settings page
    Spark.post("/autocorrect/settings/default", new AcSetResetHandler(),
        freeMarker);
    // Spark route for getting current settings
    Spark.get("/getsettings", new AcGetSetHandler());
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  /**
   * @author vx5
   *
   *         Handler route that intakes, stores option selected by user.
   */
  private static class AcSelectHandler implements Route {

    @Override
    public String handle(Request request, Response response) throws Exception {
      // Pulls the input string that was used
      QueryParamsMap qm = request.queryMap();
      String selection = qm.value("selection");
      // If valid word, add to word tracker
      if (selection != null) {
        // Obtains last word
        String[] splitSelection = selection.split(" ");
        String lastWord = splitSelection[splitSelection.length - 1];
        // Adds last word to selected word tracker
        ah.addSelectedWord(lastWord);
      }
      return GSON.toJson(ah.correct(selection));
    }
  }

  /**
   * @author vx5
   *
   *         Handler route that supplies initialized variable values for
   *         "acmain.ftl" template.
   */
  private static class AcMainBeginHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response)
        throws Exception {
      ah.resetMainVars();
      return new ModelAndView(ah.getMainMap(), "acmain.ftl");
    }

  }

  /**
   * @author vx5
   *
   *         Handler route that supplies initialized variable values for
   *         "acsettings.ftl" template.
   */
  private static class AcSetBeginHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response)
        throws Exception {
      return new ModelAndView(ah.getSetMap(), "acsettings.ftl");
    }
  }

  /**
   * @author vx5
   *
   *         Handler route that supplies default variable values for
   *         "acsettings.ftl" template.
   */
  public static class AcSetResetHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request request, Response response)
        throws Exception {
      ah.resetSetVars();
      return new ModelAndView(ah.getSetMap(), "acsettings.ftl");
    }
  }

  /**
   * @author vx5
   *
   *         Handler route that delegates processing and saving of new settings
   *         values from form in "acsettings.ftl" template, as well as
   *         population of new variable values.
   */
  public static class AcSetSaveHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response)
        throws Exception {
      // Pulls the current set of settings input
      QueryParamsMap qm = request.queryMap();
      return new ModelAndView(ah.fixSettings(qm), "acsettings.ftl");
    }

  }

  /**
   * @author vx5
   *
   *         Handler route that receives and replies to JSON request to supply
   *         Autocorrect functionality to GUI.
   *
   */
  public static class AcCorrectHandler implements Route {

    @Override
    public String handle(Request request, Response response) throws Exception {
      // Pulls the input string that was used
      QueryParamsMap qm = request.queryMap();
      String toCorrect = qm.value("toCorrect");
      return GSON.toJson(ah.correct(toCorrect));
    }

  }

  /**
   * @author vx5
   *
   *         Handler route that receives and replies to JSON request to supply
   *         current settings values of this Autocorrect.
   */
  public static class AcGetSetHandler implements Route {

    @Override
    public String handle(Request request, Response response) throws Exception {
      // TODO Auto-generated method stub
      return GSON.toJson(ah.getSettings());
    }

  }

  /**
   * @author vx5
   *
   *         Display an error page when an exception occurs in the server.
   */
  @SuppressWarnings("rawtypes")
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
