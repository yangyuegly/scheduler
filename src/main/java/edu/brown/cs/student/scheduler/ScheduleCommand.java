package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;

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

  public ScheduleCommand() {
  }

  public ScheduleCommand(UndirectedWeightedGraph<Event,Conflict> g, List<Event> nodes, Set<Conflict> edges) {
    this.g = g;
    this.nodes = nodes;
    this.edges = edges;
  }

  public UndirectedWeightedGraph<Event,Conflict> getG() {
    return this.g;
  }

  public void setG(UndirectedWeightedGraph<Event,Conflict> g) {
    this.g = g;
  }

  public List<Event> getNodes() {
    return this.nodes;
  }

  public void setNodes(List<Event> nodes) {
    this.nodes = nodes;
  }

  public Set<Conflict> getEdges() {
    return this.edges;
  }

  public void setEdges(Set<Conflict> edges) {
    this.edges = edges;
  }

  public ScheduleCommand g(UndirectedWeightedGraph<Event,Conflict> g) {
    this.g = g;
    return this;
  }

  public ScheduleCommand nodes(List<Event> nodes) {
    this.nodes = nodes;
    return this;
  }

  public ScheduleCommand edges(Set<Conflict> edges) {
    this.edges = edges;
    return this;
  }

  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ScheduleCommand)) {
            return false;
        }
        ScheduleCommand scheduleCommand = (ScheduleCommand) o;
        return Objects.equals(g, scheduleCommand.g) && Objects.equals(nodes, scheduleCommand.nodes) && Objects.equals(edges, scheduleCommand.edges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(g, nodes, edges);
  }

  @Override
  public String toString() {
    return "{" +
      " g='" + getG() + "'" +
      ", nodes='" + getNodes() + "'" +
      ", edges='" + getEdges() + "'" +
      "}";
  }
  
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
