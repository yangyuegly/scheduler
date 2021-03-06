package edu.brown.cs.student.accounts;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;

/**
 * This class is used to represent a user (a conference organizer).
 */
public class User {

  /**
   * These are fields for this class.
   *
   * email - a String, which represents the user's email conventions - a List of conventions, which
   * represents the conventions that this user is managing
   */
  private String email;
  private String password;
  private List<Convention> conventions = new ArrayList<>();

  /**
   * This is a constructor for this class.
   *
   * @param userEmail - a String, which represents the user's email
   */
  public User(String userEmail) {
    this.email = userEmail;
  }

  /**
   * This method is used to get the conventions that this user is managing.
   *
   * @return a List of conventions, which represents the conventions that this user is managing
   */
  public List<Convention> getConventions() {
    DatabaseUtility db = new DatabaseUtility();

    return db.getUserConventions(email);
  }

}
