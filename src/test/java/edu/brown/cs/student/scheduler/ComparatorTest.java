package edu.brown.cs.student.scheduler;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

public class ComparatorTest {

  @Test
  public void testCompare() {
    CompareStartTime comparator = new CompareStartTime();
    Event firstEvent = new Event(0, "click clack song writing", "songs about the book");
    LocalDateTime firstTime = LocalDateTime.of(2020, 3, 20, 3, 15);
    firstEvent.setStart(firstTime);

    Event secondEvent = new Event(1, "click clack moo song singing", "sing the songs we wrote");
    LocalDateTime secondTime = LocalDateTime.of(2020, 3, 20, 4, 15);
    secondEvent.setStart(secondTime);

    Event equalEvent = new Event(0, "click clack moo dance", "dance to the click clack moo songs");
    equalEvent.setStart(secondTime);

    assertEquals(comparator.compare(firstEvent, secondEvent), -1);
    assertEquals(comparator.compare(equalEvent, secondEvent), 0);
    assertEquals(comparator.compare(secondEvent, firstEvent), 1);

  }

}
