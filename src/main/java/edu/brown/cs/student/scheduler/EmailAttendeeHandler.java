package edu.brown.cs.student.scheduler;

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

    List<String> attendeeEmails = db.getAttendeeEmailsFromConventionID(conventionID); // add
                                                                                      // this!!!!!!!!!!!!!!!
    attendeeEmails.add("abby_goldberg@brown.edu"); // delete
    String message;
    if (this.sendEmails(events, attendeeEmails)) {
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
  private boolean sendEmails(List<CalendarEvent> events, List<String> emails) {
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
      InternetAddress address = new InternetAddress(sender);
      message.setFrom(new InternetAddress(sender));

      for (String attendeeEmail : emails) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(attendeeEmail));
      }

      message.setSubject("This is Subject");
      message.setContent("<h1>This is a HTML text</h1>", "text/html");
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

}
