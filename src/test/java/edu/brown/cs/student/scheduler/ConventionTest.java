package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;

/**
 * This class is used to test the methods in the Convention class.
 */
public class ConventionTest {

//  @Test
  /**
   * This method is used to test the getNumTimeSlots method in the Convention class.
   */
  public void testGetNumTimeSlots() {
    Convention conv = new Convention("1", "conv", "2020-04-10", 3, 90, "07:30", "19:30");
    assertEquals(conv.getNumTimeSlotsPerDay(), 8);

    Convention conv2 = new Convention("2", "conv", "2020-04-10", 3, 90, "07:30", "19:00");
    assertEquals(conv2.getNumTimeSlotsPerDay(), 7);

    Convention conv3 = new Convention("3", "conv", "2022-11-30", 2, 30, "08:45", "12:30");
    assertEquals(conv3.getNumTimeSlotsPerDay(), 7);

    Convention conv4 = new Convention("4", "conv", "2022-11-30", 1, 180, "08:45", "10:30");
    assertEquals(conv4.getNumTimeSlotsPerDay(), 0);
  }

}