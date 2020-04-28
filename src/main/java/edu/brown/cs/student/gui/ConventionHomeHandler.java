package edu.brown.cs.student.gui;

import java.time.LocalDate;
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

    Convention currConv = new Convention(conventionID);

    if (!currConv.isLoaded()) {
      // this convention was never set up with the name and time information

      // gets the current date (we don't want the user to schedule an event in the past)
      LocalDate today = LocalDate.now();

      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "currDay", today, "id",
          conventionID, "errorMessage", "");

      return new ModelAndView(variables, "setup_conv.ftl");
    }

    String convName = currConv.getName();
    List<Event> events = currConv.getEvents();
    String existingEvents = "";

    // this creates a string that tells the user what events are already in this convention
    for (Event event : events) {
      existingEvents += "<p>" + event.getName() + "</p>";
    }

    if (existingEvents.equals("")) {
      existingEvents = "No events yet.";
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "id", conventionID,
        "convName", convName, "existingEvents", existingEvents);

    return new ModelAndView(variables, "convention_home.ftl");
  }
}
