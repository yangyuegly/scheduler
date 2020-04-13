package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to handle requests to the home page of a convention.
 */
public class ConventionHomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    QueryParamsMap queryMap = req.queryMap();
    String name = queryMap.value("convName");
    String startDate = queryMap.value("startDate");
    String endDate = queryMap.value("endState");
    String startTime = queryMap.value("startTime");
    String endTime = queryMap.value("endTime");
    
    
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "message", "");
    
    
    
    return new ModelAndView(variables, "home.ftl");
  }
}