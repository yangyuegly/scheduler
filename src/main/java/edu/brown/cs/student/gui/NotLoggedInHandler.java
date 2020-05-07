package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used when someone who is not logged in tries to access a page that they do not have
 * access to without an account It takes the user to the login page and displays a message
 * explaining that they need to login. It implements TemplateViewRoute.
 */
public class NotLoggedInHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
        "Please log in");
    return new ModelAndView(variables, "login.ftl");
  }
}
