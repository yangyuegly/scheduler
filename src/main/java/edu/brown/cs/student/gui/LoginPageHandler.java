package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display the login page. It implements TemplateViewRoute.
 */
public class LoginPageHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");

    if (userEmail != null) {
      // they are already logged in
      response.redirect("/account");
      return null;
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message", "");
    return new ModelAndView(variables, "login.ftl");
  }
}
