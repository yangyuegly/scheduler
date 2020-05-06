package edu.brown.cs.student.gui;

import java.time.LocalDate;
//integrated
import java.util.ArrayList;
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
 * This class is used to display the create_exam_conv page, so the user can schedule final exams for
 * the college of their choice. It implements TemplateViewRoute.
 */
public class CreateExamConvHandler implements TemplateViewRoute {

  /**
   * These are fields for this class.
   *
   * ID_UPPER_BOUND - an int, which is used as an upper bound when generating random convention IDs
   *
   * ID_LOWER_BOUND - an int, which is used as a lower bound when generating random convention IDs
   */
  private static final int ID_UPPER_BOUND = (999999 - 100000) + 1;
  private static final int ID_LOWER_BOUND = 100000;

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }

    // gets the current date (we don't want the user to schedule an event in the past)
    LocalDate today = LocalDate.now();

    // create a convention id - we want a six digit id that has not been used
    Random rand = new Random();
    boolean avail = false;
    Integer id = null;

    while (!avail) {
      id = rand.nextInt(ID_UPPER_BOUND) + ID_LOWER_BOUND;
      DatabaseUtility db = new DatabaseUtility();

      // if avail is true, this ID is not yet used
      avail = db.addConvID(userEmail, id.toString());
    }

    // get the names of the schools on Coursicle so they appear as options for the user to select
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
        schoolSuggestions, "currDay", today, "id", id.toString(), "errorMessage", "");

    return new ModelAndView(variables, "create_exam_conv.ftl");
  }

}
