package edu.brown.cs.student.gui;

//integrated
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to log the user out. It implements TemplateViewRoute.
 */
public class LogoutHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    // remove the cookie for this user
    response.removeCookie("user");

    response.redirect("/login");
    return null; // this line will not be reached
  }

}
