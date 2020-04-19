package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class CalendarHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {

    // get events in this convention from the database, display their names and 
    // give the user options to schedule, etc
    
    String conventionID = req.params(":id");
    String userEmail = req.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      String currUserMessage = "<a href=/home>Log in</a>";
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "currUserMessage", currUserMessage, "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    

//     boolean authorized = Database.checkPermission(userEmail, conventionID);
//    if (!authorized) {
//      Map<String, Object> variables = ImmutableMap.of("title",
//          "Scheduler");
//      return new ModelAndView(variables, "unauthorized.ftl");
//    }
    String name = "";
    //name = Database.getConventionNameFromID();
    
    String currUserMessage = "<label>Logged in as <a href=/account>" + userEmail + "</a></label>" +
        "<br><a href=/logout>Log out</a>";
    
    Map<String, Object> variables = ImmutableMap.of("title",
      "Scheduler", "currUserMessage", currUserMessage, "name", name);
    return new ModelAndView(variables, "calendar_page.ftl");
  }

    
}
