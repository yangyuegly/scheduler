package edu.brown.cs.student.scheduler;

import java.util.List;

public class ScheduleTuple {
  private List<CalendarEvent> calEvents;
  private List<Event> events;

  public ScheduleTuple(List<CalendarEvent> calEvents, List<Event> events) {
    this.calEvents = calEvents;
    this.events = events;
  }

  public ScheduleTuple() {

  }

  public List<CalendarEvent> getCalEvents() {
    return calEvents;
  }

  public List<Event> getEvents() {
    return events;
  }

}
