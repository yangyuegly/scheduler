package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoadCommandTest {
  DatabaseUtility db = new DatabaseUtility();

//  @Test
  public void testLoadNoConflicts() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention", "testName");
    List<List<String>> input = new ArrayList<List<String>>();
    List<String> a = new ArrayList<>();
    a.add("attendee1");
    a.add("event1");

    List<String> b = new ArrayList<>();
    b.add("attendee2");
    b.add("event2");
    b.add("event3");

    input.add(a);
    input.add(b);
    lc.execute(input, convention);

//    List<Event> events = db.getEventsFromConventionID("testConvention");
//    List<String> eventNames = new ArrayList<>();
//
//    for (Event e : events) {
//      eventNames.add(e.getName());
//    }
//
//    assertTrue(eventNames.contains("event1") && eventNames.contains("event2")
//        && eventNames.contains("event3"));
  }

  // test with conflicts
//  @Test
  public void testLoadConflicts() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention");
    List<List<String>> input = new ArrayList<List<String>>();
    List<String> a = new ArrayList<>();
    a.add("attendee1");
    a.add("event1");

    List<String> b = new ArrayList<>();
    b.add("attendee2");
    b.add("event2");
    b.add("event3");

    lc.execute(input, convention);

    List<Event> events = db.getEventsFromConventionID("testConvention");
    List<String> eventNames = new ArrayList<>();

    for (Event e : events) {
      eventNames.add(e.getName());
    }

    assertTrue(eventNames.contains("event1") && eventNames.contains("event2")
        && eventNames.contains("event3"));

    Set<Conflict> conflicts = db.getConflictsFromConventionID("testConvention");

    Conflict c = new Conflict(new Event(2, "event2", ""), new Event(2, "event3", ""), 0);
    assertTrue(conflicts.contains(c));
    assertEquals(conflicts.size(), 1);
  }

  // test empty file
//@Test
  public void testEmptyFile() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention");
    List<List<String>> input = new ArrayList<List<String>>();
    lc.execute(input, convention);

    List<Event> events = db.getEventsFromConventionID("testConvention");
    assertTrue(events.isEmpty());
  }

}
