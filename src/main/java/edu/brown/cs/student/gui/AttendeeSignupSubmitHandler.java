package edu.brown.cs.student.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Conflict;
import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.Event;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to handle when an attendee signs up for a convention.
 */
public class AttendeeSignupSubmitHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    QueryParamsMap queryMap = request.queryMap();
    String conventionID = request.params(":id");
    String attendeeEmail = queryMap.value("attendeeEmail");
    DatabaseUtility database = new DatabaseUtility();

    if (!attendeeEmail.contentEquals("")) {
      // the attendee wants to be notified with the final schedule
      boolean addEmailSuccess = database.addAttendeeEmail(conventionID, attendeeEmail);

      if (!addEmailSuccess) {
        // the attendee's email was unable to be added to the database
        Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
            "Sorry, an error occurred, and we were unable to sign you up.");

        return new ModelAndView(variables, "attendee_signup_error.ftl");
      }
    }

    Convention conv = new Convention(conventionID);
    List<Event> eventsInConv = conv.getEvents();
    List<Event> eventsToAttend = new ArrayList<>();

    for (Event event : eventsInConv) {
      if (queryMap.value(event.getID() + "") != null) {
        // the attendee wants to go to this event
        eventsToAttend.add(event);

        System.out.println("attendee wants to attend " + event.getName()); // delete;
      }
    }

    int numEventsToAttend = eventsToAttend.size();

    // add all the pairs of events to the list of conflicts
    for (int i = 0; i < numEventsToAttend; i++) {
      Event currEvent = eventsToAttend.get(i);

      for (int j = i + 1; j < numEventsToAttend; j++) {
        Conflict newConflict = new Conflict(currEvent, eventsToAttend.get(j), 1);

        System.out.println("adding conflict between " + currEvent.getName() + " and "
            + eventsToAttend.get(j).getName()); // delete;

        if (!database.addConflict(conventionID, newConflict)) {
          System.err.println("error adding the conflict between " + currEvent.getName() + " and "
              + eventsToAttend.get(j).getName());
        }
      }
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "conventionName",
        conv.getName());
    return new ModelAndView(variables, "attendee_successfully_signed_up.ftl");
  }
}
