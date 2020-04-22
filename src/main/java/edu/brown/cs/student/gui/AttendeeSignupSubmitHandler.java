package edu.brown.cs.student.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import edu.brown.cs.student.scheduler.Event;

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
    
    // UtilityDatabase.addConflicts(conv, eventsToAttend);
    

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler",
        "conventionName", conv.getName());
    return new ModelAndView(variables, "attendee_successfully_signed_up.ftl");
  }

}
