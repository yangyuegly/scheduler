package edu.brown.cs.student.graph;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.jetty.http.PathMap.MappedEntry;

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
public class UndirectedWeightedGraph<V extends IVertex<V>, E extends IEdge<V>> {
  // edge table keeps track of weights
  private Database db;
  private int[][] weightMatrix;
  private int numColor; //the max num of color a user can take in
  private int numVertices;//the number of mini-events
  private int TS; //time slots 
  private int exameBreak = 0; 


  private final int CONCURENCY_LIMIT; 
  private SortedSet<Map.Entry<Integer, Integer>> degree;
  private Map<Integer, ArrayList<Integer>> result;
  private ArrayList<Integer[]> colors = new ArrayList<Integer[]>(); //double indexed color
                        //the length of array: time slots (provided by registrar)
                      //arrayList index: day of the exam (to be minimized)
                      //value: current concurrency limit
  private int k; //the range of num of time slots, concurency level
  private final int MAX_SCHEDULE_DAYS;
  private SortedSet nodesByDegrees;

  /**
   * Constructor for the graph. It takes in a datastore and instantiates a hashset that
   * represents a graph.
   * @param db a datastore
   */
  public UndirectedWeightedGraph(Database db, int numVertices, int CONCURENCY_LIMIT, int MAX_SCHEDULE_DAYS) {
    // two nodes may be connected iff 1) they were in the same movie 2) they share
    // initial 
    this.numVertices = numVertices;

    this.CONCURENCY_LIMIT = CONCURENCY_LIMIT;
    this.result = new HashMap<>();
    this.MAX_SCHEDULE_DAYS = MAX_SCHEDULE_DAYS;
    //initialize graph
    this.weightMatrix = new int[numVertices][numVertices];
    for (int i = 0; i < numVertices; i++) {
      for (int j = 0; j < numVertices; j++) {
        this.weightMatrix[i][j] = 0;
      }
    }

    for (int j = 0; j < MAX_SCHEDULE_DAYS; j++) {
      //initializing the color array
      Integer[] ts = new Integer[TS];
      for (int i = 0; i < TS; i++) {
        ts[i] = CONCURENCY_LIMIT;
      }
      colors.add(ts);
    }


    
  
    //wij denotes the number of students in both i and j
    //k is the range of J given by the user
    this.db = db;
    this.degree = new TreeSet<Map.Entry<Integer, Integer>>();
    this.result = new HashMap<Integer, ArrayList<Integer>>();
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
  


  public void graphColoring(int ts, int cl) {
    int numColoredCourses = 0;
    while (numColoredCourses < numVertices) {
      //for the first course
      if (numColoredCourses == 0) {
        ArrayList<Integer> indices = getFirstNodeColor();
        result.put(sortedEntries.get(0),indices);
      } else {
        ArrayList<Integer> indices = getSmallestAvailableColor();
      }
    }
  }


  /**
   * Return an arraylist of two integers representing color
   * 0: the day
   * 1: the time slot
   * @param courseID - the course to be colored
   * @return the color assigned or null
   */
  public ArrayList<Integer> getFirstNodeColor() {
    ArrayList<Integer> result = new ArrayList<Integer>();
    for (int i = 0; i < MAX_SCHEDULE_DAYS; i++) {
      for (int j = 0; j < TS; j++) {
        if (colors.get(i)[j] > 0) {
          colors.get(i)[j]--;
          result.add(i);
          result.add(j);
          break;
        }
      }
    }
    if (result.size() == 0)
      throw new NullPointerException("Unable to find first color");

    return result;
  }

  /**
   * Get the adjacency list of the courses
   * @param courseID
   * @return
   */
  public List<Integer> getAdjList(Integer courseID) {
    List<Integer> result = new ArrayList<Integer>();
  
    for (int i = 0; i < numVertices; i++) {
      if (weightMatrix[courseID][i] != 0) {
        result.add(weightMatrix[courseID][i]);
      }
    }
    return result; 
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


  /**
   * Get the smallest available color
   * @param courseID
   * @return
   */
  public List<Integer> getSmallestAvailableColor(int courseID) {

    boolean valid = false;
    List<Integer> adj = getAdjList(courseID);
    for (int i = 0; i < numVertices; i++) {
      for (int j = 0; j < TS; j++) {
        List<Integer> currColor = new ArrayList<>(List.of(i, j));
        valid = true;
        for (int r = 0; r < adj.size(); r++) {
          //get the color of the adjacent node
          List<Integer> color = (result.get(adj.get(r)));
          if (color != null) {
            //check if that color is the same as the current
            if (color.get(0) != i || color.get(1) != j) {
              if (calculateExternalDistance(color, currColor) == 0) {
                // if we don't want back-to-back exams
                if (calculateInternalDistance(color, currColor) <= this.exameBreak) {
                  valid = false;
                }
              }
              //if the color has been used up to its cl
              else if (colors.get(i)[j] <= 0) {
                valid = false;
              }
            }
          }
        }
        if (valid) return currColor;
      }
    }
    return null; 
  }
  
/**
 * Calculate the internal distance between the color
 * i.e. how many time slots apart 
 * @param a
 * @param b
 * @return
 */
public Integer calculateInternalDistance(List<Integer> a, List<Integer> b) {
  assert (a.size() == 2 && b.size() == 2);
  return (Math.abs(a.get(1) - b.get(1)));
}
  

/**
 * Calculate the external distance between the color
 * i.e. how many time days apart 
 * @param a
 * @param b
 * @return
 */
public Integer calculateExternalDistance(List<Integer> a, List<Integer> b) {
  assert  (a.size()  == 2 && b.size() == 2);
  return (Math.abs(a.get(0) - b.get(0))); 
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

 

  /**
   * calculate all nodes' degrees
   */
  public void setDegree() {
    HashMap<Integer, Integer> idToDegree = new HashMap<Integer, Integer>();

    for (int i = 0; i < numVertices; i++) {
      idToDegree.put(i, findDegrees(i));
    }
    degree = entriesSortedByValues(idToDegree);
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

  public int getExameBreak() {
    return this.exameBreak;
  }

  public void setExameBreak(int exameBreak) {
    this.exameBreak = exameBreak;
  }

  public int getCONCURENCY_LIMIT() {
    return this.CONCURENCY_LIMIT;
  }


  public SortedSet<Map.Entry<Integer,Integer>> getDegree() {
    return this.degree;
  }


  public Map<Integer,ArrayList<Integer>> getResult() {
    return this.result;
  }

  public void setResult(Map<Integer,ArrayList<Integer>> result) {
    this.result = result;
  }

  public ArrayList<Integer[]> getColors() {
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
                if (res == 0) {
                      int res = e1.getValue().getDegree();
            }
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
      ", ts='" + "'" +
      ", degree='" + "'" +
      ", colors='" + getColors() + "'" +
      ", k='" + getK() + "'" +
      "}";
  }
  

}




