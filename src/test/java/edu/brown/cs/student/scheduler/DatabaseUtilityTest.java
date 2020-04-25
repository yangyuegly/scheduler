package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
    //check duplicates in code
    assertEquals(du.addConvID("abby_goldberg@brown.edu", "testConvID1"), true);
    assertNotEquals(du.addConvID("abby_goldberg@brown.edu", "testConvID1"), false);
  }
}
