package edu.brown.cs.student.gui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

import edu.brown.cs.student.scheduler.CalendarEvent;
import edu.brown.cs.student.scheduler.Convention;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.ScheduleCommand;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This class is used to get the events and their times in a format that is compatible with the
 * FullCalendar API's javascript. It implements TemplateViewRoute.
 */
public class CalendarHandler implements Route {

  @Override
  public String handle(Request req, Response res) {
    String conventionID = req.params(":id");

    String userEmail = req.cookie("user");

    if (userEmail == null) {
      res.redirect("/home");
    }

    DatabaseUtility db = new DatabaseUtility();
    boolean authorized = db.checkPermission(userEmail, conventionID);

    if (!authorized) {
      res.redirect("/unauthorized");
    }

    Convention myConv = db.getConvention(conventionID);
    int numTimeSlotsPerDay = myConv.getNumTimeSlotsPerDay();

    ScheduleCommand schedComm = new ScheduleCommand(myConv, 100, myConv.getNumDays(),
        numTimeSlotsPerDay);
    List<CalendarEvent> eventsSched;

    try {
      eventsSched = schedComm.execute();
//      List<String> attendeeEmails = db.getAttendeeEmails(conventionID); // add this!!!!!!!!!!!!!!!

      List<String> attendeeEmails = new ArrayList<>(); // delete these lines
      attendeeEmails.add("abby_goldberg@brown.edu");
      attendeeEmails.add("rachel_fuller@brown.edu");
      sendEmails(eventsSched, attendeeEmails);

    } catch (NullPointerException err) {
      // there was an error with the scheduling
      Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", "", "defaultDate", "",
          "error", "We're sorry, we couldn't make a schedule for you. There was no way to avoid"
              + " conflicts between your events.");
      Gson gson = new Gson();

      return gson.toJson(variables);
    }

<<<<<<< HEAD
     System.out.println("just executed schedule command"); // delete

=======
>>>>>>> 59d4cd4ae24ead33f162708a244778705bb88ae9
    LocalDateTime convStartWithTime = myConv.getStartDateTime();
    LocalDate convStartDay = convStartWithTime.toLocalDate();

    Map<String, Object> variables = ImmutableMap.of("eventsForSchedule", eventsSched, "defaultDate",
        convStartDay.toString(), "error", "");
    Gson gson = new Gson();

    return gson.toJson(variables);
  }

  // do!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
  private boolean sendEmails(List<CalendarEvent> events, List<String> emails) {
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
