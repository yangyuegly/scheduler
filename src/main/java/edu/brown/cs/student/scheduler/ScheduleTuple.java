package edu.brown.cs.student.scheduler;

import java.util.List;

/**
 * Class to represent the schedule tuple
 */
public class ScheduleTuple {
  private List<CalendarEvent> calEvents;
  private List<Event> events;

  /**
   * Constructor for schedule tuple
   *
   * @param calEvents - list of calendar events
   * @param events - list of events
   */
  public ScheduleTuple(List<CalendarEvent> calEvents, List<Event> events) {
    this.calEvents = calEvents;
    this.events = events;
  }

  /**
   * Constructor for ScheduleTuple
   */
  public ScheduleTuple() {

  }

  /**
   * Getter for calendar events
   *
   * @return - list of calendar event
   */
  public List<CalendarEvent> getCalEvents() {
    return calEvents;
  }

  /**
   * Getter for events
   *
   * @return - list of events
   */
  public List<Event> getEvents() {
    return events;
  }

}
