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
    String userEmail = req.cookie("user");
    
    if (userEmail == null) {
      // user is not logged in
      //Map<String, Object> variables = ImmutableMap.of("title",     ----------------- FIX!!!!!!!!!!!!!!!!!
      //    "Scheduler", "message", "Please log in");
     // return new ModelAndView(variables, "home.ftl");
    }
    
    QueryParamsMap queryMap = req.queryMap();
    String existingEventsString = queryMap.value("existingEvents");
    
    // create a convention id
    Random rand = new Random();
    boolean avail = false;
    // we want a six digit id that has not been used
    Integer id = rand.nextInt((999999-100000) + 1) + 100000;
    //    while (!avail) {
    //     // avail = Main.getDatabase().checkID();
    //      id = rand.nextInt((999999-100000) + 1) + 100000;
    //    }
    
    ///store in database (don't get rid of existing events for this convention)!!
    
    // get convention links
    
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionLinks", "test@!!!!!!!!");
    
    Gson gson = new Gson();
    return gson.toJson(variables);  
  }

}