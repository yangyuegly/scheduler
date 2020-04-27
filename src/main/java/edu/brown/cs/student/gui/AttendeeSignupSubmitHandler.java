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

    Convention conv = new Convention(conventionID);

    // check if
    // loaded?????????????????????????????????????????????????????????????????????????????????????????

    if (!conv.isLoaded()) {
      // the convention
    }

    List<Event> eventsInConv = conv.getEvents();
    List<Event> eventsToAttend = new ArrayList<>();

    for (Event event : eventsInConv) {
      if (queryMap.value(event.getID() + "") != null) {
        // the attendee wants to go to this event
        eventsToAttend.add(event);
      }
    }

    int numEventsToAttend = eventsToAttend.size();
    DatabaseUtility database = new DatabaseUtility();

    // add all the pairs of events to the list of conflicts
    for (int i = 0; i < numEventsToAttend; i++) {
      Event currEvent = eventsToAttend.get(i);

      for (int j = i + 1; j < numEventsToAttend; j++) {
        Conflict newConflict = new Conflict(currEvent, eventsToAttend.get(j), 1);
        database.addConflict(conventionID, newConflict);
      }
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "conventionName",
        conv.getName());
    return new ModelAndView(variables, "attendee_successfully_signed_up.ftl");
  }
}
