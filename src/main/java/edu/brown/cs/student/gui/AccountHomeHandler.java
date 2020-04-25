package edu.brown.cs.student.gui;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.accounts.User;
import edu.brown.cs.student.scheduler.Convention;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

//integrated
/**
 * This class is used to display a user's account home page when the user is logged in. It
 * implements TemplateViewRoute.
 */
public class AccountHomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    String userEmail = req.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }

    User currUser = new User(userEmail);
    List<Convention> currConvs = currUser.getConventions();

    String conventionLinks = "";
    if (!currConvs.isEmpty()) {
      conventionLinks = "<p>Here are your conventions.  Click one to add events or to"
          + " schedule it!</p>";
    }

    for (Convention conv : currConvs) {
      if (conv.isLoaded()) {
        // if the convention is not loaded in the database, the user never finished creating it,
        // and it does not need to be displayed
        String id = conv.getID();
        String link = "<a href=/convention/" + id + ">" + conv.getName() + "</a><br>";
        conventionLinks = conventionLinks + link;
      }
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "conventionLinks",
        conventionLinks, "error", "");

    return new ModelAndView(variables, "account.ftl");
  }
}
