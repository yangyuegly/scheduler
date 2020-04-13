package edu.brown.cs.student.gui;


import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.main.Main;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;
/**
 * Class that is a handler for the creating a convention page.
 *
 */
public class CreateConventionHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    //gets the current date (user can't schedule an event in the past)
    Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int year = cal.get(Calendar.YEAR);
    
    String date = year + "-" + month + "-" + day;
    
    Random rand = new Random();
    boolean avail = false;
    // we want a six digit id that has not been used
    Integer id = rand.nextInt((999999-100000) + 1) + 100000;
    while (!avail) {
     // avail = Main.getDatabase().checkID();
      id = rand.nextInt((999999-100000) + 1) + 100000;
    }
    
    Map<String, Object> variables = ImmutableMap.of("title",
        "Scheduler", "currDay", date, "id", id);
    
    return new ModelAndView(variables, "createconv.ftl");    
  }
  
  

}


