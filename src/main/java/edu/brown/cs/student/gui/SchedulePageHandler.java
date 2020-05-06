package edu.brown.cs.student.gui;

import java.time.LocalDate;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This is a class used to display the schedule page without the calendar. It implements
 * TemplateViewRoute.
 */
public class SchedulePageHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");
    String conventionID = request.params(":id");

    DatabaseUtility db = new DatabaseUtility();
    boolean permission = db.checkPermission(userEmail, conventionID);

    if (!permission) {
      response.redirect("/unauthorized");
    }

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }

    Convention myConv = new Convention(conventionID);

    if (!myConv.isLoaded()) {
      LocalDate today = LocalDate.now();
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "currDay", today, "id",
          conventionID, "errorMessage",
          "You must set up the convention before you can schedule it.");

      return new ModelAndView(variables, "setup_conv.ftl");
    }

    String name = myConv.getName();

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "name", name);
    return new ModelAndView(variables, "calendar_page.ftl");
  }

}
