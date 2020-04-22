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
 * This class is used to display the page where attendees can sign up for a convention
 *   and select the events they wish to attend.  It implements TemplateViewRoute.
 */
public class AttendeeSignupHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String conventionID = request.params(":id");
    Convention conv = new Convention(conventionID);
    
//    conv.setName("Book Signing"); // delete!!!!!!!!!!11!!!!1111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    Event newEvent = new Event(1, "J.K. Rowling", "have her sign books!"); // delete!!!!!!!!!!11!!!!1111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    conv.addEvent(newEvent); // delete!!!!!!!!!!11!!!!1111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    Event newEvent2 = new Event(2, "cheese", "have her sign books!"); // delete!!!!!!!!!!11!!!!1111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    conv.addEvent(newEvent2); // delete

    String convName = conv.getName();

    // any attendee can only attend as many events as there are time slots
    int maxEventsToSelect = conv.getNumTimeSlots();
    
    List<Event> eventsInConv = conv.getEvents(); // make sure getEvents actually accesses the database!!!!!!!!!
    String eventCheckboxes = "";
    
    for (Event event : eventsInConv) {
      String eventName = event.getName();
      int eventID = event.getID();
      String description = event.getDescription();
      eventCheckboxes += "<input type=\"checkbox\" name=\"" + eventID + "\" id=\"" + eventID + "\""
          + "value=\"" + eventName + "\"><label for=\"" + eventID + "\"> " + eventName + ": "
          + description + "</label><br>";
    }
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionName", convName, "id", conventionID, 
        "maxEventsToSelect", maxEventsToSelect, "eventCheckboxes", eventCheckboxes);
    
    return new ModelAndView(variables, "attendee_signup.ftl");    
  }

}
