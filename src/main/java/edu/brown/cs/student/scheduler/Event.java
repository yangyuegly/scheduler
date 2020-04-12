package edu.brown.cs.student.scheduler;

import java.util.List;

import edu.brown.cs.student.graph.IVertex;

public class Event implements IVertex<Event> {
  Integer id;
  List<Event> adjList;
  Integer degree; //?
  
  /**
   * Constructor for the Event class.
   * @param myID
   */
  public Event(Integer myID) {
    this.id = myID;
  }
  
  
  
  @Override
  public Integer getID() {
    return id;
  }

  @Override
  public List<Event> getAdjList() {
    return adjList;
  }

  @Override
  public Integer getDegree() {
    return degree;
  }
  
 
  
  

}

