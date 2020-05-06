package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.brown.cs.student.exception.UserAuthenticationException;

public class LoginCommandTest {
  String password = "helloword";
  LoginCommand login = new LoginCommand();

  // insert right login
  @Test
  public void executeRightLogin() {
    String email = "abby_goldberg@brown.edu";
    assertTrue(login.execute(email, "term_project") == true);
  }

  // insert wrong login
  @Test(expected = UserAuthenticationException.class)
  public void executeWrongUser() {
    login.execute("abby_goldberg@brown.edu", "incorrect_password");
  }

  // insert non existing login
  @Test(expected = UserAuthenticationException.class)
  public void executeNonExistingUser() {
    login.execute("shenandoah_duraideivamani1@brown.edu", "incorrect_password");
  }
}