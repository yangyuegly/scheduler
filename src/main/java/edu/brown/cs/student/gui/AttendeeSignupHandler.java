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
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Sorry, this convention was never set up.");

      return new ModelAndView(variables, "attendee_signup_error.ftl");
    }

    String convName = conv.getName();
    List<Event> eventsInConv = conv.getEvents();
    String eventCheckboxes = "";

    // any attendee can only attend as many events as there are time slots or the total number
    // of events, if there are more time slots than events
    int numTimeSlots = conv.getNumTimeSlotsPerDay() * conv.getNumDays();
    int maxEventsToSelect = Math.min(numTimeSlots, eventsInConv.size());

    // create a checkbox for each event to display on the page
    for (Event event : eventsInConv) {
      String eventName = event.getName();
      int eventID = event.getID();
      String description = event.getDescription();
      eventCheckboxes += "<input type=\"checkbox\" name=\"" + eventID + "\" id=\"" + eventID + "\""
          + "value=\"" + eventName + "\"><label for=\"" + eventID + "\"> " + eventName;

      if ((description != null) && (!description.equals(""))) {
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
