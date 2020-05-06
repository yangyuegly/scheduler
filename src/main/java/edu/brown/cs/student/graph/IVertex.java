package edu.brown.cs.student.graph;

import java.util.List;

/**
 * Interface that represents a vertex in the graph.
 *
 * @param <V> a vertex
 * @param <E> an edge
 */
public interface IVertex<V, E> {

  /**
   * Gets the id of the vertex.
   *
   * @return the id of the vertex as a integer
   */
  Integer getID();

  /**
   * This method returns the edges that are connected to this vertex.
   *
   * @return a List of objects of type E that are connected to this vertex.
   */
  List<E> getAdjList();

  /**
   * This method adds an edge to this vertex.
   *
   * @param adj - an object of type E, which represents the edge to add
   */
  void addToAdjList(E adj);

  /**
   * This method gets the degree of this vertex.
   *
   * @return an Integer, which represents the degree of this vertex
   */
  Integer getDegree();

  /**
   * Get the heaviest weight among this vertex's adjacency list to aid sorting.
   *
   * @return - heaviest weight
   */
  Integer getHeaviestWeight();

  /**
   * This method sets the color of this vertex.
   *
   * @param color - a List of Integers, which represents the color of this vertex
   */
  void setColor(List<Integer> color);

  /**
   * This method gets the color of this vertex.
   *
   * @return a List of Integers, which represents the color of this vertex
   */
  List<Integer> getColor();
}
