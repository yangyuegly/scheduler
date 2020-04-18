package edu.brown.cs.student.gui;
// don't think we need it, all commented out
import java.util.Map;


import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display a user's account home page when the user is logged in.  
 *   It implements TemplateViewRoute.
 */
public class AccountHomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    String userEmail = req.cookie("user");
    
    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    //DATABASE CALL -- just the convention name and id
    // get convention data using req.cookie("user"); to get the email
    
    // store convention links using ids in "/convention/:id" format, send to page

    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionLinks", ""); // give it information about events and such
    return new ModelAndView(variables, "account.ftl");
  }
}
