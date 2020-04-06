package edu.brown.cs.student.graph;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.brown.cs.student.database.Database;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

//source: https://www.researchgate.net/publication/220413840_A_New_Exam_Scheduling_Algorithm_Using_Graph_Coloring
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
  private int numVertices;//the number of mini-events
  private int ts; //time slots 
  private HashMap<Integer, Integer> degree;
  private int[][] colors; //double indexed color
                          //first param: day of the exam (to be minimized)
                          //second param: time slots (provided by registrar)
  private int k; //the range of num of time slots, concurency level


  /**
   * Constructor for the graph. It takes in a datastore and instantiates a hashset that
   * represents a graph.
   * @param db a datastore
   */
  public UndirectedWeightedGraph(Database db, int numVertices) {
    // two nodes may be connected iff 1) they were in the same movie 2) they share
    // initial 
    this.numVertices = numVertices;

    //initialize graph
    this.weightMatrix = new int[numVertices][numVertices];
    for (int i = 0; i < numVertices; i++) {
      for (int j = 0; j < numVertices; j++) {
        this.weightMatrix[i][j] = 0; 
      }
    }
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
    for (E e : edges) {
      weightMatrix[e.getHead().getID()][e.getTail().getID()] = e.getWeight();
    }
  }

  
/**
 * Find the degree of vertices 
 * @param id
 * @return
 */
  public int findDegrees(int id) {
    int d = 0;
    for (int w : weightMatrix[id]) {
      if (w > 0)
        d++;
    }
    return d; 
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

  /**
   * define the  weight  of  a  color to be W (RIJ) =  (I-1)*k + J; 
   * k  is the range  of  J. 
   * A color  RIJ is said  to  be smaller than color RGH if 
   * the weight W (RIJ) is  smaller than W (RGH).
   * @param i
   * @param j
   * @return
   */
  public int calculateColorWeight(int i, int j) {
    return (i - 1) * k + j;
  }

  public UndirectedWeightedGraph() {
  }

  public UndirectedWeightedGraph(Database db, int[][] weightMatrix, int numColor, int numVertices, int ts, HashMap<Integer,Integer> degree, int[][] colors, int k) {
    this.db = db;
    this.weightMatrix = weightMatrix;
    this.numColor = numColor;
    this.numVertices = numVertices;
    this.ts = ts;
    this.degree = degree;
    this.colors = colors;
    this.k = k;
  }

  public Database getDb() {
    return this.db;
  }

  public void setDb(Database db) {
    this.db = db;
  }

  public int[][] getWeightMatrix() {
    return this.weightMatrix;
  }

  public void setWeightMatrix(int[][] weightMatrix) {
    this.weightMatrix = weightMatrix;
  }

  public int getNumColor() {
    return this.numColor;
  }

  public void setNumColor(int numColor) {
    this.numColor = numColor;
  }

  public int getNumVertices() {
    return this.numVertices;
  }

  public void setNumVertices(int numVertices) {
    this.numVertices = numVertices;
  }

  public int getTs() {
    return this.ts;
  }

  public void setTs(int ts) {
    this.ts = ts;
  }

  public HashMap<Integer,Integer> getDegree() {
    return this.degree;
  }

  /**
   * calculate all nodes' degrees
   */
  public void setDegree() {
    for (int i = 0; i < numVertices; i++) {
      degree.put(i, findDegrees(i));
    }
    entriesSortedByValues(degree);
  }

  public int[][] getColors() {
    return this.colors;
  }

  public void setColors(int[][] colors) {
    this.colors = colors;
  }

  public int getK() {
    return this.k;
  }

  public void setK(int k) {
    this.k = k;
  }

  /**
   * Util method that sorts the map based on values
   * https://stackoverflow.com/questions/2864840/treemap-sort-by-value
   * @param Map<K,V>
   * @return SortedSet<>
   */
  private static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
    SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(
        new Comparator<Map.Entry<K, V>>() {
          @Override
          public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
            int res = e1.getValue().compareTo(e2.getValue());
            return res != 0 ? res : 1;
          }
        });
    sortedEntries.addAll(map.entrySet());
    return sortedEntries;
  }


  @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UndirectedWeightedGraph)) {
            return false;
        }
        UndirectedWeightedGraph undirectedWeightedGraph = (UndirectedWeightedGraph) o;
        return Objects.equals(db, undirectedWeightedGraph.db) && Objects.equals(weightMatrix, undirectedWeightedGraph.weightMatrix) && numColor == undirectedWeightedGraph.numColor && numVertices == undirectedWeightedGraph.numVertices && ts == undirectedWeightedGraph.ts && Objects.equals(degree, undirectedWeightedGraph.degree) && Objects.equals(colors, undirectedWeightedGraph.colors) && k == undirectedWeightedGraph.k;
  }

  @Override
  public int hashCode() {
    return Objects.hash(db, weightMatrix, numColor, numVertices, ts, degree, colors, k);
  }

  @Override
  public String toString() {
    return "{" +
      " db='" + getDb() + "'" +
      ", weightMatrix='" + getWeightMatrix() + "'" +
      ", numColor='" + getNumColor() + "'" +
      ", numVertices='" + getNumVertices() + "'" +
      ", ts='" + getTs() + "'" +
      ", degree='" + getDegree() + "'" +
      ", colors='" + getColors() + "'" +
      ", k='" + getK() + "'" +
      "}";
  }
  

}




