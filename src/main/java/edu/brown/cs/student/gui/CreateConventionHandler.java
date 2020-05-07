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
 * Class that is a handler for the creating a convention page. It implements TemplateViewRoute.
 *
 */
public class CreateConventionHandler implements TemplateViewRoute {

  /**
   * These are fields for this class.
   *
   * ID_UPPER_BOUND - an int, which is used as an upper bound when generating random convention IDs
   *
   * ID_LOWER_BOUND - an int, which is used as a lower bound when generating random convention IDs
   */
  private static final int ID_UPPER_BOUND = (999999 - 100000) + 1;
  private static final int ID_LOWER_BOUND = 100000;

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      response.redirect("/not_logged_in");
    }

    // gets the current date (we don't want the user to schedule an event in the past)
    LocalDate today = LocalDate.now();

    // create a convention id - we want a six digit id that has not been used
    Random rand = new Random();
    boolean avail = false;
    Integer id = null;

    while (!avail) {
      id = rand.nextInt(ID_UPPER_BOUND) + ID_LOWER_BOUND;
      DatabaseUtility db = new DatabaseUtility();

      // if avail is true, this ID has not yet been used
      avail = db.addConvID(userEmail, id.toString());
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "currDay", today, "id",
        id.toString(), "errorMessage", "");
    return new ModelAndView(variables, "setup_conv.ftl");
  }

}
