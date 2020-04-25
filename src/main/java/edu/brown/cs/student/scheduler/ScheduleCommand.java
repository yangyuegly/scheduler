package edu.brown.cs.student.scheduler;

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
   * Schedules the events
   */
  public String execute() {
    extractNodes();
    extractEdges();
    this.graph = new UndirectedWeightedGraph<Event, Conflict>(this.nodes, this.CONCURENCY_LIMIT,
        this.MAX_SCHEDULE_DAYS, this.TS);
    graph.addAllEdges(this.edges);
    graph.graphColoring(this.TS, this.CONCURENCY_LIMIT);

    // delete this
    for (Event event : nodes) {
      List<Integer> color = event.getColor();
      System.out.println("color is" + color.get(0) + " " + color.get(1));
    }

//    Map<List<Integer>, List<Event>> timeSlotToEventsMap = makeScheduleMap();
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

  /**
   * Set graph
   *
   * @param graph - the graph to set it to
   *
   * @return - this class
   */
//  public ScheduleCommand graph(UndirectedWeightedGraph<Event, Conflict> graph) {
//    this.graph = graph;
//    return this;
//  }

  /**
   * Set nodes
   *
   * @param nodes - the nodes to set it to
   *
   * @return - this class
   */
//  public ScheduleCommand nodes(List<Event> nodes) {
//    this.nodes = nodes;
//    return this;
//  }

  /**
   * Set edges
   *
   * @param edges - edges to set it to
   *
   * @return - this class
   */
//  public ScheduleCommand edges(HashSet<Conflict> edges) {
//    this.edges = edges;
//    return this;
//  }

  /**
   * This method turns the scheduled graph into a Map of time slots to Events.
   *
   * @return Map of Lists of Integers to Lists of Events, where the Lists of Integers represent time
   *         slots, and the Events represent events that are scheduled for that time slot.
   */
//  private Map<List<Integer>, List<Event>> makeScheduleMap() {
//    Map<List<Integer>, List<Event>> timeSlotToEventsMap = new HashMap<>();
//
//    for (Event currEvent : nodes) {
//      List<Integer> currTimeSlot = currEvent.getColor();
//      List<Event> eventsInSlot = timeSlotToEventsMap.get(currTimeSlot);
//
//      if (eventsInSlot == null) {
//        // this time slot is not yet in timeSlotToEventsMap
//        eventsInSlot = new ArrayList<>();
//        eventsInSlot.add(currEvent);
//        timeSlotToEventsMap.put(currTimeSlot, eventsInSlot);
//      } else {
//        eventsInSlot.add(currEvent);
//        timeSlotToEventsMap.replace(currTimeSlot, eventsInSlot);
//      }
//    }
//
//    return timeSlotToEventsMap;
//  }

  /**
   * This method turns the scheduled graph into a String that contains events with times and dates
   * in the format needed for the FullCalendar API.
   *
   * @return a String, which represents the format describing events and their times for the
   *         calendar
   */
  private String makeScheduleString() {
    // this format:
//    String scheduleString = "[{\"title\": \"Long Event\", \"start\": \"2020-04-12T10:30:00\", \"end\": \"2020-04-12T12:30:00\"}, "
//        + "{\"title\": \"Long Event\", \"start\": \"2020-04-12T10:30:00\", \"end\": \"2020-04-12T12:30:00\"}]";

//    String scheduleString = "events: ["
//    for ()
    return "";
  }

  @Override
  public String toString() {
    return "{" + " graph='" + getGraph() + "'" + "}";
  }

}
