package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import edu.brown.cs.student.graph.UndirectedWeightedGraph;
import edu.brown.cs.student.main.ICommand;
import edu.brown.cs.student.main.Main;

/**
 * This class is used to schedule the user's convention. It implements the
 * ICommand interface.
 */
public class ScheduleCommand implements ICommand {
  UndirectedWeightedGraph<Event, Conflict> graph = null;
  List<Event> nodes;
  Set<Conflict> edges;

  public ScheduleCommand(UndirectedWeightedGraph<Event,Conflict> graph) {
    this.graph = graph;
    this.nodes = new ArrayList<>();
    this.edges = new HashSet<>();
  }

  public UndirectedWeightedGraph<Event,Conflict> getGraph() {
    return this.graph;
  }

  public void setGraph(UndirectedWeightedGraph<Event,Conflict> graph) {
    this.graph = graph;
  }

  public List<Event> getNodes() {
    Main.getDatabase().getCollection("events");
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

  public ScheduleCommand graph(UndirectedWeightedGraph<Event,Conflict> graph) {
    this.graph = graph;
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
        return Objects.equals(graph, scheduleCommand.graph) && Objects.equals(nodes, scheduleCommand.nodes) && Objects.equals(edges, scheduleCommand.edges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(graph, nodes, edges);
  }

  @Override
  public String toString() {
    return "{" +
      " graph='" + getGraph() + "'" +
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
>>>>>>> da8c55e7a0af1bdde6dea9967f7ce3e1589095c8
