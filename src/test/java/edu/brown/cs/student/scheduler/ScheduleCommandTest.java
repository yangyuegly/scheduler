package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScheduleCommandTest {
  DatabaseUtility du;
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
  @Test
  public void testExecute() {
    setUp();
    assertEquals("Brown University Final Exams", myConv.getName());
    System.out.println(myConv);
    List<Event> events = du.getEventsFromConventionID("616997");
    assertEquals(101,events.size());
    ScheduleCommand sched = new ScheduleCommand(myConv, 30, 10, 5, "616997");
    sched.setNodes(events);
    assertEquals(101, sched.nodes.size());
    List<CalendarEvent> result = sched.execute();
    assertEquals(101, result.size());
    tearDown();
  }


}