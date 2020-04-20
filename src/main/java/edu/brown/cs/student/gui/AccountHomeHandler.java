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
      String currUserMessage = "<a href=/home>Log in</a>";
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "currUserMessage", currUserMessage, "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }

    User currUser = new User(userEmail);
    //List<Convention> currConvs = currUser.getConventions(); //don't have all events filled out
   //just here temporarily to test !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    List<Convention> currConvsTest = new ArrayList<>();
    Convention testConvention = new Convention("777");
    testConvention.setName("Book Signing");
    currConvsTest.add(testConvention);
    // /faking it
    
    String conventionLinks = "";
    if (!currConvsTest.isEmpty()) {
      conventionLinks = "<p>Here are your conventions.  Click one to add events or to schedule it!</p>";
    }
    
    for (Convention conv : currConvsTest) {
      String id = conv.getID();
      String link = "<a href=/convention/" + id + ">" + conv.getName() + "</a><br>";
      conventionLinks = conventionLinks + link;
    }
    
    String currUserMessage = "<label>Logged in as <a href=/account>" + userEmail + "</a></label>" +
        "<br><a href=/logout>Log out</a>";
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "currUserMessage", currUserMessage, "conventionLinks", conventionLinks,
        "error", ""); 
    return new ModelAndView(variables, "account.ftl");
  }
}
