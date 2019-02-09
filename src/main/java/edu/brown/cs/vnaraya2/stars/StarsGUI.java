package edu.brown.cs.vnaraya2.stars;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import freemarker.template.Configuration;
import kdtrees.KDTree;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * @author vx5
 *
 *         Object that manages parts of the GUI that the stencil had contained
 *         within the Main class. Includes all Spark Routes and related
 *         handlers, and delegates the actual processing of GUI inputs to the
 *         StarsGUIHandler class
 */
public class StarsGUI {
  // Stores the StarsGUIHandler instance
  private static StarsGUIHandler gh;

  /**
   * Constructor for StarsGUI class, which simply takes in this project's
   * instances of AllStars and KDTree so that they may be passed to the
   * constructor of the StarsGUIHandler object, which actually processes GUI
   * input.
   *
   * @param allStars object that holds and manipulates all Stars
   * @param kdTree   instance of k-d Tree used for search operations
   */
  public StarsGUI(AllStars allStars, KDTree kdTree) {
    gh = new StarsGUIHandler(allStars, kdTree);
  }

  /**
   * Method that was in Main class in stencil code, that handles the launching
   * of the Spark Server and the setup of Spark routes.
   *
   * @param port the port value to be used for the Spark server
   */
  @SuppressWarnings("unchecked")
  public void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    // Initial Spark route given by stencil code
    Spark.get("/stars", new BeginHandler(), freeMarker);
    // Additional Spark route that responds to star load form
    Spark.post("/starsloaded", new LoadHandler(), freeMarker);
    // Additional Spark route that responds to command form
    Spark.post("/commandrun", new CommandHandler(), freeMarker);
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
   *         Handles the initial view of our Stars website.
   */
  private static class BeginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      gh.resetVars();
      // Initializes values for stars-loading section
      return new ModelAndView(gh.getMap(), "query.ftl");
    }
  }

  /**
   * @author vx5
   *
   *         Handles the submission and processing of the Stars Loading form.
   */
  private static class LoadHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response)
        throws Exception {
      // Gets the query map
      QueryParamsMap qm = request.queryMap();
      // Pulls the end of the filepath input
      String pathend = qm.value("filename");
      // Returns with map generated by GUI handler
      return new ModelAndView(gh.loadNeighbors(pathend), "query.ftl");
    }
  }

  /**
   * @author vx5
   *
   *         Handles the submission and processing of the command form, which
   *         relates to either the "neighbors" or "radius" search requests.
   */
  private static class CommandHandler implements TemplateViewRoute {

    @Override
    public ModelAndView handle(Request request, Response response)
        throws Exception {
      // Gets the query map
      QueryParamsMap qm = request.queryMap();
      // Returns with map generated by GUI handler
      return new ModelAndView(gh.doCommand(qm), "query.ftl");
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
