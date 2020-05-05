package edu.brown.cs.student.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.Event;
import edu.brown.cs.student.webscraper.WebScraper;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class is used to add an event to the database. It implements Route.
 */
public class AddEventHandler implements Route {

  @Override
  public String handle(Request req, Response response) throws Exception {
    String userEmail = req.cookie("user");

    QueryParamsMap queryMap = req.queryMap();
    String eventString = queryMap.value("event");
    String conventionID = queryMap.value("conventionID");
    Convention conv = new Convention(conventionID);

    DatabaseUtility db = new DatabaseUtility();
    boolean permission = db.checkPermission(userEmail, conventionID);

    if (!permission) {
      Map<String, Object> variables = ImmutableMap.of("errorMessage",
          "You do not have permission to edit this convention.");

      Gson gson = new Gson();
      return gson.toJson(variables);
    }

    // checking if this is an exam
    String college = WebScraper.collegeName;
    WebScraper scraper = new WebScraper(conventionID);
    WebScraper.setCollege(college);
    String correspondingID = scraper.scrape();

    if (correspondingID != null && !correspondingID.isEmpty()) {
      // this id is associated with an exam
      Map<String, Object> variables = ImmutableMap.of("errorMessage",
          "This is an exam convention.  You cannot add events to it.");

      Gson gson = new Gson();
      return gson.toJson(variables);
    }

    List<Event> priorEvents = db.getEventsFromConventionID(conventionID);
    int eventID = priorEvents.size();

    Gson g = new Gson();
    String[] nameDescription = g.fromJson(eventString, new TypeToken<String[]>() {
    }.getType());

    String eventName = nameDescription[0];
    String eventDescription = nameDescription[1];
    Event currEvent = new Event(eventID, eventName, eventDescription);

    if (!conv.addEvent(currEvent)) {
      Map<String, Object> variables = ImmutableMap.of("errorMessage",
          "A problem occurred.  Please try again.");

      Gson gson = new Gson();
      return gson.toJson(variables);
    }

    Map<String, Object> variables = ImmutableMap.of("errorMessage", "");

    Gson gson = new Gson();
    return gson.toJson(variables);
  }

}
