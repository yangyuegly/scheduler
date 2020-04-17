package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.brown.cs.student.graph.UndirectedWeightedGraph;
import edu.brown.cs.student.main.ICommand;

/**
 * This class is used to schedule the user's convention. It implements the
 * ICommand interface.
 */
public class ScheduleCommand implements ICommand {
  UndirectedWeightedGraph<Event, Conflict> g = null;
  List<Event> nodes = new ArrayList<>();
  Set<Conflict> edges = new HashSet<>();
  
  @Override
  public String getKeyword() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String execute(String input) {
    // TODO Auto-generated method stub
    return null;
  }

}
