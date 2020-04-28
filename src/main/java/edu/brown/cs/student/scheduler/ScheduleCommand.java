package edu.brown.cs.student.scheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.student.graph.UndirectedWeightedGraph;

/**
 * This class is used to schedule the user's convention. It implements the ICommand interface.
 */
public class ScheduleCommand {
  List<Event> nodes;
  Set<Conflict> edges;
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

    System.out.println("got start date time"); // delete

    int dayOfSlot = timeSlot.get(0);
    LocalDateTime slotDayAtStartTime = convStart.plusDays(dayOfSlot);

    System.out.println("got made slot start"); // delete

    int numMinutesBeforeSlot = timeSlot.get(1) * convention.getEventDuration();

    System.out.println("got num minutes before slot"); // delete

    System.out.println("time slot " + dayOfSlot + " " + numMinutesBeforeSlot); // delete

    return slotDayAtStartTime.plusMinutes(numMinutesBeforeSlot);
  }

  /**
   * Schedules the events
   */
  public List<CalendarEvent> execute() {
    extractNodes();
    extractEdges();

    this.graph = new UndirectedWeightedGraph<Event, Conflict>(this.nodes, this.CONCURENCY_LIMIT,
        this.MAX_SCHEDULE_DAYS, this.TS);
    graph.addAllEdges(this.edges);

    System.out.println(this.edges);

    graph.graphColoring(this.TS, this.CONCURENCY_LIMIT);

    List<CalendarEvent> calEvents = new ArrayList<>();

    for (Event event : nodes) {
      System.out.println("in sched loop event " + event.getName()); // delete

      LocalDateTime currStart = this.getTimeSlotStart(event.getColor());

      System.out.println("got start"); // delete

      Integer eventDur = convention.getEventDuration();

      System.out.println("got duration"); // delete

      String currEnd = currStart.plusMinutes(eventDur).toString();

      System.out.println("got end"); // delete

      CalendarEvent newEvent = new CalendarEvent(event.getName(), currStart.toString(), currEnd);
      calEvents.add(newEvent);

      System.out.println("made calendar event"); // delete
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
    this.nodes = convention.getEvents();
    System.out.println("number of nodes: " + nodes.size()); // delete
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
