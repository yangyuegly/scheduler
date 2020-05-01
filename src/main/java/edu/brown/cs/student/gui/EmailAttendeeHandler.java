package edu.brown.cs.student.gui;

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

  @Override
  public Object handle(Request request, Response response) throws Exception {
    String conventionID = request.params(":id");

    String userEmail = request.cookie("user");

    if (userEmail == null) {
      response.redirect("/home"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean authorized = db.checkPermission(userEmail, conventionID);

    if (!authorized) {
      response.redirect("/unauthorized"); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    QueryParamsMap queryMap = request.queryMap();
    String eventString = queryMap.value("events");
    System.out.println("event string is " + eventString);
    Gson g = new Gson();
    List<CalendarEvent> events = g.fromJson(eventString, new TypeToken<List<CalendarEvent>>() {
    }.getType());

    System.out.println("num events is" + events.size());

    String emailContent = "";
    for (CalendarEvent event : events) {
      emailContent = emailContent + "<p>" + event.getTitle() + ": "
          + friendlyDate(event.getStart().toString()) + " to "
          + friendlyDate(event.getEnd().toString()) + "</p>";
    }

    Convention myConv = db.getConvention(conventionID);

    List<String> attendeeEmails = db.getAttendeeEmailsFromConventionID(conventionID); // add
                                                                                      // this!!!!!!!!!!!!!!!
    attendeeEmails.add("rachel_fuller@brown.edu"); // delete
    // attendeeEmails.add("abby_goldberg@brown.edu");
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
    // use the
    // events!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
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
      String end = "<p>This schedule made with Sked and sent by the convention organizer.</p></body>";
      message.setContent(header + emailContent + end, "text/html");
      Transport.send(message);
      System.out.println("Mail successfully sent"); // delete!!!!!!!!!!!!!!!!!!!!
    } catch (MessagingException mex) {
      mex.printStackTrace(); // do better than
                             // this!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
      System.out.println("send failed"); // delete
      return false;
    }

    return true;

  }

  String friendlyDate(String dateString) {
    // 2020-12-21T09:00
    String res = "";
    String[] dateTime = dateString.split("T");
    res = res + dateTime[0] + " " + dateTime[1];
    return res;
  }

}
