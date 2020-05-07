package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the ScheduleCommand class.
 *
 */
public class ScheduleCommandTest {
  DatabaseUtility du = new DatabaseUtility();

  Convention myConv;
  @Before 
  public void setUp() {
    du = new DatabaseUtility();
    myConv = du.getConvention("616997");

  }
  
  @After 
  public void tearDown() {
     du = null;
     myConv = null;

  }
  // @Test
  // public void testExecute() {
  //   setUp();
  //   assertEquals("Brown University Final Exams", myConv.getName());
  //   System.out.println(myConv);
  //   List<Event> events = du.getEventsFromConventionID("616997");
  //   assertEquals(101,events.size());
  //   ScheduleCommand sched = new ScheduleCommand(myConv, 30, 10, 5, "616997");
  //   sched.setNodes(events);
  //   assertEquals(101, sched.getNodes().size());
  //   List<CalendarEvent> result = sched.execute();
  //   assertEquals(101, result.size());
  //   tearDown();
  // }


  @Test
  public void noConflictsTest() {
    Convention noConflicts = du.getConvention("838012");

    int numTimeSlotsPerDay = noConflicts.getNumTimeSlotsPerDay();
    ScheduleCommand schedComm = new ScheduleCommand(noConflicts, 5, noConflicts.getNumDays(),
        numTimeSlotsPerDay, null);
    List<CalendarEvent> schedule = schedComm.execute();
    assertEquals(schedule.size(), 3);
    CalendarEvent event1 = schedule.get(0);
    CalendarEvent event2 = schedule.get(1);
    CalendarEvent event3 = schedule.get(2);
    assertTrue(event1.getStart().equals(event2.getStart()));
    assertTrue(event1.getStart().equals(event3.getStart()));
    assertTrue(event1.getEnd().equals(event2.getEnd()));
    assertTrue(event1.getEnd().equals(event3.getEnd()));
  }

  @Test
  public void noEventsTest() {
    Convention noEvents = du.getConvention("695715");
    int numTimeSlotsPerDay = noEvents.getNumTimeSlotsPerDay();
    ScheduleCommand schedComm = new ScheduleCommand(noEvents, 5, noEvents.getNumDays(),
        numTimeSlotsPerDay, null);
    List<CalendarEvent> schedule = schedComm.execute();
    assertEquals(schedule.size(), 0);
  }

  @Test
  public void notEnoughTimeTest() {
    Convention notEnough = du.getConvention("695715");
    int numTimeSlotsPerDay = notEnough.getNumTimeSlotsPerDay();
    ScheduleCommand schedComm = new ScheduleCommand(notEnough, 5, notEnough.getNumDays(),
        numTimeSlotsPerDay, null);
    List<CalendarEvent> schedule = schedComm.execute();
    assertEquals(schedule.size(), 0);
  }

  @Test
  public void tooManyConflictsTest() {
    Convention tooManyConflicts = du.getConvention("401570");
    int numTimeSlotsPerDay = tooManyConflicts.getNumTimeSlotsPerDay();
    ScheduleCommand schedComm = new ScheduleCommand(tooManyConflicts, 5,
        tooManyConflicts.getNumDays(), numTimeSlotsPerDay, null);
    boolean caught = false;
    try {
      List<CalendarEvent> schedule = schedComm.execute();
    } catch (NullPointerException err) {
      caught = true;
    }
    assertTrue(caught);
  }

  @Test
  public void conflictsTest() {
    Convention notEnough = du.getConvention("393142");
    int numTimeSlotsPerDay = notEnough.getNumTimeSlotsPerDay();
    ScheduleCommand schedComm = new ScheduleCommand(notEnough, 5, notEnough.getNumDays(),
        numTimeSlotsPerDay, null);
    List<CalendarEvent> schedule = schedComm.execute();
    assertEquals(schedule.size(), 3);
    CalendarEvent event1 = schedule.get(0);
    CalendarEvent event2 = schedule.get(1);
    CalendarEvent event3 = schedule.get(2);
    assertNotEquals(event1.getStart(), event2.getStart());
    assertNotEquals(event2.getStart(), event3.getStart());
    assertNotEquals(event1.getStart(), event3.getStart());
  }

  @Test
  public void duplicateConflictsTest() {
    // Multiple attendees indicated they wanted to go to event 1 and event 3, so that conflict has
    // weight 3. (I did this manually when creating the convention)
    Convention duplicatesConv = du.getConvention("176578");
    int numTimeSlotsPerDay = duplicatesConv.getNumTimeSlotsPerDay();
    ScheduleCommand schedComm = new ScheduleCommand(duplicatesConv, 5, duplicatesConv.getNumDays(),
        numTimeSlotsPerDay, null);
    List<CalendarEvent> schedule = schedComm.execute();
    assertEquals(schedule.size(), 3);
    CalendarEvent event1 = schedule.get(0);
    CalendarEvent event3 = schedule.get(2);
    assertNotEquals(event1.getStart(), event3.getStart());

  }

}
