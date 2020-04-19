package edu.brown.cs.student.gui;
import java.util.ArrayList;
import java.util.List;
// don't think we need it, all commented out
import java.util.Map;


import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.accounts.User;
import edu.brown.cs.student.scheduler.Convention;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display a user's account home page when the user is logged in.  
 *   It implements TemplateViewRoute.
 */
public class AccountHomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    String userEmail = req.cookie("user");
    
    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    User currUser = new User(userEmail);
    //List<Convention> currConvs = currUser.getConventions(); //don't have all events filled out
   //faking it !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    List<Convention> currConvsTest = new ArrayList<>();
    Convention testConvention = new Convention("777");
    testConvention.setName("Book Signing");
    currConvsTest.add(testConvention);
    // /faking it
    
    String conventionLinks = "";
    if (!currConvsTest.isEmpty()) {
      conventionLinks = "Here are your current conventions:<br>";
    }
    
    for (Convention conv : currConvsTest) {
      String id = conv.getID();
      String link = "<a href=/convention/" + id + ">" + conv.getName() + "</a><br>";
      conventionLinks = conventionLinks + link;
    }
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionLinks", conventionLinks, "error", ""); 
    return new ModelAndView(variables, "account.ftl");
  }
}
