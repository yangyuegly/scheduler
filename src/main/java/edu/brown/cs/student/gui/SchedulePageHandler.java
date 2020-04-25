package edu.brown.cs.student.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.Event;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

public class SchedulePageHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");
    String conventionID = request.params(":id");

    DatabaseUtility db = new DatabaseUtility();
    boolean permission = db.checkPermission(userEmail, conventionID);

    if (!permission) {
      System.out.println("permission denied");
      response.redirect("/account");
    }

    if (userEmail == null) {
      // user is not logged in
      // Map<String, Object> variables = ImmutableMap.of("title", -----------------
      // FIX!!!!!!!!!!!!!!!!!
      // "Scheduler", "message", "Please log in");
      // return new ModelAndView(variables, "home.ftl");
    }

    QueryParamsMap queryMap = request.queryMap();
    String EventsToAddString = queryMap.value("existingEvents"); // deal with
                                                                 // this!!!!!!!!!!!!!!!!!!!!!!!!
    System.out.println("events to add" + EventsToAddString);

    List<Event> events = new ArrayList<>(); // get this from request though

    for (Event currEvent : events) {
      if (!db.addEvent(conventionID, currEvent)) {
        // the convention ID is not in the database
        // ?? IDK what to
        // do!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1!!!!
      }
    }

    // adding a collaborator
    String collaboratorEmail = queryMap.value("colEmail");
    db.addConvID(collaboratorEmail, conventionID);
//    }

    Convention myConv = new Convention(conventionID); // DatabaseUtility.getConvention(conventionID);
                                                      // // because we need all the fields
                                                      // !!!!!!!!!!!!!!
    String name = myConv.getName();

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "name", name);
    return new ModelAndView(variables, "calendar_page.ftl");
  }

}
