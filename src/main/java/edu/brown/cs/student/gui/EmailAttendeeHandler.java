package edu.brown.cs.student.gui;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.brown.cs.student.scheduler.CalendarEvent;
import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class is used to email the attendees of a convention with the convention's schedule. It
 * implements Route.
 */
public class EmailAttendeeHandler implements Route {

  /**
   * This is a field for this class.
   *
   * AM_TO_PM_BOUNDARY - an int, which is used to determine if a time is in AM or PM
   */
  private static final int AM_TO_PM_BOUNDARY = 12;

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String conventionID = request.params(":id");

    String userEmail = request.cookie("user");

    if (userEmail == null) {
      Map<String, Object> variables = ImmutableMap.of("message",
          "You are not authorized to send emails.");
      Gson gson = new Gson();

      return gson.toJson(variables);
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean authorized = db.checkPermission(userEmail, conventionID);

    if (!authorized) {
      Map<String, Object> variables = ImmutableMap.of("message",
          "You are not authorized to send emails.");
      Gson gson = new Gson();

      return gson.toJson(variables);
    }

    QueryParamsMap queryMap = request.queryMap();
    String eventString = queryMap.value("events");
    Gson g = new Gson();
    List<CalendarEvent> events = g.fromJson(eventString, new TypeToken<List<CalendarEvent>>() {
    }.getType());

    String emailContent = "";
    for (CalendarEvent event : events) {
      emailContent = emailContent + "<p>" + event.getTitle() + ": "
          + friendlyDate(event.getStart(), event.getEnd()) + "</p>";
    }

    Convention myConv = db.getConvention(conventionID);

    List<String> attendeeEmails = db.getAttendeeEmailsFromConventionID(conventionID);
    String message;

    if (attendeeEmails.isEmpty()) {
      message = "No email sent because no attendees have signed up.";
    } else if (this.sendEmails(events, attendeeEmails, myConv.getName(), emailContent)) {
      message = "Schedule sent!";
    } else {
      message = "Email could not be sent.";
    }

    Map<String, Object> variables = ImmutableMap.of("message", message);
    Gson gson = new Gson();

    return gson.toJson(variables);
  }

  /**
   * Method to send emails to attendees.
   *
   * @param events -- events to send to the attendees.
   * @param emails -- the emails of the attendees.
   *
   * @return -- true if sent, false if could not be sent.
   */
  private boolean sendEmails(List<CalendarEvent> events, List<String> emails, String convName,
      String emailContent) {
    String sender = "sked.organizer@gmail.com";

    String host = "smtp.gmail.com";
    // Get system properties
    Properties properties = System.getProperties();
    // Setup mail server
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", "465");
    properties.put("mail.smtp.ssl.enable", "true");
    properties.put("mail.smtp.auth", "true");

    // Get the Session object.// and pass username and password
    Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(sender, "term_project");
      }
    });

    try {
      MimeMessage message = new MimeMessage(session);
      InternetAddress address = new InternetAddress("Sked Project<" + sender + ">");
      message.setFrom(address);

      for (String attendeeEmail : emails) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(attendeeEmail));
      }

      message.setSubject("Schedule for " + convName);

      String header = "<body> <h1>Here's the schedule for " + convName + " </h1>";
      String end = "<p>This schedule made with Sked and sent by the convention organizer."
          + "</p></body>";
      message.setContent(header + emailContent + end, "text/html");
      Transport.send(message);
    } catch (MessagingException mex) {
      System.err.println("ERROR: email sending failed");
      return false;
    }

    return true;
  }

  /**
   * This method turns the start date/time and end date/time into a String that is easily
   * understandable for the schedule receiver.
   *
   * @param startString - a String, in the format of a LocalDateTime turned into a String, which
   *        represents the start date and time for an event
   * @param endString - a String, in the format of a LocalDateTime turned into a String, which
   *        represents the end date and time for an event
   *
   * @return a String, which represents the time of the event in a form that is easily
   *         understandable for the schedule receiver
   */
  private String friendlyDate(String startString, String endString) {
    String res = "";
    String[] startDateTime = startString.split("T");
    String[] endDateTime = endString.split("T");

    String[] date = startDateTime[0].split("-");
    String[] startTime = startDateTime[1].split(":");
    String[] endTime = endDateTime[1].split(":");

    try {
      int year = Integer.parseInt(date[0]);
      int month = Integer.parseInt(date[1]);
      int day = Integer.parseInt(date[2]);
      int startHour = Integer.parseInt(startTime[0]);
      int startMin = Integer.parseInt(startTime[1]);
      int endHour = Integer.parseInt(endTime[0]);
      int endtMin = Integer.parseInt(endTime[1]);

      LocalDateTime locDateTime = LocalDateTime.of(year, month, day, startHour, startMin);
      String startAmPm = "AM";
      String endAmPm = "AM";

      if (startHour > AM_TO_PM_BOUNDARY) {
        startAmPm = "PM";
        startHour = startHour - AM_TO_PM_BOUNDARY;
      }

      if (endHour > AM_TO_PM_BOUNDARY) {
        endAmPm = "PM";
        endHour = endHour - AM_TO_PM_BOUNDARY;
      }

      String startMinute = startMin + "";
      String endMinute = endtMin + "";

      if (startMinute.length() == 1) {
        startMinute = "0" + startMinute;
      }

      if (endMinute.length() == 1) {
        endMinute = "0" + endMinute;
      }

      String dayAllCaps = locDateTime.getDayOfWeek().toString();
      String dayString = dayAllCaps.substring(0, 1)
          + dayAllCaps.substring(1, dayAllCaps.length()).toLowerCase();

      String monthAllCaps = locDateTime.getMonth().toString();
      String monthString = monthAllCaps.substring(0, 1)
          + monthAllCaps.substring(1, monthAllCaps.length()).toLowerCase();

      return dayString + ", " + monthString + " " + day + " from " + startHour + ":" + startMinute
          + " " + startAmPm + " to " + endHour + ":" + endMinute + " " + endAmPm;
    } catch (NumberFormatException err) {
      System.err.println("ERROR: invalid date-time for event");
      return "Unable to get date information for an event";
    }
  }

}
