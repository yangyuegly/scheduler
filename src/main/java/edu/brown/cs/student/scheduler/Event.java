package edu.brown.cs.student.scheduler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.brown.cs.student.graph.IVertex;

/**
 * Represents an Event which is the vertex of the graph. It implements IVertex for the parameters
 * Event and Conflict.
 */
public class Event implements IVertex<Event, Conflict> {

  /**
   * Id of event, list of edges, degree of vertex, color of vertex name and description of vertex.
   */
  private Integer id;
  private List<Conflict> adjList;
  private Integer degree; //
  private List<Integer> color;
  private String name;
  private String description;
  private LocalDateTime start;
  private LocalDateTime end;

  /**
   * Constructor of an event.
   *
   * @param id - id of event
   * @param name - name of event
   * @param description - description of event
   */
  public Event(Integer id, String name, String description) {
    this.id = id;
    this.adjList = new ArrayList<Conflict>();
    this.degree = 0;
    this.color = new ArrayList<>();
    this.name = name;
    this.description = description;
  }

  /**
   * This method is used to get this event's description.
   *
   * @return a String, which represents the description for this Event
   */
  public String getDescription() {
    return description;
  }

  @Override
  public Integer getHeaviestWeight() {
    int max = 0;
    for (Conflict i : this.getAdjList()) {
      max = i.getWeight() > max ? i.getWeight() : max;
    }
    return max;
  }

  /**
   * Setter method for id.
   *
   * @param id - id of the event to set it to
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Getter for name.
   *
   * @return - name of event
   */
  public String getName() {
    return name;
  }

  @Override
  public Integer getID() {
    return id;
  }

  @Override
  public List<Conflict> getAdjList() {
    return new ArrayList<>(adjList);
  }

  /**
   * return size of adjacent list.
   */
  @Override
  public Integer getDegree() {
    return adjList.size();
  }

  @Override
  public void addToAdjList(Conflict adj) {
    this.adjList.add(adj);
  }

  @Override
  public void setColor(List<Integer> c) {
    this.color = c;
  }

  @Override
  public List<Integer> getColor() {
    return new ArrayList<>(this.color);
  }

  /**
   * Method to set the start time of an event after it has been found by the graph.
   *
   * @return -- the start time of the event.
   */
  public LocalDateTime getStart() {
    return this.start;
  }

  /**
   * Method to set the start time of an event after it has been found by the graph.
   *
   * @param start -- the start time of the event.
   */
  public void setStart(LocalDateTime start) {
    this.start = start;
  }

  /**
   * Method to set the end time of an event after it has been found by the graph.
   *
   * @return -- the end time of the event.
   */
  public LocalDateTime getEnd() {
    return this.end;
  }

  /**
   * Method to set the end time of an event after it has been found by the graph.
   *
   * @param end -- the end time of the event.
   */
  public void setEnd(LocalDateTime end) {
    this.end = end;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Event)) {
      return false;
    }

    Event event = (Event) o;
    return Objects.equals(id, event.id) && Objects.equals(name, event.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    return "{" + " id='" + getID() + " name=" + getName() + "'" + ", degree='" + getDegree() + "'"
        + ", color='" + getColor() + "'" + "}";
  }

}
