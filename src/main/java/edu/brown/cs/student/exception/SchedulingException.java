package edu.brown.cs.student.exception;

/**
 * This class represents an exception that is thrown when a schedule cannot be formed. It extends
 * NullPointerException.
 */
public class SchedulingException extends NullPointerException {

  /**
   * This is a field for this class.
   *
   * serialVersionUID - a long, which is an identifier for this exception
   */
  private static final long serialVersionUID = 2057784884408164506L;

  /**
   * This is a constructor for this class.
   *
   * @param message - the error message
   */
  public SchedulingException(String message) {
    super(message);
  }
}
