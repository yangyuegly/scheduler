package edu.brown.cs.student.scheduler;

import java.util.Objects;

public class Conflict {
    Integer event1id;
    Integer event2id;
    Integer weight; 

  public Conflict(Integer event1id, Integer event2id, Integer weight) {
    this.event1id = event1id;
    this.event2id = event2id;
    this.weight = weight;
  }

  public Conflict() {
  }

  public Conflict(Integer event1id, Integer event2id) {
    this.event1id = event1id;
    this.event2id = event2id;
  }

  public Integer getWeight() {
    return this.weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public Conflict weight(Integer weight) {
    this.weight = weight;
    return this;
  }

  public Integer getEvent1id() {
    return this.event1id;
  }

  public void setEvent1id(Integer event1id) {
    this.event1id = event1id;
  }

  public Integer getEvent2id() {
    return this.event2id;
  }

  public void setEvent2id(Integer event2id) {
    this.event2id = event2id;
  }

  public Conflict event1id(Integer event1id) {
    this.event1id = event1id;
    return this;
  }

  public Conflict event2id(Integer event2id) {
    this.event2id = event2id;
    return this;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Conflict)) {
            return false;
        }
        Conflict conflict = (Conflict) o;
        return Objects.equals(event1id, conflict.event1id) && Objects.equals(event2id, conflict.event2id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(event1id, event2id);
  }

  @Override
  public String toString() {
    return "{" +
      " event1id='" + getEvent1id() + "'" +
      ", event2id='" + getEvent2id() + "'" +
      "}";
  }


}