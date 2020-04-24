package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.graph.UndirectedWeightedGraph;
import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.ScheduleCommand;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class CalendarHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {

    // get events in this convention from the database, display their names and 
    // give the user options to schedule, etc
    
    String conventionID = req.params(":id");
    String userEmail = req.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    

//    boolean authorized = DatabaseUtility.checkPermission(userEmail, conventionID);
//    if (!authorized) {
//      Map<String, Object> variables = ImmutableMap.of("title",
//          "Scheduler");
//      return new ModelAndView(variables, "unauthorized.ftl");
//    }
   
    Convention myConv = new Convention(conventionID); //DatabaseUtility.getConvention(conventionID); // because we need all the fields
    String name = myConv.getName();
    int numTimeSlots = myConv.getNumTimeSlots();
    
    //want to calculate the time slot
    ScheduleCommand schedComm = new ScheduleCommand(myConv, 100, myConv.getNumDays(), 
        numTimeSlots); // change concurrency limit !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
    
    
    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "name", name);
    return new ModelAndView(variables, "calendar_page.ftl");
  }

    
}
