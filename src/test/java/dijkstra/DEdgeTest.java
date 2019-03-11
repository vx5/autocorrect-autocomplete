package dijkstra;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bacon.ActorVertex;
import bacon.FilmEdge;

public class DEdgeTest {
  // Stores DEdge instance
  private DEdge<ActorVertex, FilmEdge> e;
  // Stores vertices on either side of the DEdge
  private ActorVertex a;
  private ActorVertex b;

  @Test
  public void testGetId() {
    // Tests for proper id
    assertEquals(e.getId(), "E");
  }

  @Test
  public void testGetWeight() {
    // Tests for proper weight
    assertEquals(e.getWeight(), 0, 0);
  }

  @Test
  public void testGetOtherNode() {
    // Tests for returning opposite nodes
    assertEquals(e.getOtherNode(a).getId(), "B");
    assertEquals(e.getOtherNode(b).getId(), "A");
  }

  @Before
  public void setUp() {
    // Initializes all ActorVertexes and DEdge
    a = new ActorVertex("NameA", "A", 0);
    b = new ActorVertex("NameB", "B", 0);
    e = new DEdge<ActorVertex, FilmEdge>("E", 0, a);
    e.setTail(b);
  }

  @After
  public void tearDown() {
    a = null;
    b = null;
    e = null;
  }
}
