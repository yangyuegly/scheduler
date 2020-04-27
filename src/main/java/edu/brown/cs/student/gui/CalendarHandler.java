package edu.brown.cs.student.gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.student.scheduler.CalendarEvent;
import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.ScheduleCommand;
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
    String conventionID = req.params(":id");

//    String userEmail = req.cookie("user"); // what do we do with this - this handler gives information, it doesn't display a page?????????????????????????????????
//
//    if (userEmail == null) {
//      // user is not logged in
//      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
//          "Please log in");
//      return new ModelAndView(variables, "home.ftl");
//    }

    DatabaseUtility db = new DatabaseUtility();
//    boolean authorized = db.checkPermission(userEmail, conventionID); // what do we do with this?????
//
//    if (!authorized) {
//      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler");
//      return new ModelAndView(variables, "unauthorized.ftl");
//    }

    Convention myConv = db.getConvention(conventionID);
    int numTimeSlotsPerDay = myConv.getNumTimeSlotsPerDay();

    ScheduleCommand schedComm = new ScheduleCommand(myConv, 100, myConv.getNumDays(),
        numTimeSlotsPerDay); // change concurrency
                             // limit!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    // String scheduleString = schedComm.execute();
    List<CalendarEvent> eventsSched = schedComm.execute();

//    System.out.println("schedString: " + scheduleString); // delete
//
//    // delete
//    for (int i = 0; i < 175; i++) {
//      System.out.print(scheduleString.charAt(i)); // delete
//    }
//
    LocalDateTime convStartWithTime = myConv.getStartDateTime();
    LocalDate convStartDay = convStartWithTime.toLocalDate();

    Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", eventsSched, "defaultDate",
        convStartDay.toString());
    Gson gson = new Gson();

    return gson.toJson(variables);
  }

}
