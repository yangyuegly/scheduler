package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoDatabase;

import edu.brown.cs.student.main.Main;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to add a newly created account to the database and send the new user
 *  to their account's home page.
 */
public class CreateAccountSubmitHandler implements TemplateViewRoute {
  
  @Override
  public ModelAndView handle(Request req, Response res) {
    QueryParamsMap queryMap = req.queryMap();
    String email = queryMap.value("email");
    String password = queryMap.value("password");
    
    
    boolean add = false; //fix this, see below
    // boolean add = Database.addUser(email, password);
    if (!add) {
      // redirect back to login page ???
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "errorMessage", "");
      
      return new ModelAndView(variables, "create_account.ftl");
    } else {
 // sets the cookie so it expires after two hours
    res.cookie("user", email, 120 * 60 * 1000);  
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionLinks", "", "error", "");
    
      return new ModelAndView(variables, "account.ftl");
  }
  }

}
