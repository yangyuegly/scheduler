package edu.brown.cs.student.scheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs.student.exception.SchedulingException;
import edu.brown.cs.student.graph.UndirectedWeightedGraph;

/**
 * This class is used to schedule the user's convention. It implements the ICommand interface.
 */
public class ScheduleCommand {

  /**
   * Fields denoting the set of nodes, edges, the convention to schedule, time slot, concurrency
   * limit and the graph
   */
  List<Event> nodes;
  Set<Conflict> edges;
  Convention convention;
  Integer TS;
  Integer CONCURENCY_LIMIT, MAX_SCHEDULE_DAYS;
  UndirectedWeightedGraph<Event, Conflict> graph;
  String correspondingID;
  Convention correspondingConvention;

  /**
   * Constructor for scheduling events
   *
   * @param convention - convention to schedule
   * @param CONCURENCY_LIMIT - Limit for concurrency threads
   * @param MAX_SCHEDULE_DAYS - Convention duration in days
   * @param TS
   */
  public ScheduleCommand(Convention convention, Integer CONCURENCY_LIMIT, Integer MAX_SCHEDULE_DAYS,
      Integer TS, String correspondingID) {
    // this.graph = graph;
    this.TS = TS;
    this.nodes = new ArrayList<>();
    this.edges = new HashSet<>();
    this.convention = convention;
    this.CONCURENCY_LIMIT = CONCURENCY_LIMIT;
    this.MAX_SCHEDULE_DAYS = MAX_SCHEDULE_DAYS;
    this.correspondingID = correspondingID;
    this.correspondingConvention = new Convention(correspondingID);
  }

  /**
   * Schedules the convention using the graph coloring algorithm.
   *
   * @return a List of CalendarEvents, which represent the scheduled Events in a format that is
   *         compatible with the FullCalendar API.
   *
   * @throws SchedulingException if there is no possible schedule
   */
  public List<CalendarEvent> execute() throws SchedulingException {
    System.out.println("in schedule command");

    extractNodes();
    Map<String, Integer> findNames = new HashMap<>();
    for (Event eve : this.nodes) {
      findNames.put(eve.getName(), eve.getID());
    }
    extractEdges();
    for (Conflict curr : this.edges) {
      if (curr.getTail().getID() == null || curr.getTail().getID() < 0) {
        // System.out.println("tail" + curr.getTail());
        System.out.println("find names:" + curr.getTail().getName());
        System.out.println("dict" + findNames.get(curr.getTail().getName()));
        curr.getTail().setId(findNames.get(curr.getTail().getName()));
        // System.out.println("modified" + curr.getTail());

      }
      if (curr.getHead().getID() == null || curr.getHead().getID() < 0) {
        System.out.println("head" + curr.getHead());

        curr.getHead().setId(findNames.get(curr.getHead().getName()));
      }
    }
    for (Conflict curr : this.edges) {
      System.out.println("conflict:" + curr);
    }
    this.graph = new UndirectedWeightedGraph<Event, Conflict>(this.nodes, this.CONCURENCY_LIMIT,
        this.MAX_SCHEDULE_DAYS, this.TS);
    graph.addAllEdges(this.edges);

    Set<Event> colored = graph.graphColoring(this.TS, this.CONCURENCY_LIMIT);

    List<CalendarEvent> calEvents = new ArrayList<>();
    List<Event> eventsToSort = new ArrayList<>();

    for (Event event : colored) {
      eventsToSort.add(event);
      LocalDateTime currStart = this.getTimeSlotStart(event.getColor());
      event.setStart(currStart);

      Integer eventDur = convention.getEventDuration();

      LocalDateTime currEnd = currStart.plusMinutes(eventDur);
      event.setEnd(currEnd);
    }
    System.out.println("is null?? " + (eventsToSort == null));

    System.out.println("sorted list length is " + eventsToSort.size());
    Collections.sort(eventsToSort, new CompareStartTime());

    for (Event event : eventsToSort) {
      // System.out.println("start time" + event.getStart());
      CalendarEvent newEvent = new CalendarEvent(event.getName(), event.getStart().toString(),
          event.getEnd().toString());
      calEvents.add(newEvent);

    }

    return calEvents;
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

    if (correspondingID == null) {
      this.nodes = this.convention.getEvents();
    } else {
      System.out.println("corressponding convention");
      this.nodes = correspondingConvention.getEvents();
    }

  }

  /**
   * This method sets the edges field to the List of Conflicts in the Convention.
   */
  private void extractEdges() {

    if (correspondingID == null) {
      this.edges = this.convention.getConflicts();
    } else {
      this.edges = correspondingConvention.getConflicts();
    }
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

  @Override
  public String toString() {
    return "{" + " graph='" + getGraph() + "'" + "}";
  }

}
