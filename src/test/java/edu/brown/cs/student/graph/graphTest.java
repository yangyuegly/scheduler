package edu.brown.cs.student.graph;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
      events.add(new Event(i, i.toString(), ""));
    }
    Set<Conflict> conflicts = new HashSet<Conflict>();
    conflicts.add(new Conflict(events.get(0), events.get(1), 3));
    conflicts.add(new Conflict(events.get(0), events.get(3), 5));
    conflicts.add(new Conflict(events.get(1), events.get(3), 6));
    conflicts.add(new Conflict(events.get(1), events.get(2), 1));
    conflicts.add(new Conflict(events.get(3), events.get(2), 1));

    conflicts.add(new Conflict(events.get(1), events.get(0), 3));
    conflicts.add(new Conflict(events.get(3), events.get(0), 5));
    conflicts.add(new Conflict(events.get(3), events.get(1), 6));
    conflicts.add(new Conflict(events.get(2), events.get(1), 1));
    conflicts.add(new Conflict(events.get(2), events.get(3), 1));
    int CL = 10;
    int md = 4;
    int TS = 5;
    graph = new UndirectedWeightedGraph<>(events, CL, md, TS);
    graph.addAllEdges(conflicts);
  }

  /**
   * Resets the graph
   */
  @After
  public void tearDown() {
    events = null;
    graph = null;
  }

  @Test
  public void getAdjListTest() {
    setUp();
    assertEquals(2, events.get(0).getAdjList().size());
    tearDown();
  }

  @Test
  public void getNumVerticesTest() {
    setUp();
    assertEquals(10, graph.getNumVertices());
    tearDown();
  }

  @Test
  public void getFirstNodeColorTest() {
    setUp();
    List<Integer> colors = graph.getFirstNodeColor();
    assertEquals(2, colors.size());
    tearDown();
  }

  @Test
  public void getSmallestAvailableColorTest() {
    setUp();
    List<Integer> colors = graph.getSmallestAvailableColor(0);
    assertEquals(2, colors.size());
    tearDown();
  }

  @Test
  public void graphColoringTest() {
    setUp();
    graph.graphColoring(5, 5);
    assertEquals(0, (int) events.get(0).getColor().get(0));
    assertEquals(0, (int) events.get(0).getColor().get(1));
    assertEquals(0, (int) events.get(1).getColor().get(0));
    assertEquals(1, (int) events.get(1).getColor().get(1));
    tearDown();
  }

  @Test
  public void checkConcurrencyLimit() {
    setUp();
    assertEquals(10, (int) (graph.getColors().get(0)[0]));
    tearDown();
  }

}
