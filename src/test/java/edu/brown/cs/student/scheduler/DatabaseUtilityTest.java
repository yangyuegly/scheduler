package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class DatabaseUtilityTest {
  DatabaseUtility du = new DatabaseUtility();

  @Test
  public void checkPermissionTest() {
    // when the user has permission to view the convention
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "104559"), true);

    // when the convention ID does not exist
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "NonExistingID"), false);

    // when the convention exists, but the user does not have permission to access it
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "417498"), false);
  }

  @Test
  public void getEventsFromConventionIDTest() {
    // when there are no events in a convention
    List<Event> emptyEvents = du.getEventsFromConventionID("315288");
    assertTrue(emptyEvents.isEmpty());

    // when there are several events in a convention
    List<Event> events = du.getEventsFromConventionID("292679");
    Event e1 = new Event(0, "Event 1", "event 1");
    Event e2 = new Event(1, "Event 2", "");

    assertEquals(2, events.size());
    assertTrue(events.contains(e1));
    assertTrue(events.contains(e2));

    // when the convention was not set up
    assertTrue(du.getEventsFromConventionID("absolutely not a real ID").isEmpty());
  }

//  @Test
  public void addConventionDataTest() {
    Convention convention = new Convention("c1", "my convention", "2020-04-10", 3, 90, "07:30",
        "19:30");
    assertEquals(du.addConventionData(convention), true);
  }

//  @Test
  public void addConvIDTest() {
    // if you want to run line 40, change to a new ID.
    // assertEquals(du.addConvID("abby_goldberg@brown.edu", "testConvention2"), true);
    assertEquals(du.addConvID("abby_goldberg@brown.edu", "testConvention1"), false);
  }

  @Test
  public void getConflictsTest() {
    // when there are no conflicts
    Set<Conflict> emptyConflicts = du.getConflictsFromConventionID("121644");
    assertTrue(emptyConflicts.isEmpty());

    // when there are several conflicts
    Set<Conflict> conflicts = du.getConflictsFromConventionID("506840");
    Event e1 = new Event(0, "Event 1", "");
    Event e2 = new Event(1, "Event 2", "");
    Event e3 = new Event(2, "Event 3", "");
    Conflict conflict1 = new Conflict(e1, e2, 1);
    Conflict conflict2 = new Conflict(e2, e1, 1);
    Conflict conflict3 = new Conflict(e2, e3, 1);
    Conflict conflict4 = new Conflict(e3, e2, 1);
    assertEquals(conflicts.size(), 4);
    assertTrue(conflicts.contains(conflict1));
    assertTrue(conflicts.contains(conflict2));
    assertTrue(conflicts.contains(conflict3));
    assertTrue(conflicts.contains(conflict4));
  }

//  @Test
  public void addEventTest() {
    assertTrue(du.addEvent("c1", new Event(0, "justATest", "")));
    assertTrue(du.addEvent("c1", new Event(0, "justATest", "")));
//    assertFalse(du.addEvent("nonExistingConvention", new Event(0, "justATest", "")));
  }

  // @Test
  public void addConflictTest() { // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    Event e1 = new Event(1, "x", "");
    Event e2 = new Event(2, "y", "");
    Conflict c = new Conflict(e1, e2, 100);
    du.addEvent("testConvention", e2);
    Conflict c1 = new Conflict(e2, e1, 100);
    du.addConflict("940576", c1);
//    du.addConflict("940576", c);
//    assertFalse(du.addEvent("nonExistingConvention", new Event(0, "justATest")));
  }

  @Test
  public void getConvention() {
    Convention c1 = du.getConvention("506840");
    assertEquals(c1.getName(), "2 Pairs of Conflicts");

    Integer numDays1 = 3;
    Integer eventDur1 = 60;
    assertEquals(c1.getNumDays(), numDays1);
    assertEquals(c1.getEventDuration(), eventDur1);

    LocalDateTime startTime1 = LocalDateTime.of(2020, 5, 20, 9, 0);
    LocalDateTime endTime1 = LocalDateTime.of(2020, 5, 23, 21, 00);
    assertEquals(c1.getStartDateTime(), startTime1);
    assertEquals(c1.getEndDateTime(), endTime1);

    Convention c2 = du.getConvention("410322");
    assertEquals(c2.getName(), "Abilene Christian University Final Exams");

    Integer numDays2 = 30;
    Integer eventDur2 = 60;
    assertEquals(c2.getNumDays(), numDays2);
    assertEquals(c2.getEventDuration(), eventDur2);

    LocalDateTime startTime2 = LocalDateTime.of(2022, 1, 1, 3, 5);
    LocalDateTime endTime2 = LocalDateTime.of(2022, 1, 31, 10, 10);
    assertEquals(c2.getStartDateTime(), startTime2);
    assertEquals(c2.getEndDateTime(), endTime2);
  }

  @Test
  public void getUserConventionsTest() {
    List<Convention> conventions = du.getUserConventions("rachel@comcast.net");
    assertTrue(conventions.get(0).getID().equals("816365"));
    assertTrue(conventions.get(1).getID().equals("452446"));

    List<Convention> conventions2 = du.getUserConventions("user_with_one_conv@gmail.com");
    assertTrue(conventions2.get(0).getID().equals("230478"));
    assertEquals(conventions2.size(), 1);

    List<Convention> conventions3 = du.getUserConventions("user_with_no_conventions@gmail.com");
    assertTrue(conventions3.isEmpty());

    List<Convention> c1 = du.getUserConventions("invalidUser");
    assertTrue(c1 == null);
  }

//  @Test
  public void addCollabTest() {
    assertTrue(du.addConvIDCollaborator("shenadurai@gmail.com", "000001"));
    assertFalse(du.addConvIDCollaborator("shenadurai1@gmail.com", "000001"));
  }

  @Test
  public void testAddAttendeeEmail() {
    assertTrue(du.addAttendeeEmail("493210", "abby_goldberg@brown.edu"));
    List<String> emails = du.getAttendeeEmailsFromConventionID("493210");
    assertTrue(emails.contains("abby_goldberg@brown.edu"));

    assertTrue(du.addAttendeeEmail("185296", "rachel_fuller@brown.edu"));
    List<String> emails2 = du.getAttendeeEmailsFromConventionID("185296");
    assertTrue(emails2.contains("rachel_fuller@brown.edu"));

    // the ID is not in the database
    assertFalse(du.addAttendeeEmail("not a real id!!!", "rachel_fuller@brown.edu"));
  }

  @Test
  public void testGetAttendeeEmailsFromConventionID() {
    List<String> emails = du.getAttendeeEmailsFromConventionID("493210");
    assertEquals(emails.size(), 2);
    assertEquals(emails.get(0), "rachel_fuller@brown.edu");
    assertEquals(emails.get(1), "abby_goldberg@brown.edu");

    assertTrue(du.getAttendeeEmailsFromConventionID("816365").isEmpty());
  }

  // @Test
  // public void testGeorgeTown() {
  // HashMap<Integer, Integer> map = du.updateEventID("604605");
  // Boolean ret = du.updateConflict("604605", map);
  // System.out.println("iu");
  // assertTrue(ret);
  // }
}
