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
import edu.brown.cs.student.webscraper.WebScraper;
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
    WebScraper scraper = new WebScraper(conventionID);
    String correspondingID = scraper.scrape();
    System.out.println(WebScraper.collegeName);
    if (correspondingID == null || correspondingID.isEmpty()) {
      System.out.println("this is not an exam");
      correspondingID = null;
    }
    String userEmail = req.cookie("user");

    if (userEmail == null) {
      res.redirect("/home");
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean authorized = db.checkPermission(userEmail, conventionID);

    if (!authorized) {
      res.redirect("/unauthorized");
    }

    Convention myConv = db.getConvention(conventionID);
    int numTimeSlotsPerDay = myConv.getNumTimeSlotsPerDay();

    ScheduleCommand schedComm = new ScheduleCommand(myConv, 100, myConv.getNumDays(),
        numTimeSlotsPerDay, correspondingID);
    List<CalendarEvent> schedule;

    try {
      schedule = schedComm.execute();
    } catch (NullPointerException err) {
      // there was an error with the scheduling
      Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", "", "defaultDate", "",
          "error", "We're sorry, we couldn't make a schedule for you. There was no way to avoid"
              + " conflicts between your events.");
      Gson gson = new Gson();

      return gson.toJson(variables);
    }

    LocalDateTime convStartWithTime = myConv.getStartDateTime();
    LocalDate convStartDay = convStartWithTime.toLocalDate();

    Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", schedule, "defaultDate",
        convStartDay.toString(), "error", "");
    Gson gson = new Gson();

    return gson.toJson(variables);
  }

}
