package edu.brown.cs.student.gui;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class CreateConvSubmitHandler implements TemplateViewRoute {
  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");
    
    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    
    QueryParamsMap queryMap = request.queryMap();
    String id = request.params(":id");
    String name = queryMap.value("convName");
    String startDate = queryMap.value("startDate");
   
    String startTime = queryMap.value("startTime");
    String endTime = queryMap.value("endTime");
    String submitType = queryMap.value("submitType");
    


    int numDays;
    int eventDur;
    try {
      String numDaysString = queryMap.value("numDays");
      numDays = Integer.parseInt(numDaysString);
      String eventDuration = queryMap.value("eventDuration");
      eventDur = Integer.parseInt(eventDuration);
    } catch (NumberFormatException err) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "id", id.toString(), "errorMessage",
          "The number of days must be an integer.");
      return new ModelAndView(variables, "setup_conv.ftl");
    }
    Convention newConv = new Convention(id, startDate, numDays, eventDur,
        startTime, endTime);
    
    // need to add Convention parameters to Database
   //boolean added = Database.addConvData(newConv); 
    
    if (submitType.equals("Add events by hand")) {
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", 
          name, "existingEvents", "", "id", id);
      return new ModelAndView(variables, "convention_home.ftl");
    } else {
      
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", 
          name, "id", id.toString(), "message", "");
      return new ModelAndView(variables, "upload_conv.ftl");
    }
  }
}
  