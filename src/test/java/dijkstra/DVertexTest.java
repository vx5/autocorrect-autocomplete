package dijkstra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bacon.ActorVertex;
import bacon.FilmEdge;

public class DVertexTest {
  // Stores instance of DVertex for testing
  private DVertex<ActorVertex, FilmEdge> a;

  @Test
  public void testConstruction() {
    // Check for non-null construction
    DVertex<ActorVertex, FilmEdge> v = new DVertex<ActorVertex, FilmEdge>("V",
        0);
    assertNotNull(v);
  }

  @Test
  public void testGetId() {
    // Test for basic repsonse
    assertEquals(a.getId(), "A");
  }

  /**
   * Tests accessors and mutators relevant to distance
   */
  @Test
  public void testDistAccMut() {
    float delta = 0;
    assertEquals(a.getDist(), 0, delta);
    a.setDist(1);
    assertEquals(a.getDist(), 1, delta);
  }

  /**
   * Tests accessors and mutators relevant to edges
   */
  @Test
  public void testEdgeAccMut() {
    // Instantiates new edge
    FilmEdge e1 = new FilmEdge("NameE1", "E1", 1,
        new ActorVertex("nameA", "A", 0));
    // Set in standard and best-path roles
    a.addEdge(e1);
    a.setPrevEdge(e1);
    // Test for presence in standard and best-path roles
    assertEquals(a.getPrevEdge(), e1);
    assertEquals(a.getEdges().size(), 1);
    assertEquals(a.getEdges().iterator().next(), e1);
  }

  @Test
  public void testCompareTo() {
    // Instantiates new vertices for comparison
    DVertex<ActorVertex, FilmEdge> b = new DVertex<ActorVertex, FilmEdge>("B",
        -1);
    DVertex<ActorVertex, FilmEdge> c = new DVertex<ActorVertex, FilmEdge>("C",
        1);
    // Verifies appropriate comparisons
    assertEquals(a.compareTo(b), 1);
    assertEquals(a.compareTo(a), 0);
    assertEquals(a.compareTo(c), -1);
  }

  @Before
  public void setUp() {
    // Populates instance field
    a = new DVertex<ActorVertex, FilmEdge>("A", 0);
  }

  @After
  public void tearDown() {
    // Clears instance field
    a = null;
  }
}
