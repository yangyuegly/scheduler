package edu.brown.cs.student.scheduler;

import java.util.List;

/**
 * Class to represent the schedule tuple. This is used so the CalendarHandler has both
 * CalendarEvents (which are compatible with the FullCalendar API) and Events, which are used for
 * sending emails.
 */
public class ScheduleTuple {

  /**
   * These are fields for this class.
   *
   * calEvents - a List of CalendarEvents, which represent the events in this schedule in a form
   * that is compatible with the FullCalendar API
   *
   * events - a List of Events, which represent the events in this schedule in a form that is
   * compatible with the java code
   */
  private List<CalendarEvent> calEvents;
  private List<Event> events;

  /**
   * Constructor for schedule tuple.
   *
   * @param calEvents - list of calendar events
   * @param events - list of events
   */
  public ScheduleTuple(List<CalendarEvent> calEvents, List<Event> events) {
    this.calEvents = calEvents;
    this.events = events;
  }

  /**
   * Getter for calendar events.
   *
   * @return - list of calendar event
   */
  public List<CalendarEvent> getCalEvents() {
    return calEvents;
  }

  /**
   * Getter for events.
   *
   * @return - list of events
   */
  public List<Event> getEvents() {
    return events;
  }

}
