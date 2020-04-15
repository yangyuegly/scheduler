package edu.brown.cs.student.scheduler;

import java.util.List;

import edu.brown.cs.student.graph.IVertex;

public class Event implements IVertex<Event, Conflict> {
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



  @Override
  public void addToAdjList(Conflict adj) {
    // TODO Auto-generated method stub
    
  }



  @Override
  public void setColor(List<Integer> c) {
    // TODO Auto-generated method stub
    
  }



  @Override
  public List<Integer> getColor() {
    // TODO Auto-generated method stub
    return null;
  }
  

}

