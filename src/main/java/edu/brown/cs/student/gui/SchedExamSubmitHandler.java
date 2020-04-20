package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.webscraper.WebScraper;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to schedule final exams and take the user to the page that
 *   displays the schedule.
 */
public class SchedExamSubmitHandler implements TemplateViewRoute {
  
  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");
    String id = request.params(":id");
    
    if (userEmail == null) {
      // user is not logged in
      String currUserMessage = "<a href=/home>Log in</a>";
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "currUserMessage", currUserMessage, "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    
    String currUserMessage = "<label>Logged in as <a href=/account>" + userEmail
        + "</a></label>" + "<br><a href=/logout>Log out</a>";
    
    QueryParamsMap queryMap = request.queryMap();
    String schoolName = queryMap.value("schoolName");
    String startDate = queryMap.value("startDate");
    String numDaysString = queryMap.value("numDays");
    String eventDuration = queryMap.value("eventDuration");
    String startTime = queryMap.value("startTime");
    String endTime = queryMap.value("endTime");
    String submitType = queryMap.value("submitType");
    
    int numDays = Integer.parseInt(numDaysString);
    
    int idInt = Integer.parseInt(id);
   
     WebScraper scraper = new WebScraper(idInt);// takes in convention id!!!
    // need to call setSchool() - do we do this with the ID or the name?  If it's the id, how do we get it?
    
    // schedule it
    
    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "currUserMessage",
        currUserMessage, "name", schoolName + " Final Exams"); // fix!!!!!!!!!!!!!!
      return new ModelAndView(variables, "calendar_page.ftl");

  }

}
