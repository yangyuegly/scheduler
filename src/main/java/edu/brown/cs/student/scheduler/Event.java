package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.brown.cs.student.graph.IVertex;

/**
 * Represents an Event which is the vertex of the graph
 */
public class Event implements IVertex<Event, Conflict> {

  /**
   * Id of event, list of edges, degree of vertex, color of vertex name and description of vertex
   */
  Integer id;
  List<Conflict> adjList;
  Integer degree; //
  List<Integer> color;
  String name;
  String description;

  /**
   * Constructor for Event
   */
  public Event() {
  }

  /**
   * Constructor for Event
   *
   * @param id - id of event
   * @param name - name of event
   */
  public Event(Integer id, String name) {
    this.id = id;
    this.adjList = new ArrayList<>();
    this.degree = 0;
    this.color = new ArrayList<>();
    this.name = name;
    this.description = "";
  }

  /**
   * Constructor of Event
   *
   * @param name - name of the event
   */
  public Event(String name) {
    this.adjList = new ArrayList<>();
    this.degree = 0;
    this.color = new ArrayList<>();
    this.name = name;
    this.description = "";
  }

  /**
   * Constructor of event
   *
   * @param id - id of event
   * @param name - name of event
   * @param description - description of event
   */
  public Event(Integer id, String name, String description) {
    this.id = id;
    this.adjList = new ArrayList<>();
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

  /**
   * Get the heaviest weight among v's adjacency list to aid sorting
   *
   * @return - heaviest weight
   */

  @Override
  public Integer getHeaviestWeight() {
    int max = 0;
    for (Conflict i : this.getAdjList()) {
      max = i.getWeight() > max ? i.getWeight() : max;
    }
    return max;
  }

  

  /**
   * Setter method for id
   *
   * @param id - id of the event to set it to
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Setter for adjList
   *
   * @param adjList - val to set it to
   */
  public void setAdjList(List<Conflict> adjList) {
    this.adjList = adjList;
  }

  /**
   * Setter for degree
   *
   * @param degree - degree of the vertex to set it to
   */
  public void setDegree(Integer degree) {
    this.degree = degree;
  }

  /**
   * Getter for name
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
    return adjList;
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
    adjList.add(adj);
  }

  @Override
  public void setColor(List<Integer> c) {
    this.color = c;
  }

  @Override
  public List<Integer> getColor() {
    return this.color;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
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
    return "{" + " id='" + getID() + "'" + ", degree='" + getDegree() + "'" + ", color='"
        + getColor() + "'" + "}";
  }

}
