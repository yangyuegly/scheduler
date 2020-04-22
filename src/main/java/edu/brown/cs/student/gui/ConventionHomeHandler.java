package edu.brown.cs.student.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.Event;
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
    

//     boolean authorized = Database.checkPermission(userEmail, conventionID);
//    if (!authorized) {
//      Map<String, Object> variables = ImmutableMap.of("title",
//          "Scheduler");
//      return new ModelAndView(variables, "unauthorized.ftl");
//    }

    
    // get convention object w/all events from database based on id
    
    
    Convention currConv = new Convention(conventionID);
    
    // for testing purposes: // delete!!!!!!!!!!@!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    currConv.setName("Book Signing");
    Event jkEvent = new Event(1, "J. K. Rowling Book Signing");
    currConv.addEvent(jkEvent);
    
    
    String convName = currConv.getName();
    List<Event> events = currConv.getEvents();
    String existingEvents = "";

    for (Event event : events) {
      existingEvents+="<p>" + event.getName() + "</p>";
    }
    
    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", 
        "convName", convName, "existingEvents", existingEvents);
    
    return new ModelAndView(variables, "convention_home.ftl");
  }
}
