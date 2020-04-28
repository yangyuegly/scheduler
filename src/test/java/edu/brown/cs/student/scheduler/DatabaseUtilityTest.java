package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;

import org.junit.Test;

public class DatabaseUtilityTest {
  DatabaseUtility du = new DatabaseUtility();

  @Test
  public void checkPermissionTest() {
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "211716"), true);
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "NonExistingID"), false);
  }

  @Test
  public void getEventsFromConventionIDTest() {
    List<Event> events = du.getEventsFromConventionID("442715");
    Event e1 = new Event(0, "Javascript/CSS");
    Event e2 = new Event(1, "C++");

    assertTrue(events.contains(e1));
    assertTrue(events.contains(e2));
  }

  @Test
  public void addConventionDataTest() {
    Convention convention = new Convention("c1", "my convention", "2020-04-10", 3, 90, "07:30",
        "19:30");
    assertEquals(du.addConventionData(convention), true);
  }

  @Test
  public void addConvIDTest() {
    // if you want to run line 40, change to a new ID.
    // assertEquals(du.addConvID("abby_goldberg@brown.edu", "testConvention2"), true);
    assertEquals(du.addConvID("abby_goldberg@brown.edu", "testConvention1"), false);
  }

///  @Test
  public void getConflictsTest() {// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    HashSet<Conflict> conflicts = du.getConflictsFromConventionID("c1");
    Event e1 = new Event(0, "e1");
    Event e2 = new Event(1, "e2");
    Conflict conflict = new Conflict(e1, e2, 1);
    assertTrue(conflicts.contains(conflict));
  }

  @Test
  public void addEventTest() {
    assertTrue(du.addEvent("c1", new Event(0, "justATest")));
    assertFalse(du.addEvent("nonExistingConvention", new Event(0, "justATest")));
  }

  // @Test
  public void addConflictTest() { // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    Event e1 = new Event(1, "x");
    Event e2 = new Event(2, "y");
    Conflict c = new Conflict(e1, e2, 100);
    du.addEvent("testConvention", e2);
    Conflict c1 = new Conflict(e2, e1, 100);
    du.addConflict("940576", c1);
//    du.addConflict("940576", c);
//    assertFalse(du.addEvent("nonExistingConvention", new Event(0, "justATest")));
  }

//  @Test
  public void getConvention() {
    Convention c = du.getConvention("388982");
  }

//  @Test
  public void getUserConventionsTest() {
    List<Convention> conventions = du.getUserConventions("abbyjg730@gmail.com");
    assertTrue(conventions.get(0).getID().equals("899966"));
    assertTrue(conventions.get(1).getID().equals("701564"));

    List<Convention> c1 = du.getUserConventions("invalidUser");
    assertTrue(c1 == null);
  }
}
