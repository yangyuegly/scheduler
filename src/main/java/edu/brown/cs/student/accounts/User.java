package edu.brown.cs.student.accounts;

import java.util.ArrayList;
import java.util.List;

import edu.brown.cs.student.scheduler.Convention;

/**
 * This class is used to represent a user (a conference organizer).
 */
public class User {

  /**
   * These are fields for this class.
   *
   * email - a String, which represents the user's email
   * conventions - a List of conventions, which represents the conventions that
   *   this user is managing
   */
  private String email;
  private List<Convention> conventions = new ArrayList<>();
  
  /**
   * This is a constructor for this class.
   *
   * @param userEmail - a String, which represents the user's email
   */
  public User(String userEmail) {
    email = userEmail;
  }

  /**
   * This method is used to get the conventions that this user is managing.
   *
   * @return a List of conventions, which represents the conventions that
   *   this user is managing
   */
  public List<Convention> getConventions() {
    return conventions;
    //return Database.getConventionsNamesIds(email); 

  }

}
