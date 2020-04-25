package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display the unauthorized page that appears when a user tries to go to a
 * page for which they do not have access permission. It implements TemplateViewRoute.
 */
public class UnauthorizedHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler");
    return new ModelAndView(variables, "unauthorized.ftl");
  }
}
