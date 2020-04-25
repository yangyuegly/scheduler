package edu.brown.cs.student.gui;

//not integrated, need to process the uploaded file
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.csvparser.CSVParser;
import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.LoadCommand;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * Class to allow a user to upload a file.
 * 
 * @author rfuller1
 *
 */
public class UploadHandler implements TemplateViewRoute {
  DatabaseUtility db = new DatabaseUtility();

  @Override
  public ModelAndView handle(Request request, Response response) {
    String userEmail = request.cookie("user");
    String id = request.params(":id");

    boolean permission = db.checkPermission(userEmail, id);
    if (!permission) {
      System.out.println("permission denied");
      response.redirect("/account");
    }

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }
    StringBuilder textBuilder = new StringBuilder();
    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    try (InputStream inputStream = request.raw().getPart("file").getInputStream();
        Reader reader = new BufferedReader(
            new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
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
    // call the load command

    Convention conv = new Convention(id);
    CSVParser parser = new CSVParser();
    LoadCommand load = new LoadCommand();
    load.execute(parser.parse(textBuilder), conv);

    // go to the schedule page
    response.redirect("/schedule/" + id);

    return null;
  }

}
