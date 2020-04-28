package edu.brown.cs.student.scheduler;

import java.util.Objects;

import edu.brown.cs.student.graph.IEdge;

/**
 * Class that represents a conflict Edge
 */
public class Conflict implements IEdge<Event, Conflict> {

  //fields of the class representing the pairwise conflict of events and weight
  Event event1;
  Event event2;
  Integer weight;

  /**
   * Constructor for Conflict
   * @param event1 - event in conflict
   * @param event2 - event in conflict
   * @param weight - weight of this edge
   */
  public Conflict(Event event1, Event event2, Integer weight) {
    this.event1 = event1;
    this.event2 = event2;
    this.weight = weight;
  }

  @Override
  public Event getHead() {
    return this.event1;
  }

  @Override
  public void setHead(Event event1) {
    this.event1 = event1;
  }

  @Override
  public Event getTail() {
    return this.event2;
  }

  @Override
  public void setTail(Event event2) {
    this.event2 = event2;
  }

  @Override
  public Integer getWeight() {
    return this.weight;
  }

  @Override
  public void setWeight(Integer weight) {
    this.weight = weight;
  }



  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Conflict)) {
            return false;
        }
        Conflict conflict = (Conflict) o;
        return Objects.equals(event1, conflict.event1) && Objects.equals(event2, conflict.event2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event1, event2);
  }

  @Override
  public String toString() {
    return "{" +
      " event1='" + getHead() + "'" +
      ", event2='" + getTail() + "'" +
      ", weight='" + getWeight() + "'" +
      "}";
  }

}