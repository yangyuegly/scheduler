package edu.brown.cs.student.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.Event;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display the page where attendees can sign up for a convention and select
 * the events they wish to attend. It implements TemplateViewRoute.
 */
public class AttendeeSignupHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String conventionID = request.params(":id");
    Convention conv = new Convention(conventionID);

    if (!conv.isLoaded()) {
      // the convention manager(s) did not finish setting up the convention
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler");

      return new ModelAndView(variables, "attendee_signup_no_conv.ftl");
    }

    String convName = conv.getName();

    // any attendee can only attend as many events as there are time slots
    int maxEventsToSelect = conv.getNumTimeSlotsPerDay() * conv.getNumDays();

    List<Event> eventsInConv = conv.getEvents();
    String eventCheckboxes = "";

    // create a checkbox for each event to display on the page
    for (Event event : eventsInConv) {
      String eventName = event.getName();
      int eventID = event.getID();
      String description = event.getDescription();
      eventCheckboxes += "<input type=\"checkbox\" name=\"" + eventID + "\" id=\"" + eventID + "\""
          + "value=\"" + eventName + "\"><label for=\"" + eventID + "\"> " + eventName;

      if ((description != null) && (description != "")) {
        eventCheckboxes += ": " + description;
      }

      eventCheckboxes += "</label><br>";
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "conventionName",
        convName, "id", conventionID, "maxEventsToSelect", maxEventsToSelect, "eventCheckboxes",
        eventCheckboxes);

    return new ModelAndView(variables, "attendee_signup.ftl");
  }

}
