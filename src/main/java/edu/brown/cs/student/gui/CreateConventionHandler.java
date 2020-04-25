package edu.brown.cs.student.gui;

import java.time.LocalDate;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.DatabaseUtility;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * Class that is a handler for the creating a convention page.
 *
 */
public class CreateConventionHandler implements TemplateViewRoute {

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

    // we want to try to make IDs until we find one that is not already in use
    while (!avail) {
      id = rand.nextInt((999999 - 100000) + 1) + 100000;
      DatabaseUtility db = new DatabaseUtility();

      // if avail is true, this ID is not yet used
      avail = db.addConvID(userEmail, id.toString());
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "currDay", today, "id",
        id.toString(), "errorMessage", "");

    return new ModelAndView(variables, "setup_conv.ftl");
  }

}
