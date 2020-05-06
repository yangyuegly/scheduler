package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.student.exception.UserAuthenticationException;

/**
 * Class to test the Register Command
 *
 */
public class RegisterCommandTest {
  String password = "helloword";
  RegisterCommand register = new RegisterCommand();

  // insert right register
//  @Test
  public void executeRightLogin() {
    String email = "durais1@gmail.com";
    assertTrue(register.execute(email, "term_project") == true);
  }

  // user already exists
  @Test(expected = UserAuthenticationException.class)
  public void executeWrongUser() {
    register.execute("abby_goldberg@brown.edu", "password");
  }
}
