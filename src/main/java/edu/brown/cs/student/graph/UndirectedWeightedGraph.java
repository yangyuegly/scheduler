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

import edu.brown.cs.student.exception.SchedulingException;

//source: https://www.researchgate.net/publication/220413840_A_New_Exam_Scheduling_Algorithm_Using_Graph_Coloring
/**
 * The Graph class takes in a V that extends Vertex and E that extends Edge. It gets information
 * from a DataStore and runs a dynamic dikstra's algorithm to find the shortest path between to
 * nodes in the graph.
 *
 * @param V - a class that implements Vertex
 * @param E - a class that implements Edge
 */
public class UndirectedWeightedGraph<V extends IVertex<V, E>, E extends IEdge<V, E>> {

  /**
   * These are fields for this class.
   *
   * weightMatrix - a array of int arrays, which keeps track of weights
   *
   * numColor - an int, which represents the max num of color a user can take in
   *
   * numVertices - an int, which represents the number of vertices in this graph
   *
   * TS - an int, which represents the max number of time slots in a day
   *
   * examBreak - an int, which represents the amount of time wanted for a break
   *
   * nodes - a Map of Integers to V, which maps node Id to vertex object
   *
   * CONCURRENCY_LIMIT - an int, which represents the max number of events that can happen at the
   * same time
   *
   * degree - a SortedSet
   *
   * result - a Set of V
   *
   * colors - a List of Integer arrays, which represent double indexed colors; the length of the
   * inner arrays is the number of time slots in a day; the arrayList index is the day of the exam
   * (to be minimized); value is the current concurrency limit
   *
   * k - an int, which represents the range of num of time slots, concurency level
   *
   * MAX_SCHEDULE_DAYS - an int, which represents the max number of days over which this graph can
   * be scheduled
   *
   * coloredMap - a Map of Integers to V
   */
  private int[][] weightMatrix;
  private int numColor;
  private int numVertices;
  private int TS;
  private int examBreak = 0;
  private Map<Integer, V> nodes;
  private final int CONCURENCY_LIMIT;
  private SortedSet<Map.Entry<Integer, V>> degree;
  private Set<V> result;
  private List<Integer[]> colors = new ArrayList<Integer[]>();
  private int k;
  private final int MAX_SCHEDULE_DAYS;
  private Map<Integer, V> coloredMap = new HashMap<>();

