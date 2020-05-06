package edu.brown.cs.student.exception;

/**
 * This class represents an exception for invalid user authentication. It extends
 * NullPointerException.
 */
public class UserAuthenticationException extends NullPointerException {

  /**
   * This is a field for this class.
   *
   * serialVersionUID - a long, which is an identifier for this exception
   */
  private static final long serialVersionUID = 3107531416093829274L;

  /**
   * This is a constructor for this class.
   *
   * @param message - the error message
   */
  public UserAuthenticationException(String message) {
    super(message);
  }
}
