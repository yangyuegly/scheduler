package edu.brown.cs.student.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;

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
    StringBuilder textBuilder = new StringBuilder();
    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    try (InputStream inputStream= request.raw().getPart("file").getInputStream();
        Reader reader = new BufferedReader(new InputStreamReader
            (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
      int c = 0;
      while ((c = reader.read()) != -1) {
          textBuilder.append((char) c);
      }
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (ServletException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
    //process this now
    
    // call the load command

       
    // fix!!!!!!!!!!!!!!!1
    Map<String, Object> variables = ImmutableMap.of("title", // fix!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        "Scheduler", "message", "Send to schedule page!!!");
    return new ModelAndView(variables, "home.ftl"); //send to schedule page
    
  }
  
}
