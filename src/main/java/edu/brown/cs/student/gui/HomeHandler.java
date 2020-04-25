package edu.brown.cs.student.gui;

//integrated
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to handle requests to the front page of our website. It implements
 * TemplateViewRoute.
 */
public class HomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
//    String userEmail = request.cookie("user");
//
//    if (userEmail != null) {
//      // they are already logged in
//      response.redirect("/account");
//      return null;
//    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message", "");
    return new ModelAndView(variables, "home.ftl");
  }
}