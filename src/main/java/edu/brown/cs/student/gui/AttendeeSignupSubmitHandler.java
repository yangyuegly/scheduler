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
    String attendeeName = queryMap.value("attendeeName");

    Convention conv = new Convention(conventionID);

//    conv.setName("Book Signing"); // delete!!!!!!!!!!11!!!!1111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    Event newEvent = new Event(1, "J.K. Rowling", "have her sign books!"); // delete!!!!!!!!!!11!!!!1111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    conv.addEvent(newEvent); // delete!!!!!!!!!!11!!!!1111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    Event newEvent2 = new Event(2, "cheese", "have her sign books!"); // delete!!!!!!!!!!11!!!!1111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    conv.addEvent(newEvent2); // delete

    List<Event> eventsInConv = conv.getEvents();
    List<Event> eventsToAttend = new ArrayList<>();

    for (Event event : eventsInConv) {
      if (queryMap.value(event.getID() + "") != null) {
        // the attendee wants to go to this event
        eventsToAttend.add(event);
      }
    }

    List<Conflict> conflictsFromAttendee = new ArrayList<>();
    int numEventsToAttend = eventsToAttend.size();

    // add all the pairs of events to the list of conflicts
    for (int i = 0; i < numEventsToAttend; i++) {
      Event currEvent = eventsToAttend.get(i);

      for (int j = i + 1; j < numEventsToAttend; j++) {
        Conflict newConflict = new Conflict(currEvent, eventsToAttend.get(j), 1);
        conflictsFromAttendee.add(newConflict);
      }
    }

    DatabaseUtility database = new DatabaseUtility();
//    database.updateConflicts(conventionID, conflictsFromAttendee);

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "conventionName",
        conv.getName());
    return new ModelAndView(variables, "attendee_successfully_signed_up.ftl");
  }
}
