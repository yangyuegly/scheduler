package edu.brown.cs.student.exception;

/**
 *Exception for invalid user authentication
 */
public class UserAuthenticationException extends NullPointerException {
  /**
   * Constructor
   * @param message - the error message
   */
  public UserAuthenticationException(String message) {
    super(message);
  }
}