package edu.brown.cs.student.gui;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class CreateConvSubmitHandler implements TemplateViewRoute {
  @Override
  public ModelAndView handle(Request req, Response res) {
    QueryParamsMap queryMap = req.queryMap();
    String name = queryMap.value("convName");
    String startDate = queryMap.value("startDate");
    String numDaysString = queryMap.value("numDays");
    String eventDuration = queryMap.value("eventDuration");
    String startTime = queryMap.value("startTime");
    String endTime = queryMap.value("endTime");
    String submitType = queryMap.value("submitType");
    
    int numDays;
    
    Random rand = new Random();
    boolean avail = false;
    // we want a six digit id that has not been used
    Integer id = rand.nextInt((999999-100000) + 1) + 100000;
//    while (!avail) {
//     // avail = Main.getDatabase().checkID();
//      id = rand.nextInt((999999-100000) + 1) + 100000;
//    }
    //add to database
    
    
    
    try {
      numDays = Integer.parseInt(numDaysString);
    } catch (NumberFormatException err) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "errorMessage", "The number of days must be an integer.");
      return new ModelAndView(variables, "create_conv.ftl");
    }
    if (submitType.equals("Add events by hand")) {
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", 
          name, "id", id.toString());
      return new ModelAndView(variables, "add_event.ftl");
    } else {
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", 
          name, "message", "");
      return new ModelAndView(variables, "upload_conv.ftl");
    }
  }
  
  
  
}
  