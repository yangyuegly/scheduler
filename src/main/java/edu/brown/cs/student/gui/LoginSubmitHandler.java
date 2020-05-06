package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.exception.UserAuthenticationException;
import edu.brown.cs.student.scheduler.LoginCommand;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to handle requests to log in. It implements TemplateViewRoute.
 */
public class LoginSubmitHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    QueryParamsMap queryMap = request.queryMap();
    String email = queryMap.value("email");
    String password = queryMap.value("password");
    LoginCommand loginComm = new LoginCommand();

    try {
      loginComm.execute(email, password);

    } catch (UserAuthenticationException e) {
      String message = e.getMessage();

      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message", message);
      return new ModelAndView(variables, "login.ftl");
    }

    response.cookie("user", email);

    // go to the user's account page
    response.redirect("/account");

    return null; // this line will not be reached
  }

}
