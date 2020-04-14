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
    String numDaysString = queryMap.value("numDays");
    String eventDuration = queryMap.value("eventDuration");
    String startTime = queryMap.value("startTime");
    String endTime = queryMap.value("endTime");
    
    int numDays = 0;
    
    try {
      numDays = Integer.parseInt(numDaysString);
    } catch (NumberFormatException err) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "errorMessage", "The number of days must be an integer.");
      
      return new ModelAndView(variables, "createconv.ftl");
    }
    
    
    
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "message", "");
    
    
    
    return new ModelAndView(variables, "home.ftl"); // fix
  }
}