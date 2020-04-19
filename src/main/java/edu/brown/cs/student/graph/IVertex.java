package edu.brown.cs.student.graph;
import java.util.List;
import java.util.Set;

/**
 * Interface that represents a vertex in the graph.
 * @param <V> a vertex
 * @param <E> an edge
 */
public interface IVertex<V, E>{

  /**
   * Gets the id of the vertex.
   * @return the id of the vertex as a integer
   */
  Integer getID();

  List<E> getAdjList(); //should the return edges?

  void addToAdjList(E adj);
  
  Integer getDegree();

  Integer getHeaviestWeight();
  void setColor(List<Integer> c);

  List<Integer> getColor();
}

