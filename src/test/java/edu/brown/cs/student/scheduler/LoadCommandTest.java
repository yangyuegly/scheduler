package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LoadCommandTest {
  DatabaseUtility db = new DatabaseUtility();

//  @Test
  public void testLoadNoConflicts() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention");
    List<String[]> input = new ArrayList<String[]>();
    String[] a = new String[] {
        "attendee1", "event1"
    };
    String[] b = new String[] {
        "attendee2", "event2"
    };
    String[] c = new String[] {
        "attendee3", "event3"
    };
    input.add(a);
    input.add(b);
    input.add(c);
    lc.execute(input, convention);

    List<Event> events = db.getEventsFromConventionID("testConvention");
    List<String> eventNames = new ArrayList<>();

    for (Event e : events) {
      eventNames.add(e.getName());
    }

    assertTrue(eventNames.contains("event1") && eventNames.contains("event2")
        && eventNames.contains("event3"));
  }

  // test with conflicts
//  @Test
  public void testLoadConflicts() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention");
    List<String[]> input = new ArrayList<String[]>();
    String[] a = new String[] {
        "attendee1", "event1"
    };
    String[] b = new String[] {
        "attendee2", "event2", "event3"
    };
    input.add(a);
    input.add(b);
    lc.execute(input, convention);

    List<Event> events = db.getEventsFromConventionID("testConvention");
    List<String> eventNames = new ArrayList<>();

    for (Event e : events) {
      eventNames.add(e.getName());
    }

    assertTrue(eventNames.contains("event1") && eventNames.contains("event2")
        && eventNames.contains("event3"));

    HashSet<Conflict> conflicts = DatabaseUtility.getConflictsFromConventionID("testConvention");

    Conflict c = new Conflict(new Event("event2"), new Event("event3"), 0);
    assertTrue(conflicts.contains(c));
    assertEquals(conflicts.size(), 1);
  }

  // test empty file
//@Test
  public void testEmptyFile() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention");
    List<String[]> input = new ArrayList<String[]>();
    lc.execute(input, convention);

    List<Event> events = db.getEventsFromConventionID("testConvention");
    assertTrue(events.isEmpty());
  }

}
