package edu.brown.cs.student.gui;

import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoDatabase;
import com.google.gson.Gson;

import edu.brown.cs.student.main.Main;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateViewRoute;

/**
 * This class is used to save a convention and take the user to their account home page.  It
 *   implements Route.
 */
public class SaveConventionHandler implements Route {

  @Override
  public String handle(Request req, Response response) throws Exception {
    // TO DO: we need to get the id in javascript
    String userEmail = req.cookie("user");
    String conventionID = req.params(":id");
    //Database.checkPermission(userEmail, conventionID);
    
    if (userEmail == null) {
      // user is not logged in
      //Map<String, Object> variables = ImmutableMap.of("title",     ----------------- FIX!!!!!!!!!!!!!!!!!
      //    "Scheduler", "message", "Please log in");
     // return new ModelAndView(variables, "home.ftl");
    }
    
    QueryParamsMap queryMap = req.queryMap();
    String existingEventsString = queryMap.value("existingEvents");
    
    // get convention links
    
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionLinks", "test@!!!!!!!!");
    
    Gson gson = new Gson();
    return gson.toJson(variables);  
  }

}
