package edu.brown.cs.student.graph;

//import edu.brown.cs.student.database.Database;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

//source: https://www.researchgate.net/publication/220413840_A_New_Exam_Scheduling_Algorithm_Using_Graph_Coloring
/**
 * The Graph class takes in a V that extends Vertex and E that extends Edge. It gets information
 * from a DataStore and runs a dynamic dikstra's algorithm to find the shortest path between to
 * nodes in the graph.
 *
 * @param <V> an object that implements Vertex
 * @param <E> an object that implements Edge
 */
public class UndirectedWeightedGraph<V extends IVertex<V, E>, E extends IEdge<V, E>> {
  // edge table keeps track of weights
  private int[][] weightMatrix;
  private int numColor; // the max num of color a user can take in
  private int numVertices;// the number of mini-events
  private int TS; // time slots
  private int examBreak = 0;
  private Map<Integer, V> nodes; // map node Id to vertex object

  private final int CONCURENCY_LIMIT;
  private SortedSet<Map.Entry<Integer, V>> degree; // a sorted set
  private HashSet<V> result;
  private List<Integer[]> colors = new ArrayList<Integer[]>(); // double indexed color
  // the length of array: time slots (provided by registrar)
  // arrayList index: day of the exam (to be minimized)
  // value: current concurrency limit
  private int k; // the range of num of time slots, concurency level
  private final int MAX_SCHEDULE_DAYS;

  /**
   * Constructor for the graph. It takes in a list of vertices
   *
   * @param vertices
   * @param CONCURENCY_LIMIT
   * @param MAX_SCHEDULE_DAYS
   */
  public UndirectedWeightedGraph(List<V> vertices, int CONCURENCY_LIMIT, int MAX_SCHEDULE_DAYS,
      int TS) {
    // two nodes may be connected iff 1) they were in the same movie 2) they share
    // initial
    this.numVertices = vertices.size();
    this.CONCURENCY_LIMIT = CONCURENCY_LIMIT;
    this.TS = TS;
    this.result = new HashSet<>();
    this.MAX_SCHEDULE_DAYS = MAX_SCHEDULE_DAYS;
    this.nodes = new HashMap<Integer, V>();
    for (V i : vertices) {
      nodes.put(i.getID(), i);
    }
    // initialize graph
    this.weightMatrix = new int[numVertices][numVertices];
    for (int i = 0; i < numVertices; i++) {
      for (int j = 0; j < numVertices; j++) {
        this.weightMatrix[i][j] = 0;
      }
    }

    for (int j = 0; j < MAX_SCHEDULE_DAYS; j++) {
      // initializing the color array
      Integer[] ts = new Integer[TS];
      for (int i = 0; i < TS; i++) {
        ts[i] = CONCURENCY_LIMIT;
      }
      colors.add(ts);
    }

    // wij denotes the number of students in both i and j
    // k is the range of J given by the user
    // this.db = db;
    this.degree = new TreeSet<Map.Entry<Integer, V>>();
    this.result = new HashSet<V>();
  }

  /**
   * Add all edges to the graph
   *
   * @param edges
   */
  public void addAllEdges(Set<E> edges) {
    System.out.println("numVertices: " + numVertices); // delete

    for (E e : edges) {
      System.out.println("in loop, edge is " + e.getHead().getID() + " to " + e.getTail().getID()); // delete
      System.out.println("weight: " + e.getWeight());
      this.weightMatrix[0][1] = 100;
      System.out.println("changed matrix value");

      this.weightMatrix[e.getHead().getID()][e.getTail().getID()] = e.getWeight();


      this.weightMatrix[e.getTail().getID()][e.getHead().getID()] = e.getWeight();


      nodes.get(e.getHead().getID()).addToAdjList(e);
    }


    setDegree();
  }

  /**
   * Find the degree of vertices
   *
   * @param id
   *
   * @return the size of the node's adjacency list
   */
  public int findDegrees(int id) {
    return nodes.get(id).getAdjList().size();
  }

