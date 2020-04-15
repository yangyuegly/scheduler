package edu.brown.cs.student.gui;

import java.io.File;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * Class to allow a user to upload a file.
 * @author rfuller1
 *
 */
public class UploadHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");
    
    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    
    QueryParamsMap queryMap = request.queryMap();
    
    
    
    String name = queryMap.value("name");
    String file = queryMap.value("file");
    
    
    // fix!!!!!!!!!!!!!!!1
//    Map<String, Object> variables = ImmutableMap.of("title",
//        "Scheduler", "message", "Please log in");
//    return new ModelAndView(variables, "home.ftl");
    
    return null;
    
  }
  
}
