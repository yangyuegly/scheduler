package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.student.scheduler.DatabaseUtility;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class is used to add a collaborator to a convention, so the collaborator can also work on
 * the convention. It implements Route.
 */
public class AddCollaboratorHandler implements Route {

  @Override
  public String handle(Request req, Response response) throws Exception {
    String userEmail = req.cookie("user");
    String conventionID = req.params(":id");

    QueryParamsMap queryMap = req.queryMap();
    String eventString = queryMap.value("event");

    DatabaseUtility db = new DatabaseUtility();
    boolean permission = db.checkPermission(userEmail, conventionID);

    if (!permission) {
      Map<String, Object> variables = ImmutableMap.of("errorMessage",
          "You do not have permission to add a collaborator.", "successMessage", "");
      Gson gson = new Gson();
      return gson.toJson(variables);
    }

    // adding a collaborator
    String collaboratorEmail = queryMap.value("colEmail");

    if (!db.addConvIDCollaborator(collaboratorEmail, conventionID)) {
      // an error occurred
      Map<String, Object> variables = ImmutableMap.of("errorMessage",
          "An error occurred.  Please try again.", "successMessage", "");
      Gson gson = new Gson();
      return gson.toJson(variables);
    }

    Map<String, Object> variables = ImmutableMap.of("errorMessage", "", "successMessage", "Added!");

    Gson gson = new Gson();
    return gson.toJson(variables);
  }
}
