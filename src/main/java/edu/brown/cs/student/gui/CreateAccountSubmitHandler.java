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
    
    // add them to database!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    MongoDatabase database = Main.getDatabase();
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "eventLinks", "");
    
    return new ModelAndView(variables, "account.ftl");
  }

}
