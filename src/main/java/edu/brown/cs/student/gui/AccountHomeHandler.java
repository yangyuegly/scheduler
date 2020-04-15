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
    // User currUser = AuthenticationDatabase.checkLogin(email, password);
    
//    if (currUser == null) {
//      // invalid login
//      Map<String, Object> variables = ImmutableMap.of("title",
//          "Scheduler", "message", "Incorrect username or password.  Try again.");
//      return new ModelAndView(variables, "home.ftl");
//    }
    
    // get convention data

    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionLinks", ""); // give it information about events and such
    return new ModelAndView(variables, "account.ftl");
  }
}
