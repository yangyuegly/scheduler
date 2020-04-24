package edu.brown.cs.student.gui;
//integrated
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.accounts.User;
import edu.brown.cs.student.exception.UserAuthenticationException;
import edu.brown.cs.student.main.Main;
import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.Event;
import edu.brown.cs.student.scheduler.LoginCommand;
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
  public ModelAndView handle(Request request, Response response) {
    QueryParamsMap queryMap = request.queryMap();
    String email = queryMap.value("email");
    String password = queryMap.value("password");
    LoginCommand loginComm = new LoginCommand();

    try {
      loginComm.execute(email, password);

    } catch (UserAuthenticationException e) {
      String message = e.getMessage();
      
      String currUserEmail = request.cookie("user");
      
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message", message);
      return new ModelAndView(variables, "home.ftl"); // how would we redirect with message????????????????????????
    }
    
    // sets the cookie so it expires after two hours
    response.cookie("user", email, 72000000); //120 * 60 * 1000);  
    
    response.redirect("/account");
    
    return null; // is this ok??? don't think it gets here !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  }

}
