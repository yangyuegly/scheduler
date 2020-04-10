package edu.brown.cs.student.graph;
import java.util.Set;

/**
 * Interface that represents a vertex in the graph.
 * @param <V> a vertex
 * @param <E> an edge
 */
public interface IVertex<V, E> {



  /**
   * Gets the cost of the getting to the vertex.
   * @return the cost of the vertex
   */
  Double getCost();

  /**
   * Sets the cost of getting to the vertex.
   * @param cost the cost that we want to set the vertex to
   */
  void setCost(int cost);

  /**
   * Sets the used boolean to b.
   * @param b the value we want to set the used boolean to
   */
  void setUsed(boolean b);

  /**
   * Gets the value of the used boolean.
   * @return the used boolean
   */
  boolean getUsed();

  /**
   * Sets the previous edge that the vertex is connected to.
   * @param pathPrev the previous edge we want to set
   */
  void setPrev(E pathPrev);

  /**
   * Gets the previous edge of the vertex.
   * @return the previous edge as type E
   */
  E getPrev();

  /**
   * Gets the id of the vertex.
   * @return the id of the vertex as a integer
   */
  Integer getID();
}