  /**
   *
   * @param ts
   * @param cl
   */
  public void graphColoring(int ts, int cl) {
    int numColoredCourses = 0;
    Iterator<Map.Entry<Integer, V>> iter = degree.iterator();
    while (iter.hasNext()) {
      V curr = iter.next().getValue();
      // if the current node is not colored
      if (!result.contains(curr)) {
        // for the first course
        if (numColoredCourses == 0) {
          List<Integer> indices = getFirstNodeColor();
          curr.setColor(indices);
          result.add(curr);
          numColoredCourses++;
        } else {
          // color the current course
          List<Integer> indices = getSmallestAvailableColor(curr.getID());
          if (indices.size() == 2) {
            System.out.println("indices:" + indices);
            curr.setColor(indices);
            result.add(curr);
            numColoredCourses++;
            // decrement the concurrency limit for a color
            colors.get(indices.get(0))[indices.get(1)]--;
          }
          for (E e : curr.getAdjList()) {
            V i = e.getTail();
            // check if an adjacent node is colored
            if (!result.contains(i)) {
              List<Integer> adjColors = getSmallestAvailableColor(i.getID());
              if (adjColors.size() == 2) {
                i.setColor(adjColors);
                System.out.println("adjacent:" + i);
                result.add(i);
                numColoredCourses++;
                colors.get(adjColors.get(0))[adjColors.get(1)]--;
              }
            }
          }

        }
      }
    }
  }

  /**
   * Return an arraylist of two integers representing color 0: the day 1: the time slot
   *
   * @param courseID - the course to be colored
   *
   * @return the color assigned or null
   */
  public List<Integer> getFirstNodeColor() {
    List<Integer> result = new ArrayList<Integer>();
    for (int i = 0; i < MAX_SCHEDULE_DAYS; i++) {
      for (int j = 0; j < TS; j++) {
        if (colors.get(i)[j] > 0) {
          colors.get(i)[j]--;
          result.add(i);
          result.add(j);
          return result;
        }
      }
    }
    throw new NullPointerException("Unable to find first color");

  }

  /**
   * define the weight of a color to be W (RIJ) = (I-1)*k + J; k is the range of J. A color RIJ is
   * said to be smaller than color RGH if the weight W (RIJ) is smaller than W (RGH).
   *
   * @param i
   * @param j
   *
   * @return
   */
  public int calculateColorWeight(int i, int j) {
    return (i - 1) * k + j;
  }

  /**
   * Get the smallest available color
   *
   * @param courseID
   *
   * @return
   */
  public List<Integer> getSmallestAvailableColor(int courseID) {
    
    boolean valid = false;
    List<E> adj = nodes.get(courseID).getAdjList();
    for (int i = 0; i < this.MAX_SCHEDULE_DAYS; i++) {
      for (int j = 0; j < this.TS; j++) {
        List<Integer> currColor = new ArrayList<>(List.of(i, j));
        valid = true;
        for (int r = 0; r < adj.size(); r++) {
          // get the color of the adjacent node
          List<Integer> color = (adj.get(r).getTail().getColor());
          if (color != null && !color.isEmpty()) {
            System.out.println("colors:" + color);
            // check if that color is the same as the current
            if (color.get(0) != i || color.get(1) != j) {
              System.out.println("i:" + i + " j " + j);
              if (calculateExternalDistance(color, currColor) == 0) {
                // if we don't want back-to-back exams
                if (calculateInternalDistance(color, currColor) <= this.examBreak) {
                  valid = false;
                }
              }
              // if the color has been used up to its cl
              else if (colors.get(i)[j] <= 0) {
                valid = false;
              }
            } else {
              valid = false;
            }
          }
        }
        if (valid) {
          System.out.println("outside of loop: " + currColor);
          return currColor;
        }
      }
    }
    return null;
  }

  /**
   * Calculate the internal distance between the color i.e. how many time slots apart
   *
   * @param a
   * @param b
   *
   * @return
   */
  public Integer calculateInternalDistance(List<Integer> a, List<Integer> b) {
    assert (a.size() == 2 && b.size() == 2);
    return (Math.abs(a.get(1) - b.get(1)));
  }

