package edu.brown.cs.student.gui;

//integrated
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.webscraper.WebScraper;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display the create_exam_conv page, so the user can
 * schedule final exams for the college of their choice.
 */
public class CreateExamConvHandler implements TemplateViewRoute {
  DatabaseUtility db = new DatabaseUtility();

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }

    // gets the current date (user can't schedule an event in the past)
    Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int year = cal.get(Calendar.YEAR);

    String date = year + "-" + month + "-" + day;

    // create a convention id
    Random rand = new Random();
    boolean avail = false;
    // we want a six digit id that has not been used
    Integer id = null;
    while (!avail) {
      id = rand.nextInt((999999 - 100000) + 1) + 100000;
//       avail = true; //delete
      avail = db.addConvID(userEmail, id.toString()); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    }

    // get the names of the schools on Coursicle so they appear as suggestions
    String schoolSuggestions = "";
    WebScraper scraper = new WebScraper(id.toString());
    Map<String, String> schoolNameToIDMap = scraper.getcoursesToIDs();
    List<String> schoolNamesList = new ArrayList<>();

    for (String schoolName : schoolNameToIDMap.keySet()) {
      schoolNamesList.add(schoolName);
    }

    Collections.sort(schoolNamesList);

    for (String schoolName : schoolNamesList) {
      schoolSuggestions = schoolSuggestions + "<option value=\"" + schoolName + "\" />" + schoolName
          + "</option>";
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "schoolSuggestions",
        schoolSuggestions, "currDay", date, "id", id.toString(), "errorMessage", "");

    return new ModelAndView(variables, "create_exam_conv.ftl");
  }

}