  /**
   * Constructor for the graph.
   *
   * @param vertices - a List of objects of type V, which represents the vertices of this graph
   * @param CONCURENCY_LIMIT - an int, which represents the max number of events that can happen at
   *        the same time
   * @param MAX_SCHEDULE_DAYS - an int, which represents the max number of days over which this
   *        graph can be scheduled
   * @param TS - - an int, which represents the max number of time slots in a day
   */
  public UndirectedWeightedGraph(List<V> vertices, int CONCURENCY_LIMIT, int MAX_SCHEDULE_DAYS,
      int TS) {
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
   * Add all edges to the graph.
   *
   * @param edges - a Set of objects of type E, which represent the edges of this graph
   */
  public void addAllEdges(Set<E> edges) {
    System.out.println("in addAllEdges");

    for (E e : edges) {
      this.weightMatrix[e.getHead().getID()][e.getTail().getID()] = e.getWeight();
      this.weightMatrix[e.getTail().getID()][e.getHead().getID()] = e.getWeight();
      System.out.println(nodes.get(e.getHead().getID()) + " this");
      nodes.get(e.getHead().getID()).addToAdjList(e);
    }
    for (Map.Entry<Integer, V> n : nodes.entrySet()) {
      System.out.println(n.getValue().getID() + " " + n.getValue().getAdjList());
    }
    setDegree();
  }

  /**
   * Find the degree of the given vertex.
   *
   * @param id - an int, which represents the ID of a node
   *
   * @return an int, which represents the size of the node's adjacency list
   */
  public int findDegrees(int id) {
    return nodes.get(id).getAdjList().size();
  }

  /**
   * This method colors the graph using the graph coloring algorithm.
   *
   * @param ts - an int, which represents the max number of time slots in a day
   * @param cl - an int, which represents the max number of events that can happen at the same time
   *
   * @return a Set of vertices, which represent the colored nodes
   *
   * @throws SchedulingException if a schedule is not possible
   */
  public Set<V> graphColoring(int ts, int cl) throws SchedulingException {
    Set<V> coloredSet = new HashSet<V>();
    System.out.println("here");
    int numColoredCourses = 0;
    Iterator<Map.Entry<Integer, V>> iter = degree.iterator();
    while (iter.hasNext()) {
      V curr = iter.next().getValue();
      // if the current node is not colored
      System.out.println(curr);
      if (!result.contains(curr)) {
        // for the first course
        if (numColoredCourses == 0) {
          List<Integer> indices = getFirstNodeColor();
          curr.setColor(indices);
          coloredMap.put(curr.getID(), curr);
          System.out.println("first node:" + curr);
          // coloredSet.add(curr);
          result.add(curr);
          numColoredCourses++;
        } else {
          // color the current course
          List<Integer> indices = getSmallestAvailableColor(curr.getID());
          if (indices.size() == 2) {
            curr.setColor(indices);
            coloredMap.put(curr.getID(), curr);
            result.add(curr);
            // coloredSet.add(curr);
            numColoredCourses++;
            // decrement the concurrency limit for a color
            colors.get(indices.get(0))[indices.get(1)]--;
          } else {
            System.out.println("stuck");
            throw new SchedulingException(
                "Unable to find a conflict-free schedule for this convention.");
          }
        }
        for (E e : curr.getAdjList()) {
          V i = e.getTail();
          // check if an adjacent node is colored
          if (!result.contains(i)) {
            List<Integer> adjColors = getSmallestAvailableColor(i.getID());
            if (adjColors.size() == 2) {
              i.setColor(adjColors);
              coloredMap.put(i.getID(), i);
              result.add(i);
              // coloredSet.add(i);
              numColoredCourses++;
              colors.get(adjColors.get(0))[adjColors.get(1)]--;
            }
          }
        }
      }
    }
    for (Map.Entry<Integer, V> c : coloredMap.entrySet()) {
      System.out.println(c.getValue());
      coloredSet.add(c.getValue());
    }
    return coloredSet;
  }

  /**
   * Return a list of two integers representing color 0: the day 1: the time slot.
   *
   * @return a List of Integers, which represents the color assigned or null
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
    return null;
  }

  /**
   * define the weight of a color to be W (RIJ) = (I-1)*k + J; k is the range of J. A color RIJ is
   * said to be smaller than color RGH if the weight W (RIJ) is smaller than W (RGH).
   *
   * @param i - an int
   * @param j - an int
   *
   * @return an int, which represents the weight of a color
   */
  public int calculateColorWeight(int i, int j) {
    return (i - 1) * k + j;
  }

  /**
   * Get the smallest available color.
   *
   * @param courseID - an int, which represents the id of a node
   *
   * @return a List of Integers, which represents the smallest available color
   */
  public List<Integer> getSmallestAvailableColor(int courseID) {

    // System.out.println("the current course id " + courseID);
    boolean valid = false;
    List<E> adj = nodes.get(courseID).getAdjList();
    for (int i = 0; i < this.MAX_SCHEDULE_DAYS; i++) {
      for (int j = 0; j < this.TS; j++) {
        List<Integer> currColor = new ArrayList<>(List.of(i, j));
        valid = true;
        for (int r = 0; r < adj.size(); r++) {
          // System.out.println(nodes.get((adj.get(r).getTail().getID())));
          // get the color of the adjacent node
          List<Integer> color;
          if (coloredMap.containsKey(adj.get(r).getTail().getID())) {
            // System.out.println("colors:" + color);
            // check if that color is the same as the current
            color = coloredMap.get(adj.get(r).getTail().getID()).getColor();
            if (color.get(0) != i || color.get(1) != j) {
              // System.out.println("i:" + i + " j " + j);
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
          // System.out.println("outside of loop: " + currColor);
          return currColor;
        }
      }
    }
    return null;
    // throw new NullPointerException("Unable to find a schedue for the current convention");
  }

  /**
   * Calculate the internal distance between the color i.e. how many time slots apart.
   *
   * @param a - a List of Integers, which represents a time slot
   * @param b - a List of Integers, which represents a time slot
   *
   * @return an Integer, which represents the internal distance between colors a and b (how many
   *         time slots apart they are)
   */
  public Integer calculateInternalDistance(List<Integer> a, List<Integer> b) {
    assert (a.size() == 2 && b.size() == 2);
    return (Math.abs(a.get(1) - b.get(1)));
  }

  /**
   * Calculate the external distance between the color i.e. how many time days apart.
   *
   * @param a - a List of Integers, which represents a time slot
   * @param b - a List of Integers, which represents a time slot
   *
   * @return n Integer, which represents the external distance between colors a and b (how many days
   *         apart they are)
   */
  public Integer calculateExternalDistance(List<Integer> a, List<Integer> b) {
    assert (a.size() == 2 && b.size() == 2);
    return (Math.abs(a.get(0) - b.get(0)));
  }

  public int getNumVertices() {
    return this.numVertices;
  }

  /**
   * Calculate all nodes' degrees.
   */
  public void setDegree() {
    degree = entriesSortedByValues(nodes);
  }

  /**
   * This method gets the colors field.
   *
   * @return a List of Integer arrays, which represent double indexed colors
   */
  public List<Integer[]> getColors() {
    return this.colors;
  }

  /**
   * Util method that sorts the map based on values.
   * https://stackoverflow.com/questions/2864840/treemap-sort-by-value
   *
   * @param map - a Map of objects of type K to objects of type T, which represents a map
   *
   * @return SortedSet of map entries, which is sorted based on values
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
            res *= -1;
            // compare by heaviest weight if has the same degree
            if (res == 0) {
              res = e1.getValue().getHeaviestWeight() < e2.getValue().getHeaviestWeight() ? 1 : -1;
              if (res == 0) {
                res = e1.getValue().getID() < e2.getValue().getID() ? 1 : -1;
              }
              return res;
            } else {
              return res;
            }
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
    return "{" + "'" + ", weightMatrix='" + weightMatrix + "'" + ", numColor='" + numColor + "'"
        + ", numVertices='" + numVertices + "'" + ", ts='" + "'" + ", degree='" + "'" + ", colors='"
        + getColors() + "'" + ", k='" + k + "'" + "}";
  }

}
