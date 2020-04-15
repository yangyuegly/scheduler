package edu.brown.cs.student.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.accounts.User;
import edu.brown.cs.student.main.Main;
import edu.brown.cs.student.scheduler.Convention;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;
import java.util.ArrayList;
import java.util.List;
/**
 * This class is used to handle requests to log in.  It implements
 *   TemplateViewRoute.
 */
public class LoginHandler implements TemplateViewRoute  {

  @Override
  public ModelAndView handle(Request req, Response res) {
    QueryParamsMap queryMap = req.queryMap();
    String email = queryMap.value("email");
    String password = queryMap.value("password");
    
    // use the LoginCommand
    
//    User currUser = Database.checkLogin(email, password);
    
    //FOR TESTING ONLY
    User currUser = new User(email);
    
    // sets the cookie so it expires after two hours
    res.cookie("user", email, 120 * 60 * 1000);  
    
    
    
    if (currUser == null) {
      // invalid login
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Incorrect username or password.  Try again.");
      return new ModelAndView(variables, "home.ftl");
    }
    
    
    List<Convention> conventions = currUser.getConventions();

    if (conventions.isEmpty()) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "conventionLinks", "");
      return new ModelAndView(variables, "account.ftl");
    }

    String convString = "<p>Here are your conventions.  Click one to edit or schedule it!</p><br>";

    for (Convention currConv : conventions) {
      String convLink = "<a href=/convention/" + currConv.getID() + ">" + currConv.getName() + "</a>";
      convString+= convLink + "<br>";
    }

    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionLinks", convString);
    return new ModelAndView(variables, "account.ftl");
  }

}
