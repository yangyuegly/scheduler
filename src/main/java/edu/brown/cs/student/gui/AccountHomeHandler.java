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
 * This class is used to display a user's account home page.  It implements TemplateViewRoute.
 */
public class AccountHomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    QueryParamsMap queryMap = req.queryMap();
    String email = queryMap.value("email");
    String password = queryMap.value("password");
    // User currUser = AuthenticationDatabase.checkLogin(email, password);
    
//    if (currUser == null) {
//      // invalid login
//      Map<String, Object> variables = ImmutableMap.of("title",
//          "Scheduler", "message", "Incorrect username or password.  Try again.");
//      return new ModelAndView(variables, "home.ftl");
//    }

    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "connectingPath", ""); // give it information about events and such
    return new ModelAndView(variables, "account.ftl");
  }
}
