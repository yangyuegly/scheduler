package edu.brown.cs.student.gui;

import java.io.File;
import java.util.Map;
import java.util.List;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.csvparser.CSVParser;
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
    String id = request.params(":id");
    
    // check this user can access this convention !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    
    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title",
          "Scheduler", "message", "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    
    QueryParamsMap queryMap = request.queryMap();
    String filename = queryMap.value("file");
    
    CSVParser parser = new CSVParser(filename); // also pass in first line to make sure it is right!!!!!!!!
    
    if (!parser.parse()) {
      // an error occurred
//      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "convName", // doesn't know what names is
//          name, "message", "Invalid file.");
//      return new ModelAndView(variables, "upload_conv.ftl");
    }
    
//    List<String[]> csvData = parser.getFileData();
//    
//    for (String[] row : csvData) {
//      for (String elem : row) {
//        System.out.println(elem);
//      }
//    }
    
    
    
    
    
    String name = queryMap.value("name");
    String file = queryMap.value("file");
    
    
    // fix!!!!!!!!!!!!!!!1
//    Map<String, Object> variables = ImmutableMap.of("title",
//        "Scheduler", "message", "Please log in");
//    return new ModelAndView(variables, "home.ftl");
    
    return null;
    
  }
  
}
