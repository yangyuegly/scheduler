package edu.brown.cs.student.graph;
import java.util.List;
import java.util.Set;

/**
 * Interface that represents a vertex in the graph.
 * @param <V> a vertex
 * @param <E> an edge
 */
public interface IVertex<V> {

  /**
   * Gets the id of the vertex.
   * @return the id of the vertex as a integer
   */
  Integer getID();

  List<V> getAdjList(); //should the return edges?
  
  
  Integer getDegree();
}

