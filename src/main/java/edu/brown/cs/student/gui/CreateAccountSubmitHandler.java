package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.exception.UserAuthenticationException;
import edu.brown.cs.student.scheduler.RegisterCommand;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

//integrated but register command needs to return a boolean
/**
 * This class is used to add a newly created account to the database and send the new user to their
 * account's home page.
 */
public class CreateAccountSubmitHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    QueryParamsMap queryMap = request.queryMap();
    String email = queryMap.value("email");
    String password = queryMap.value("password");
    RegisterCommand registerComm = new RegisterCommand();

    try {
      registerComm.execute(email, password);

    } catch (UserAuthenticationException err) {
      String message = err.getMessage();

      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "errorMessage",
          message);
      return new ModelAndView(variables, "create_account.ftl");
    }

    // sets the cookie so it expires after two hours
    response.cookie("user", email, 120 * 60 * 1000);
    response.redirect("/account");

    return null; // this line will not be reached
  }

}
