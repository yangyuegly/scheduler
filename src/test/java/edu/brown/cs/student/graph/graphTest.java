package edu.brown.cs.student.graph;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.student.graph.UndirectedWeightedGraph;
import edu.brown.cs.student.scheduler.Conflict;
import edu.brown.cs.student.scheduler.Event;

public class graphTest {
  List<Event> events;
  UndirectedWeightedGraph<Event, Conflict> graph;
  /**
   * Sets up the graph (with both large and small database)
   */
  @Before
  public void setUp() {

    events = new ArrayList<Event>();

    for (Integer i = 0; i < 10; i++) {
      events.add(new Event(i, i.toString()));
    }
    HashSet<Conflict> conflicts = new HashSet<Conflict>();
    conflicts.add(new Conflict(events.get(0), events.get(1), 3));
    conflicts.add(new Conflict(events.get(0), events.get(3), 5));
    conflicts.add(new Conflict(events.get(1), events.get(3), 6));
    conflicts.add(new Conflict(events.get(1), events.get(2), 1));
    conflicts.add(new Conflict(events.get(3), events.get(2), 1));
    int CL = 10;
    int md = 4;
    graph = new UndirectedWeightedGraph<>(events, CL ,
    md);
    graph.addAllEdges(conflicts);
}

  /**
   * Resets the graph
   */
  @After
  public void tearDown() {

  }
  @Test
  public void getNumVerticesTest() {
    setUp();
    assertEquals(10, graph.getNumVertices());
  }
  @Test
  public void getSmallestAvailableColorTest() {
  }
}