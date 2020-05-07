package edu.brown.cs.student.gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.student.exception.SchedulingException;
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
    Gson gson = new Gson();
    String conventionID = req.params(":id");
    String userEmail = req.cookie("user");

    if (userEmail == null) {
      Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", "", "defaultDate", "",
          "error", "You must be logged in to view a schedule.");
      return gson.toJson(variables);
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean authorized = db.checkPermission(userEmail, conventionID);

    if (!authorized) {
      Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", "", "defaultDate", "",
          "error", "You do not have permission to view this schedule.");
      return gson.toJson(variables);
    }

    // see if this convention is an exam schedule
    Convention myConv = db.getConvention(conventionID);
    WebScraper scraper = new WebScraper(conventionID);
    Map<String, String> schoolNameToIDMap = scraper.getcoursesToIDs();
    String convName = myConv.getName();
    String[] schoolNameArray = convName.split(" ");
    String schoolName = "";

    // remove "Final Exams" from the name of the convention, giving us the name of the school
    for (int i = 0; i < schoolNameArray.length - 2; i++) {
      schoolName += schoolNameArray[i] + " ";
    }

    String schoolID = schoolNameToIDMap.get(schoolName.trim());
    WebScraper.setCollege(schoolID);

    // this ID will be null if this convention is not an exam, otherwise it will be the ID of the
    // school whose exams are in this convention
    String correspondingID = scraper.scrape();

    int numTimeSlotsPerDay = myConv.getNumTimeSlotsPerDay();
    ScheduleCommand schedComm = new ScheduleCommand(myConv, 5, myConv.getNumDays(),
        numTimeSlotsPerDay, correspondingID);
    List<CalendarEvent> schedule;

    try {
      schedule = schedComm.execute();

    } catch (SchedulingException err) {
      // there was an error with the scheduling
      Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", "", "defaultDate", "",
          "error", "We're sorry, we couldn't make a schedule for you. There was no way to avoid"
              + " conflicts between your events.");
      return gson.toJson(variables);

    } catch (NullPointerException err) {
      System.err.println("Null pointer occurred while scheduling");

      // there was an error with the scheduling
      Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", "", "defaultDate", "",
          "error", "We're sorry, we couldn't make a schedule for you. There was no way to avoid"
              + " conflicts between your events.");
      return gson.toJson(variables);
    }

    LocalDateTime convStartWithTime = myConv.getStartDateTime();
    LocalDate convStartDay = convStartWithTime.toLocalDate();

    Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", schedule, "defaultDate",
        convStartDay.toString(), "error", "");
    return gson.toJson(variables);
  }

}
