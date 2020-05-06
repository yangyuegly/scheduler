package edu.brown.cs.student.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
//do we intend to save this in the database??
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.webscraper.WebScraper;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to schedule final exams and take the user to the page that displays the
 * schedule.
 */
public class SchedExamSubmitHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    DatabaseUtility db = new DatabaseUtility();
    String userEmail = request.cookie("user");
    String id = request.params(":id");

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }

    QueryParamsMap queryMap = request.queryMap();
    String schoolName = queryMap.value("schoolName");
    String startDate = queryMap.value("startDate");
    String numDaysString = queryMap.value("numDays");
    String eventDuration = queryMap.value("eventDuration");
    String startTime = queryMap.value("startTime");
    String endTime = queryMap.value("endTime");
    String submitType = queryMap.value("submitType");

    int numDays;
    int eventDur;
    Convention newConv;

    try {
      numDays = Integer.parseInt(numDaysString);
      eventDur = Integer.parseInt(eventDuration);
      newConv = new Convention(id, schoolName + " Final Exams", startDate, numDays, eventDur,
          startTime, endTime);
      // add this convention to the database

      db.addConventionData(newConv);

    } catch (NumberFormatException err) {
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "id", id.toString(),
          "errorMessage", "The number of days and the date/time fields must be integers.");
      return new ModelAndView(variables, "setup_conv.ftl");
    }

    // Use the WebScraper to add the events and conflicts to the convention
    WebScraper scraper = new WebScraper(id);
    Map<String, String> schoolNameToIDMap = scraper.getcoursesToIDs();
    String schoolID = schoolNameToIDMap.get(schoolName);

    if (schoolID == null) {
      // the user selected a name that is not available to scrape
      Calendar cal = Calendar.getInstance();
      int month = cal.get(Calendar.MONTH) + 1;
      int day = cal.get(Calendar.DAY_OF_MONTH);
      int year = cal.get(Calendar.YEAR);

      String date = year + "-" + month + "-" + day;

      List<String> schoolNamesList = new ArrayList<>();
      String schoolSuggestions = "";

      for (String currSchoolName : schoolNameToIDMap.keySet()) {
        schoolNamesList.add(currSchoolName);

      }

      Collections.sort(schoolNamesList);

      for (String currSchoolName : schoolNamesList) {
        schoolSuggestions = schoolSuggestions + "<option value=\"" + currSchoolName + "\" />"
            + schoolName + "</option>";
      }

      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "schoolSuggestions",
          schoolSuggestions, "currDay", date, "id", id.toString(), "errorMessage",
          "Please select a school from the list.");
      return new ModelAndView(variables, "create_exam_conv.ftl");
    }

    WebScraper.setCollege(schoolID);
    db.addConvIDCollaborator(userEmail, id);

    // schedule this exam
    response.redirect("/schedule/" + id);
    return null;
  }

}
