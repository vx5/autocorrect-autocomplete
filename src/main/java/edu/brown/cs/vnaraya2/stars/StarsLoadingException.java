package edu.brown.cs.vnaraya2.stars;

/**
 * Exception to represent issues related to loading the Stars, whether in
 * reading the CSV file itself or in constructing the Stars from the CSV file.
 *
 * @author vx5
 */
public class StarsLoadingException extends Exception {
  // Required implementation of Superclass field
  private static final long serialVersionUID = 1L;

  /**
   * Constructor that stores error message specific to where the Exception is
   * thrown.
   *
   * @param errorMessage error message to be passed to either REPL or GUI
   */
  public StarsLoadingException(String errorMessage) {
    super(errorMessage);
  }

}
