package edu.brown.cs.student.graph;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import edu.brown.cs.student.database.Database;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;


/**
 * The Graph class takes in a V that extends Vertex and E that extends
 * Edge. It gets information from a DataStore and runs a dynamic
 * dikstra's algorithm to find the shortest path between to nodes in the graph.
 * @param <V> an object that implements Vertex
 * @param <E> an object that implements Edge
 */
public class UndirectedWeightedGraph<V extends IVertex<V, E>, E extends IEdge<V, E>> {
  // edge table keeps track of weights
  private Database db;
  private int[][] weightMatrix;
  private int numColor; //the max num of color a user can take in
  private int numVertices;
  private int ts; //time slots 
  private int cl; //concurrency level 
  private HashMap<Integer, Integer> degree;
  private int[][] colors; //double indexed color
                            //first param: 

  /**
   * Constructor for the graph. It takes in a datastore and instantiates a hashset that
   * represents a graph.
   * @param db a datastore
   */
  public UndirectedWeightedGraph(Database db, int numVertices) {
    // two nodes may be connected iff 1) they were in the same movie 2) they share
    // initial
    this.numVertices = numVertices;
    this.weightMatrix = new int[numVertices][numVertices];
    //wij denotes the number of students in both i and j
        //k is the range of J given by the user
    this.db = db;
    this.degree = new HashMap<>();
    
  }

  /**
   * Add all edges to the graph
   * @param edges
   */
  public void addAllEdges(HashSet<E> edges) {

  }

  /**
   * Get edges between two nodes.
   *
   * @param a the vertex that you want to get the edges for
   * @return a list of incident edges
   */
  public Set<E> getAllEdges(V a) {
    return a.getIncidentEdges();
  }

 
  public void graphColoring(int ts, int cl) {

  }
}



