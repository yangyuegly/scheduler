package edu.brown.cs.student.gui;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to setup and display the add event page with the convention name.
 */
public class AddEventHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request req, Response res) {
    QueryParamsMap queryMap = req.queryMap();
//    String convID = req.params(":id");
    
    String name = queryMap.value("name");
    String desc = queryMap.value("description");
    String attendees = queryMap.value("attendees");
    
    //put the event and attendees into the database

//    Map<String, Object> variables = ImmutableMap.of("title",
//        "Scheduler");
//    return new ModelAndView(variables, "add_event.ftl");
    
    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", 
        name);
    return new ModelAndView(variables, "convention_home.ftl");
  }

}
