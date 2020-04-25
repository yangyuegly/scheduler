package edu.brown.cs.student.scheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.brown.cs.student.graph.UndirectedWeightedGraph;

/**
 * This class is used to schedule the user's convention. It implements the ICommand interface.
 */
public class ScheduleCommand {
  List<Event> nodes;
  HashSet<Conflict> edges;
  Convention convention;
  Integer TS;
  Integer CONCURENCY_LIMIT, MAX_SCHEDULE_DAYS;
  UndirectedWeightedGraph<Event, Conflict> graph;

  /**
   * Constructor for scheduling events
   *
   * @param convention - convention to schedule
   * @param CONCURENCY_LIMIT - Limit for concurrency threads
   * @param MAX_SCHEDULE_DAYS - Convention duration in days
   * @param TS
   */
  public ScheduleCommand(Convention convention, Integer CONCURENCY_LIMIT, Integer MAX_SCHEDULE_DAYS,
      Integer TS) {
    // this.graph = graph;
    this.TS = TS;
    this.nodes = new ArrayList<>();
    this.edges = new HashSet<>();
    this.convention = convention;
    this.CONCURENCY_LIMIT = CONCURENCY_LIMIT;
    this.MAX_SCHEDULE_DAYS = MAX_SCHEDULE_DAYS;
  }

  /**
   * This method turns the given time slot into a LocalDateTime object representing the start date
   * and time of the slot.
   *
   * @param timeSlot - a List of Integers, where the first Integer represents the day of the
   *        convention that this time slot is located in, and the second Integer represents the
   *        place of this time slot in that day (ie. 1, if it is the first time slot that day, 2 if
   *        it is the second, etc)
   *
   * @return a LocalDateTime, which represents the start date and time of the given slot.
   */
  private LocalDateTime getTimeSlotStart(List<Integer> timeSlot) {
    LocalDateTime convStart = convention.getStartDateTime();
    int dayOfSlot = timeSlot.get(0);
    LocalDateTime slotDayAtStartTime = convStart.plusDays(dayOfSlot);
    int numMinutesBeforeSlot = timeSlot.get(1) * convention.getEventDuration();

    return slotDayAtStartTime.plusMinutes(numMinutesBeforeSlot);
  }

  /**
   * This method turns the scheduled graph into a String that contains events with times and dates
   * in the format needed for the FullCalendar API.
   *
   * @return a String, which represents the format describing events and their times for the
   *         calendar
   */
  private String makeScheduleString() {
    String scheduleString = "[";
    boolean firstEvent = true;

    for (Event currEvent : nodes) {
      String eventName = currEvent.getName();
      List<Integer> eventTimeSlot = currEvent.getColor();
      LocalDateTime eventStart = getTimeSlotStart(eventTimeSlot); // cache
                                                                  // this???????????????????????
      LocalDateTime eventEnd = eventStart.plusMinutes(convention.getEventDuration());

      String eventString = "{\"title\": \"" + eventName + "\", \"start\": \"" + eventStart
          + "\", \"end\": \"" + eventEnd + "\"}";

      if (!firstEvent) {
        scheduleString = scheduleString + ", ";
      } else {
        firstEvent = false;
      }

      scheduleString = scheduleString + eventString;
    }

    return scheduleString;
  }

  /**
   * Schedules the events
   */
  public String execute() {
    extractNodes();
    extractEdges();
    this.graph = new UndirectedWeightedGraph<Event, Conflict>(this.nodes, this.CONCURENCY_LIMIT,
        this.MAX_SCHEDULE_DAYS, this.TS);
    graph.addAllEdges(this.edges);
    graph.graphColoring(this.TS, this.CONCURENCY_LIMIT);

    return makeScheduleString();
  }

  /**
   * Getter for graph
   *
   * @return - graph
   */
  public UndirectedWeightedGraph<Event, Conflict> getGraph() {
    return this.graph;
  }

  /**
   * This method sets the nodes field to the List of Events in the Convention.
   */
  private void extractNodes() {
    this.nodes = convention.getEvents();
  }

  /**
   * This method sets the edges field to the List of Conflicts in the Convention.
   */
  private void extractEdges() {
    this.edges = this.convention.getConflicts();
  }

  @Override
  public String toString() {
    return "{" + " graph='" + getGraph() + "'" + "}";
  }

}
