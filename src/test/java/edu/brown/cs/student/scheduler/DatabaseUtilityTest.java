package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;

public class DatabaseUtilityTest {
  DatabaseUtility du = new DatabaseUtility();

//  @Test
  public void checkPermissionTest() {
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "792513"), true);
    assertEquals(du.checkPermission("abby_goldberg@brown.edu", "NonExistingID"), false);
  }

//  @Test
  public void getEventsFromConventionIDTest() {
    List<Event> events = du.getEventsFromConventionID("c1");
    Event e1 = new Event(0, "e1");
    Event e2 = new Event(1, "e2");
    assertTrue(events.contains(e1));
    assertTrue(events.contains(e2));
  }

//  @Test
  public void addConventionDataTest() {
    Convention convention = new Convention("c1");
    assertEquals(du.addConventionData(convention), true);
    assertEquals(du.addConventionData(convention), false);
  }

//  @Test
  public void addConvIDTest() {
    // check duplicates in code
    assertEquals(du.addConvID("abby_goldberg@brown.edu", "testConvID1"), true);
    assertNotEquals(du.addConvID("abby_goldberg@brown.edu", "testConvID1"), false);
  }

//  @Test
  public void getConflictsTest() {
    HashSet<Conflict> conflicts = du.getConflictsFromConventionID("c1");
    Event e1 = new Event(0, "e1");
    Event e2 = new Event(1, "e2");
    Conflict conflict = new Conflict(e1, e2, 1);
//    assertTrue(conflicts.contains(conflict));
  }

//  @Test
  public void addEventTest() {
    assertTrue(du.addEvent("c1", new Event(0, "justATest")));
    assertFalse(du.addEvent("nonExistingConvention", new Event(0, "justATest")));
  }

//  @Test
//  public void getConventionDataTest() {
//    String[] res1 = du.getConventionData("986329");
//    assertEquals("986329", res1[0]);
//    assertEquals("Hack at Brown 2050", res1[1]);
//
//    String[] res2 = du.getConventionData("nonExisting");
//    assertTrue(res2 == null);
//  }

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
