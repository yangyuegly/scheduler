package edu.brown.cs.student.gui;

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
    String eventsToAddString = queryMap.value("existingEvents"); // deal with
                                                                 // this!!!!!!!!!!!!!!!!!!!!!!!!
    Gson g = new Gson();
    List<String[]> eventNameDescrList = g.fromJson(eventsToAddString, List.class);

    System.out.println(eventsToAddString);
    System.out.println("first elem:" + eventNameDescrList.get(0)[0]);

//    ObjectMapper mapper = new ObjectMapper();
//    String[][] eventNameDescrArray = mapper.readValue(eventsToAddString,
//        new TypeReference<String[][]>() {
//        });

    int id = 0;

    for (String[] nameDescr : eventNameDescrList) {
      String eventName = nameDescr[0];
      String eventDescription = nameDescr[1];

      Event currEvent = new Event(id, eventName, eventDescription);
      id++;

      if (!db.addEvent(conventionID, currEvent)) {
        // the convention ID is not in the database
        // ?? IDK what to
        // do!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1!!!!
        System.err.println("add event was unsuccessful");
      }
    }

    // adding a collaborator
    String collaboratorEmail = queryMap.value("colEmail");
    db.addConvID(collaboratorEmail, conventionID);

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "conventionLinks",
        "test@!!!!!!!!"); // ?????????????????????????????????????????????????????????????????????

    Gson gson = new Gson();
    return gson.toJson(variables);
  }

}
