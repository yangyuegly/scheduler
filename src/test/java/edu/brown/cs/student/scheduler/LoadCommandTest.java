package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LoadCommandTest {
  DatabaseUtility db = new DatabaseUtility();

  /**
   * Test for no conflicts
   */
  @Test
  public void testLoadNoConflicts() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention1");
    List<List<String>> input = new ArrayList<List<String>>();
    List<String> a = new ArrayList<>();
    a.add("attendee1");
    a.add("event1");

    List<String> b = new ArrayList<>();
    b.add("attendee2");
    b.add("event2");

    input.add(a);
    input.add(b);
    lc.execute(input, convention);

    List<Event> events = db.getEventsFromConventionID("testConvention4");
    Set<Conflict> conflicts = db.getConflictsFromConventionID("testConvention4");
    List<String> eventNames = new ArrayList<>();

    for (Event e : events) {
      eventNames.add(e.getName());
    }

    assertTrue(eventNames.contains("event1") && eventNames.contains("event2"));
    assertTrue(conflicts.isEmpty());
  }

  // test with conflicts
  @Test
  public void testLoadConflicts() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention5");
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

    List<Event> events = db.getEventsFromConventionID("testConvention5");
    List<String> eventNames = new ArrayList<>();

    for (Event e : events) {
      eventNames.add(e.getName());
    }

    assertTrue(eventNames.contains("event1") && eventNames.contains("event2")
        && eventNames.contains("event3"));

    Set<Conflict> conflicts = db.getConflictsFromConventionID("testConvention5");

    Conflict c = new Conflict(new Event(1, "event2", ""), new Event(2, "event3", ""), 1);
    assertTrue(conflicts.contains(c));
    assertEquals(conflicts.size(), 2);
  }

  // test empty file
  @Test
  public void testEmptyFile() {
    LoadCommand lc = new LoadCommand();
    Convention convention = new Convention("testConvention6");
    List<List<String>> input = new ArrayList<List<String>>();
    lc.execute(input, convention);

    List<Event> events = db.getEventsFromConventionID("testConvention6");
    assertTrue(events.isEmpty());
  }

}