  /**
   * Calculate the external distance between the color i.e. how many time days apart
   *
   * @param a
   * @param b
   *
   * @return
   */
  public Integer calculateExternalDistance(List<Integer> a, List<Integer> b) {
    assert (a.size() == 2 && b.size() == 2);
    return (Math.abs(a.get(0) - b.get(0)));
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

  public Map<Integer, V> getNodes() {
    return this.nodes;
  }

  /**
   * calculate all nodes' degrees
   */
  public void setDegree() {

    degree = entriesSortedByValues(nodes);
  }

  public int getK() {
    return this.k;
  }

  public int getTS() {
    return this.TS;
  }

  public void setTS(int TS) {
    this.TS = TS;
  }

  public int getExamBreak() {
    return this.examBreak;
  }

  public void setExameBreak(int examBreak) {
    this.examBreak = examBreak;
  }

  public int getCONCURENCY_LIMIT() {
    return this.CONCURENCY_LIMIT;
  }

  public SortedSet<Map.Entry<Integer, V>> getDegree() {
    return this.degree;
  }

  public List<Integer[]> getColors() {
    return this.colors;
  }

  public void setColors(ArrayList<Integer[]> colors) {
    this.colors = colors;
  }

  public int getMAX_SCHEDULE_DAYS() {
    return this.MAX_SCHEDULE_DAYS;
  }

  public void setK(int k) {
    this.k = k;
  }

  /**
   * Util method that sorts the map based on values
   * https://stackoverflow.com/questions/2864840/treemap-sort-by-value
   *
   * @param Map<K,V>
   *
   * @return SortedSet<>
   */
  private <K, T extends IVertex<T, E>> SortedSet<Map.Entry<K, T>> entriesSortedByValues(
      Map<K, T> map) {
    SortedSet<Map.Entry<K, T>> sortedEntries = new TreeSet<Map.Entry<K, T>>(
        // sort the nodes in the weight matrix in a descending
        // order based on the degree of nodes.
        new Comparator<Map.Entry<K, T>>() {
          @Override
          public int compare(Map.Entry<K, T> e1, Map.Entry<K, T> e2) {
            // compare by degree of nodes
            Integer res = e1.getValue().getDegree().compareTo(e2.getValue().getDegree());
            // compare by heaviest weight if has the same degree
            if (res == 0) {
              res = e1.getValue().getHeaviestWeight() < e2.getValue().getHeaviestWeight() ? -1 : 1;
            }
            return res != 0 ? res : 1;
          }
        });
    sortedEntries.addAll(map.entrySet());
    return sortedEntries;
  }

  // @Override
  // public boolean equals(Object o) {
  // if (o == this)
  // return true;
  // if (!(o instanceof UndirectedWeightedGraph)) {
  // return false;
  // }
  // UndirectedWeightedGraph<V,E> undirectedWeightedGraph = (UndirectedWeightedGraph<V,E>) o;
  // return Objects.equals(weightMatrix, undirectedWeightedGraph.weightMatrix) && numColor ==
  // undirectedWeightedGraph.numColor && numVertices == undirectedWeightedGraph.numVertices && TS ==
  // undirectedWeightedGraph.TS && Objects.equals(degree, undirectedWeightedGraph.degree) &&
  // Objects.equals(colors, undirectedWeightedGraph.colors) && k == undirectedWeightedGraph.k;
  // }

  // @Override
  // public int hashCode() {
  // return Objects.hash(weightMatrix, numColor, numVertices, TS, degree, colors, k);
  // }

  @Override
  public String toString() {
    return "{" + "'" + ", weightMatrix='" + getWeightMatrix() + "'" + ", numColor='" + getNumColor()
        + "'" + ", numVertices='" + getNumVertices() + "'" + ", ts='" + "'" + ", degree='" + "'"
        + ", colors='" + getColors() + "'" + ", k='" + getK() + "'" + "}";
  }

}
