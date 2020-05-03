package edu.brown.cs.student.scheduler;

import java.util.Comparator;

/**
 * Comparator to compare start time
 */
public class CompareStartTime implements Comparator<Event> {

  @Override
  public int compare(Event e1, Event e2) {
    boolean before = e1.getStart().isBefore(e2.getStart());
    boolean equal = e1.getStart().isEqual(e2.getStart());
    if (equal) {
      return 0;
    } else if (before) {
      return -1;
    } else {
      return 1;
    }
  }
}
