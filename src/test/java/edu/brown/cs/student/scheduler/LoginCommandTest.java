package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Base64;

import org.junit.Test;

import edu.brown.cs.student.exception.UserAuthenticationException;

public class LoginCommandTest {
  String password = "helloword";
  LoginCommand login = new LoginCommand();

  @Test
  public void executeTestSalt() {
    byte[] salt = RegisterCommand.getSalt();
    String encryptedPassword = RegisterCommand.encrypt(password, salt);
    String saltString = Base64.getEncoder().encodeToString(salt);
    String decryptedPassword = login.decryptePassword(encryptedPassword, saltString);
    assertEquals(password, decryptedPassword);
  }

  //insert right login
  @Test
  public void executeRightLogin() {
    String email = "abby_goldberg@brown.edu";
    assertTrue(login.execute(email, "term_project") == true);
  }

  //insert wrong login
  @Test(expected = UserAuthenticationException.class)
  public void executeWrongUser() {
    login.execute("abby_goldberg@brown.edu", "incorrect_password");
  }

  //insert non existing login
//  @Test(expected = UserAuthenticationException.class)
  public void executeNonExistingUser() {
    login.execute("shenandoah_duraideivamani1@brown.edu", "incorrect_password");
  }
}