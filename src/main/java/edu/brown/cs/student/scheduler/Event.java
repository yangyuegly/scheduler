package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.brown.cs.student.graph.IVertex;

public class Event implements IVertex<Event, Conflict> {
  Integer id;
  List<Conflict> adjList;
  Integer degree; //
  List<Integer> color;
  String name;
  String description;

  public Event() {
  }

  public Event(Integer id, String name) {
    this.id = id;
    this.adjList = new ArrayList<>();
    this.degree = 0;
    this.color = new ArrayList<>();
    this.name = name;
    this.description = "";
  }
  public Event(String name) {
    this.adjList = new ArrayList<>();
    this.degree = 0;
    this.color = new ArrayList<>();
    this.name = name;
    this.description = "";
  }

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
    * Get the heaviest weight among v's adjacency list
    * to aid sorting
    * @return
    */

  @Override
  public Integer getHeaviestWeight() {
    int max = 0;
    for (Conflict i : this.getAdjList()) {
      max = i.getWeight() > max ? i.getWeight() : max;
    }
    return max;
  }

  public Integer getId() {
    return this.id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
  public void setAdjList(List<Conflict> adjList) {
    this.adjList = adjList;
  }
  public void setDegree(Integer degree) {
    this.degree = degree;
  }

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
  return "{" +
    " id='" + getId() + "'" +
    ", degree='" + getDegree() + "'" +
    ", color='" + getColor() + "'" +
    "}";
}

}

