package edu.brown.cs.student.gui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.webscraper.WebScraper;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display the create_exam_conv page, so the user can schedule
 *   final exams for the college of their choice.
 */
public class CreateExamConvHandler implements TemplateViewRoute {
  
  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");
    
    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    
    //gets the current date (user can't schedule an event in the past)
    Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int year = cal.get(Calendar.YEAR);
    
    String date = year + "-" + month + "-" + day;
    
    // get the names of the schools on Coursicle so they appear as suggestions
    String schoolSuggestions = "";
    WebScraper scraper = new WebScraper();
    Map<String, String> idToSchoolMap = scraper.getcoursesToIDs();
    List<String> schoolNamesList = new ArrayList<>();
    
    for (String schoolName : idToSchoolMap.values()) {
      schoolNamesList.add(schoolName);
    }
    
    Collections.sort(schoolNamesList);
    
    for (String schoolName : schoolNamesList) {
      schoolSuggestions = schoolSuggestions + "<option value=\"" + schoolName + "\" />" 
        + schoolName + "</option>";
    }
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "schoolSuggestions", schoolSuggestions, "currDay", date);
    
    return new ModelAndView(variables, "create_exam_conv.ftl");    
  }

}
