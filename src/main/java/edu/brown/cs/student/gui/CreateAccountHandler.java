package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display the page to create a new account.
 */
public class CreateAccountHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String currUserEmail = request.cookie("user");

    if (currUserEmail != null) {
      // the user is logged in and does not need to make an account
      response.redirect("/account");
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "errorMessage", "");
    return new ModelAndView(variables, "create_account.ftl");
  }
}
