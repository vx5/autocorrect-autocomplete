package edu.brown.cs.vnaraya2.stars;

/**
 * @author vx5
 *
 *         Exception to represent instances where the user enters the wrong
 *         structure of input -- in essence, it represents all errors not
 *         related to the user entering input of the wrong type.
 */
public class InputValidityException extends Exception {
  // Required implementation of Superclass field
  private static final long serialVersionUID = 1L;

  /**
   * Constructor that stores error message specific to where the Exception is
   * thrown.
   *
   * @param errorMessage message to be passed on to user
   */
  public InputValidityException(String errorMessage) {
    super(errorMessage);
  }
}
