package edu.brown.cs.student.graph;

/**
 * Interface that represents edges in a graph.
 *
 * @param <V> - A vertex object
 * @param <E> - An edge object
 */
public interface IEdge<V, E> {

  /**
   * Gets the src vertex in a directed edge.
   *
   * @return the head of the edge as V
   */
  V getHead();

  /**
   * Sets the head of the edge.
   *
   * @param h the vertex that the head will be set to
   */
  void setHead(V h);

  /**
   * Sets the tail of the edge.
   *
   * @param t the vertex that the tail will be set to
   */
  void setTail(V t);

  /**
   * Set the weight of the edge.
   *
   * @param w the weight that the edge will be set to
   */
  void setWeight(Integer w);

  /**
   * Gets the weight of the edge.
   *
   * @return the weight of the edge as a integer
   */
  Integer getWeight();

  /**
   * Gets the tail of the edge which is the end node that a directed edge points to.
   *
   * @return the tail of the edge as V
   */
  V getTail();
}
