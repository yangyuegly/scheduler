package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * This class is used to test the methods in the Convention class.
 */
public class ConventionTest {

  @Test
  /**
   * This method is used to test the getNumTimeSlots method in the Convention class.
   */
  public void testGetNumTimeSlots() {
    Convention conv = new Convention("1", "conv", "2020-04-10", 3, 90, "07:30", "19:30");
    assertEquals(conv.getNumTimeSlotsPerDay(), (Integer) 8);

    Convention conv2 = new Convention("2", "conv", "2020-04-10", 3, 90, "07:30", "19:00");
    assertEquals(conv2.getNumTimeSlotsPerDay(), (Integer) 7);

    Convention conv3 = new Convention("3", "conv", "2022-11-30", 2, 30, "08:45", "12:30");
    assertEquals(conv3.getNumTimeSlotsPerDay(), (Integer) 7);

    Convention conv4 = new Convention("4", "conv", "2022-11-30", 1, 180, "08:45", "10:30");
    assertEquals(conv4.getNumTimeSlotsPerDay(), (Integer) 0);
  }

  @Test
  public void testGetStartDateTime() {
    // using constructor that takes in Strings for the dates and times
    Convention conv = new Convention("1", "conv", "2020-04-10", 3, 90, "07:30", "19:30");
    LocalDateTime startDateTime = LocalDateTime.of(2020, 4, 10, 7, 30);
    assertEquals(startDateTime, conv.getStartDateTime());

    Convention conv2 = new Convention("2", "conv2", "2020-07-21", 2, 90, "13:30", "17:00");
    LocalDateTime startDateTime2 = LocalDateTime.of(2020, 7, 21, 13, 30);
    assertEquals(startDateTime2, conv2.getStartDateTime());

    // using constructor that takes in LocalDateTimes
    LocalDateTime startDateTime3 = LocalDateTime.of(2020, 7, 21, 15, 45);
    LocalDateTime endDateTime3 = LocalDateTime.of(2020, 7, 21, 19, 30);
    Convention conv3 = new Convention("3", "conv3", startDateTime3, 2, 90, endDateTime3);
    assertEquals(startDateTime3, conv3.getStartDateTime());

    // using constructor that only takes in the ID and uses the database to get the rest of the data
    Convention conv4 = new Convention("493210");
    LocalDateTime startDateTime4 = LocalDateTime.of(2032, 10, 24, 10, 35);
    assertEquals(startDateTime4, conv4.getStartDateTime());
  }

  @Test
  public void testGetEndDateTime() {
    // using constructor that takes in Strings for the dates and times
    Convention conv = new Convention("1", "conv", "2020-04-10", 3, 90, "07:30", "19:30");
    LocalDateTime endDateTime = LocalDateTime.of(2020, 4, 13, 19, 30);
    assertEquals(endDateTime, conv.getEndDateTime());

    Convention conv2 = new Convention("2", "conv2", "2020-07-21", 2, 90, "13:30", "17:00");
    LocalDateTime endDateTime2 = LocalDateTime.of(2020, 7, 23, 17, 0);
    assertEquals(endDateTime2, conv2.getEndDateTime());

    // using constructor that takes in LocalDateTimes
    LocalDateTime startDateTime3 = LocalDateTime.of(2020, 7, 21, 15, 45);
    LocalDateTime endDateTime3 = LocalDateTime.of(2020, 7, 21, 19, 30);
    Convention conv3 = new Convention("3", "conv3", startDateTime3, 2, 90, endDateTime3);
    assertEquals(endDateTime3, conv3.getEndDateTime());

    // using constructor that only takes in the ID and uses the database to get the rest of the data
    Convention conv4 = new Convention("493210");
    LocalDateTime endDateTime4 = LocalDateTime.of(2032, 10, 27, 18, 5);
    assertEquals(endDateTime4, conv4.getEndDateTime());
  }

  @Test
  public void testGetEvents() {
    // when there are no events in a convention
    Convention conv = new Convention("315288");
    List<Event> emptyEvents = conv.getEvents();
    assertTrue(emptyEvents.isEmpty());

    // when there are several events in a convention
    Convention conv2 = new Convention("292679");
    List<Event> events = conv2.getEvents();
    Event e1 = new Event(0, "Event 1", "event 1");
    Event e2 = new Event(1, "Event 2", "");

    assertEquals(2, events.size());
    assertTrue(events.contains(e1));
    assertTrue(events.contains(e2));

    // when the convention was not set up
    Convention conv3 = new Convention("absolutely not a real ID");
    assertTrue(conv3.getEvents().isEmpty());
  }

  @Test
  public void testGetConflicts() {
    // when there are no conflicts
    Convention conv = new Convention("121644");
    Set<Conflict> emptyConflicts = conv.getConflicts();
    assertTrue(emptyConflicts.isEmpty());

    // when there are several conflicts
    Convention conv2 = new Convention("506840");
    Set<Conflict> conflicts = conv2.getConflicts();
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

}
