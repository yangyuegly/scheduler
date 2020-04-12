package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to display the add event page with the convention name.
 */
public class AddEventHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    QueryParamsMap queryMap = req.queryMap();
    String convName = queryMap.value("convName");

//    Map<String, Object> variables = ImmutableMap.of("title",
//        "Scheduler", "eventLinks", eventString);
//    return new ModelAndView(variables, "add_event.ftl");
    
    return null;
  }

}
