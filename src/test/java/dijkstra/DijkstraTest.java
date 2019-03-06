package dijkstra;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bacon.ActorVertex;
import bacon.BaconDbOp;
import bacon.FilmEdge;

/**
 * @author vx5
 *
 *         This class tests the Dijkstra object as well as, implicitly, the
 *         functionality of a class implementing the DijkstraDbOp interface.
 *         Testing of the getPath() method is not included here, and is
 *         implicitly handled through the system tests.
 */
public class DijkstraTest {
  // Stores instance of Dijkstra object to be used
  private Dijkstra<ActorVertex, FilmEdge> d;

  @Test
  public void testConstruction() {
    // Test for non-null construction
    assertNotNull(d);
  }

  @Before
  public void setUp() {
    // Instantiates instance field
    try {
      BaconDbOp op = new BaconDbOp();
      op.setSqlDb("data/bacon/smallBacon.sqlite3");
    } catch (ClassNotFoundException | SQLException e) {
      fail("Exception thrown");
    }
    d = new Dijkstra<ActorVertex, FilmEdge>(new BaconDbOp());
  }

  @After
  public void tearDown() {
    // Clears instance field
    d = null;
  }
}
