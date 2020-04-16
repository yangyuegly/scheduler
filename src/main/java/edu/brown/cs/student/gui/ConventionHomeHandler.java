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
    // get events in this convention from the database, display their names and 
    // give the user options to schedule, etc
    
    String conventionID = req.params(":id");
    String userEmail = req.cookie("user");
    
    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    
    // use the database to confirm that the user with userEmail is authorized to view the 
    // convention with conventionID
    
    // get convention name from database
    
    //send the events to the page somehow
    
    // fix
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "convName", "");
    
    return new ModelAndView(variables, "convention_home.ftl"); // fix
  }
}
