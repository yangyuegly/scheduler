package edu.brown.cs.student.exception;

public class UserAuthenticationException extends NullPointerException {
  public UserAuthenticationException(String message) {
    super(message);
  }
}