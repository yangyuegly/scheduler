package edu.brown.cs.student.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

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
    // User currUser = AuthenticationDatabase.checkLogin(email, password);
    
//    if (currUser == null) {
//      // invalid login
//      Map<String, Object> variables = ImmutableMap.of("title",
//          "Scheduler", "message", "Incorrect username or password.  Try again.");
//      return new ModelAndView(variables, "home.ftl");
//    }
    
//    List<Convention> conventions = currUser.getConventions();

//    if (conventions == null) {
//      Map<String, Object> variables = ImmutableMap.of("title",
//          "Scheduler", "eventLinks", "");
//      return new ModelAndView(variables, "account.ftl");
//    }

    String eventString = "<p>Here are your conventions.  Click one to edit or schedule it!</p><br>";

//    for (Convention currConv : conventions) {
//      String convLink = "<a href=/convention/" + currConv.getID() + ">" + currConv.getName() + "</a>";
//      eventString+= convLink + "<br>";
//    }

    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "eventLinks", eventString);
    return new ModelAndView(variables, "account.ftl");
  }

}
