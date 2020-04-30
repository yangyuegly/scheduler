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

//  @Test
  public void checkPermissionTest() {
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "211716"), true);
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "NonExistingID"), false);
  }

  // @Test
  public void getEventsFromConventionIDTest() {
    List<Event> events = du.getEventsFromConventionID("442715");
    Event e1 = new Event(0, "Javascript/CSS", "");
    Event e2 = new Event(1, "C++", "");

    assertTrue(events.contains(e1));
    assertTrue(events.contains(e2));
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

///  @Test
  public void getConflictsTest() {// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    Set<Conflict> conflicts = du.getConflictsFromConventionID("c1");
    Event e1 = new Event(0, "e1", "");
    Event e2 = new Event(1, "e2", "");
    Conflict conflict = new Conflict(e1, e2, 1);
    assertTrue(conflicts.contains(conflict));
  }

  @Test
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

//  @Test
  public void getConvention() {
    Convention c1 = du.getConvention("496457");
    assertEquals(c1.getName(), "Test Upload 2");

    Integer numDays1 = 3;
    Integer eventDur1 = 30;
    assertEquals(c1.getNumDays(), numDays1);
    assertEquals(c1.getEventDuration(), eventDur1);

    LocalDateTime startTime1 = LocalDateTime.of(2020, 12, 13, 22, 52);
    LocalDateTime endTime1 = LocalDateTime.of(2020, 12, 16, 23, 50);
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

//  @Test
  public void getUserConventionsTest() {
    List<Convention> conventions = du.getUserConventions("abbyjg730@gmail.com");
    assertTrue(conventions.get(0).getID().equals("388982"));
    assertTrue(conventions.get(1).getID().equals("727744"));
    assertTrue(conventions.get(2).getID().equals("509857"));
    assertTrue(conventions.get(3).getID().equals("748488"));

    List<Convention> conventions2 = du.getUserConventions("user_with_one_conv@gmail.com");
    assertTrue(conventions2.get(0).getID().equals("230478"));
    assertEquals(conventions2.size(), 1);

    List<Convention> conventions3 = du.getUserConventions("user_with_no_conventions@gmail.com");
    assertTrue(conventions3.isEmpty());

    List<Convention> c1 = du.getUserConventions("invalidUser");
    assertTrue(c1 == null);
  }

  @Test
  public void addCollabTest() {
    assertTrue(du.addCollaborator("shenadurai@gmail.com", "000001"));
    assertFalse(du.addCollaborator("shenadurai1@gmail.com", "000001"));
  }

}
