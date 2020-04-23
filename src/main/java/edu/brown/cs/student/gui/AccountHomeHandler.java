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

//integrated
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

    //comment b/c getConventions not working
//    List<Convention> currConvs = currUser.getConventions(); //don't have all events filled out
//   
//    
//    String conventionLinks = "";
//    if (!currConvs.isEmpty()) {
//      conventionLinks = "<p>Here are your conventions.  Click one to add events or to"
//          + " schedule it!</p>";
//    }
//    
//    for (Convention conv : currConvs) {
//      String id = conv.getID();
//      String link = "<a href=/convention/" + id + ">" + conv.getName() + "</a><br>";
//      conventionLinks = conventionLinks + link;
//    }

    String conventionLinks = "";
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "conventionLinks", conventionLinks, "error", ""); 
    return new ModelAndView(variables, "account.ftl");
  }
}
