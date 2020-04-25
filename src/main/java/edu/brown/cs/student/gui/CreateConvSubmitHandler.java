package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

//integrated
public class CreateConvSubmitHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      response.redirect("/home");
      return null; // this line will not be reached
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
    Convention newConv;

    try {
      String numDaysString = queryMap.value("numDays");
      numDays = Integer.parseInt(numDaysString);
      String eventDuration = queryMap.value("eventDuration");
      eventDur = Integer.parseInt(eventDuration);
      newConv = new Convention(id, name, startDate, numDays, eventDur, startTime, endTime);

    } catch (NumberFormatException err) {
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "id", id.toString(),
          "errorMessage", "The number of days and the date/time fields must be integers.");
      return new ModelAndView(variables, "setup_conv.ftl");
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean added = db.addConventionData(newConv);

    if (!added) {
      System.err.println("Convention data adding failed"); // fix
                                                           // this!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    if (submitType.equals("Add events by hand")) {
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", name,
          "existingEvents", "No events yet.", "id", id);
      return new ModelAndView(variables, "convention_home.ftl");

    } else {
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", name, "id",
          id.toString(), "message", "");
      return new ModelAndView(variables, "upload_conv.ftl");
    }
  }
}
