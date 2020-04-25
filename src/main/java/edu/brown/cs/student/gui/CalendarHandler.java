package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.student.scheduler.Convention;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class is used to get the events and their times in a format that is compatible with the
 * FullCalendar API's javascript. It implements TemplateViewRoute.
 */
public class CalendarHandler implements Route {

  @Override
  public String handle(Request req, Response res) {

    // get events in this convention from the database, display their names and
    // give the user options to schedule, etc

    String conventionID = req.params(":id");

//    String userEmail = req.cookie("user"); // what do we do with this?????????????????????????????????
//
//    if (userEmail == null) {
//      // user is not logged in
//      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
//          "Please log in");
//      return new ModelAndView(variables, "home.ftl");
//    }

//    boolean authorized = DatabaseUtility.checkPermission(userEmail, conventionID);
//    if (!authorized) {
//      Map<String, Object> variables = ImmutableMap.of("title",
//          "Scheduler");
//      return new ModelAndView(variables, "unauthorized.ftl");
//    }

    Convention myConv = new Convention(conventionID); // DatabaseUtility.getConvention(conventionID);
                                                      // // because we need all the fields
                                                      // !!!!!!!!!!!!!!
    String name = myConv.getName();
//    int numTimeSlotsPerDay = myConv.getNumTimeSlotsPerDay(); // uncomment

    // uncomment
//    ScheduleCommand schedComm = new ScheduleCommand(myConv, 100, myConv.getNumDays(), numTimeSlotsPerDay); // change
    // concurrency
    // limit
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//    String scheduleString = schedComm.execute(); // uncomment

    // delete, but use this format in schedComm execute
    String scheduleString = "[{\"title\": \"Long Event\", \"start\": \"2020-04-12T10:30:00\", \"end\": \"2020-04-12T12:30:00\"}, "
        + "{\"title\": \"Long Event\", \"start\": \"2020-04-12T10:30:00\", \"end\": \"2020-04-12T12:30:00\"}]";

    String defaultDate = "2020-04-12"; // this should be the start date

    Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", scheduleString,
        "defaultDate", defaultDate);
    Gson gson = new Gson();

    return gson.toJson(variables);
  }

}
