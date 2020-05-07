package edu.brown.cs.student.gui;

import java.time.LocalDate;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to handle POST requests that submit the form data related to setting up a
 * convention. It implements TemplateViewRoute.
 *
 */
public class CreateConvSubmitHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");
    String id = request.params(":id");

    if (userEmail == null) {
      // user is not logged in
      response.redirect("/not_logged_in");
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean authorized = db.checkPermission(userEmail, id);

    if (!authorized) {
      response.redirect("/unauthorized");
    }

    QueryParamsMap queryMap = request.queryMap();
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

    boolean added = db.addConventionData(newConv);

    if (!added) {
      // an error occurred
      LocalDate today = LocalDate.now();
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "currDay", today, "id",
          id.toString(), "errorMessage",
          "An error occurred while saving your convention. Please try again.");

      return new ModelAndView(variables, "setup_conv.ftl");
    }

    if (submitType.equals("Add events by hand")) {
      // go to the convention home page
      response.redirect("/convention/" + id.toString());
      return null; // it will never reach this line

    } else {
      // the user clicked "Upload a file"
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", name, "id",
          id.toString(), "message", "");
      return new ModelAndView(variables, "upload_conv.ftl");
    }
  }
}
