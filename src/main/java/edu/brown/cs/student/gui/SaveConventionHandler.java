package edu.brown.cs.student.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.Event;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

//not integrated
/**
 * This class is used to save a convention and take the user to their account home page. It
 * implements Route.
 */
public class SaveConventionHandler implements Route {

  @Override
  public String handle(Request req, Response response) throws Exception {
    String userEmail = req.cookie("user");
    String conventionID = req.params(":id");

    DatabaseUtility db = new DatabaseUtility();
    boolean permission = db.checkPermission(userEmail, conventionID);

    if (!permission) {
      System.out.println("permission denied");
      response.redirect("/account");
    }

    if (userEmail == null) {
      // user is not logged in
      // Map<String, Object> variables = ImmutableMap.of("title", -----------------

      // "Scheduler", "message", "Please log in");
      // return new ModelAndView(variables, "home.ftl");
    }

    QueryParamsMap queryMap = req.queryMap();
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

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "conventionLinks",
        "test@!!!!!!!!"); // ????????????????????????????????

    Gson gson = new Gson();
    return gson.toJson(variables);
  }

}
