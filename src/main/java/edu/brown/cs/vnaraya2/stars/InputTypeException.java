package edu.brown.cs.vnaraya2.stars;

/**
 * @author vx5
 *
 *         Exception to represent instances where the user enters input of the
 *         wrong type.
 */
public class InputTypeException extends Exception {
  // Required implementation of Superclass field
  private static final long serialVersionUID = 1L;

  /**
   * Constructor that stores error message specific to where the Exception is
   * thrown.
   *
   * @param errorMessage message to be passed to user
   */
  public InputTypeException(String errorMessage) {
    super(errorMessage);
  }
}
