package edu.brown.cs.student.gui;
//integrated

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.Event;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to handle requests to the home page of a convention.
 */
public class ConventionHomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String conventionID = request.params(":id");
    String userEmail = request.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean authorized = db.checkPermission(userEmail, conventionID);

    if (!authorized) {
      response.redirect("/unauthorized");
    }

    // get convention object with all the events from database based on id
    Convention currConv = new Convention(conventionID);

    // check if
    // loaded!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1

    String convName = currConv.getName();
    List<Event> events = currConv.getEvents();
    String existingEvents = "";

    for (Event event : events) {
      existingEvents += "<p>" + event.getName() + "</p>";
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "id", conventionID,
        "convName", convName, "existingEvents", existingEvents);

    return new ModelAndView(variables, "convention_home.ftl");
  }
}
