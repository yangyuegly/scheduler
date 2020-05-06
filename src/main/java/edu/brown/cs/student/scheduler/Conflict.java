package edu.brown.cs.student.scheduler;

import java.util.Objects;

import edu.brown.cs.student.graph.IEdge;

/**
 * Class that represents a conflict Edge. It implements IEdge for the parameters Event and Conflict.
 */
public class Conflict implements IEdge<Event, Conflict> {

  /**
   * These are fields for this class.
   *
   * event1 - an Event, which represents the first conflicting event (conflicts are ordered)
   *
   * event2 - an Event, which represents the second conflicting event
   *
   * weight - an Integer, which represents the weight of the conflict
   */
  private Event event1;
  private Event event2;
  private Integer weight;

  /**
   * Constructor for Conflict.
   *
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
  public void setHead(Event head) {
    this.event1 = head;
  }

  @Override
  public Event getTail() {
    return this.event2;
  }

  @Override
  public void setTail(Event tail) {
    this.event2 = tail;
  }

  @Override
  public Integer getWeight() {
    return this.weight;
  }

  @Override
  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  /**
   * This method is used to increment the weight of the Conflict by 1.
   */
  public void incrementWeight() {
    this.weight++;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Conflict)) {
      return false;
    }

    Conflict conflict = (Conflict) o;
    return event1.equals(conflict.event1) && event2.equals(conflict.event2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event1, event2);
  }

  @Override
  public String toString() {
    return "{" + " event1='" + getHead() + "'" + ", event2='" + getTail() + "'" + ", weight='"
        + getWeight() + "'" + "}";
  }

}
