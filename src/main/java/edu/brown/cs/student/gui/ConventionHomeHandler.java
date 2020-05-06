package edu.brown.cs.student.gui;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.Event;
import edu.brown.cs.student.webscraper.WebScraper;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

/**
 * This class is used to handle requests to the home page of a convention.
 */
public class ConventionHomeHandler implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) {
    String conventionID = request.params(":id");
    String userEmail = request.cookie("user");

    if (userEmail == null) {
      // user is not logged in
      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "message",
          "Please log in");
      return new ModelAndView(variables, "home.ftl");
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean authorized = db.checkPermission(userEmail, conventionID);

    if (!authorized) {
      response.redirect("/unauthorized");
    }

    // checking if this is an exam
    String college = WebScraper.getcollegeID();
    WebScraper scraper = new WebScraper(conventionID);
    Map<String, String> schoolNameToIDMap = scraper.getcoursesToIDs();
    Convention school = db.getConvention(conventionID);
    String schoolN = school.getName();
    String[] schoolNameArray = schoolN.split(" ");
    String schoolName = "";
    for (int i = 0; i < schoolNameArray.length - 2; i++) {
      schoolName += schoolNameArray[i] + " ";
    }
    // remove "Final Exams"
    String schoolID = schoolNameToIDMap.get(schoolName.trim());
    WebScraper.setCollege(schoolID);
    System.out.println("schoolID: " + schoolID);
    String correspondingID = scraper.scrape();

    if (correspondingID != null && !correspondingID.isEmpty()) {
      // this id is associated with an exam
      conventionID = correspondingID;
    }

    Convention currConv = new Convention(conventionID);

    if (!currConv.isLoaded()) {
      // this convention was never set up with the name and time information

      // gets the current date (we don't want the user to schedule an event in the past)
      LocalDate today = LocalDate.now();

      Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "currDay", today, "id",
          conventionID, "errorMessage", "");

      return new ModelAndView(variables, "setup_conv.ftl");
    }

    String convName = currConv.getName();
    List<Event> events = currConv.getEvents();
    String existingEvents = "";

    // this creates a string that tells the user what events are already in this convention
    for (Event event : events) {
      existingEvents += makeEventBox(event, existingEvents);
    }

    if (existingEvents.equals("")) {
      existingEvents = "No events yet.";
    }

    Map<String, Object> variables = ImmutableMap.of("title", "Scheduler", "id", conventionID,
        "convName", convName, "existingEvents", existingEvents);

    return new ModelAndView(variables, "convention_home.ftl");
  }

  /**
   * This method is used to make the HTML object that displays the given event.
   *
   * @param currEvent - an Event, which represents an event to be displayed
   * @param existingEvents - a String, which represents the String of events that have already been
   *        converted into HTML form
   *
   * @return a String, which represents the HTML object that displays the given event.
   */
  private String makeEventBox(Event currEvent, String existingEvents) {
    String description = currEvent.getDescription();
    String eventString = "<p></p>";

    if (!existingEvents.equals("")) {
      // add a break in between events
      eventString += "<p></p>";
    }

    eventString += "<button type=\"button\" class=\"collapsible\">" + currEvent.getName()
        + "</button>\r\n" + "<div class=\"content\">\r\n" + "<p>";

    if (description.equals("")) {
      eventString += "No description.";
    } else {
      eventString += currEvent.getDescription();
    }

    return eventString + "</p>\r\n" + "</div>";
  }
}
