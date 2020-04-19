package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;

import java.util.Base64;

import org.junit.Test;

import edu.brown.cs.student.scheduler.LoginCommand;

public class LoginCommandTest {
  String password = "helloword";
  LoginCommand login = new LoginCommand();

  @Test
  public void executeTest() {
    byte[] salt = RegisterCommand.getSalt();
    String encryptedPassword = RegisterCommand.encrypt(password, salt);
    String saltString = Base64.getEncoder().encodeToString(salt);
    String decryptedPassword = login.decryptePassword(encryptedPassword, saltString);
    assertEquals(password, decryptedPassword);
  }
}