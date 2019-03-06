package bacon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BaconDbOpTest {
  // Stores instance of BaconDbOp used in tests
  private BaconDbOp o;

  /**
   * Jointly tests the setSqlDb() and hasDb() methods, as they rely on each
   * other.
   */
  @Test
  public void testSqlDbHasDb() {
    try {
      BaconDbOp altOp = new BaconDbOp();
      altOp.setSqlDb("data/bacon/smallBacon.sqlite3");
      assertTrue(altOp.hasDb());
    } catch (ClassNotFoundException | SQLException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testValidNeighbors() {
    ActorVertex a = new ActorVertex("Mark Ruffalo", "id", 0);
    ActorVertex b = new ActorVertex("Rupert Grint", "id", 0);
    ActorVertex c = new ActorVertex("Emma Watson", "id", 0);
    assertTrue(o.validNeighbors(a, b));
    assertFalse(o.validNeighbors(a, c));
  }

  @Test
  public void testGiveNeighbors() {
    // Creates new actor
    ActorVertex taylor = new ActorVertex("Taylor Swift", "/m/0dl567", 0);
    // Tests that it begins with no edges
    assertEquals(taylor.getEdges().size(), 0);
    // Adds edges
    try {
      o.giveNeighbors(taylor);
    } catch (Exception e) {
      fail("Exception thrown");
    }
    // Checks for correct number of edges
    assertEquals(taylor.getEdges().size(), 1);
    // Checks for correct edge
    FilmEdge valDay = taylor.getEdges().iterator().next();
    assertTrue(valDay.getName().contentEquals("Valentine's Day"));
    // Checks for correct opposite vertex
    ActorVertex shirley = valDay.getOtherNode(taylor);
    assertTrue(shirley.getName().contentEquals("Shirley McLaine"));
  }

  @Test
  public void testActorNameToId() {
    // Test for correct actor ID returned
    try {
      assertEquals(o.actorNameToId("Taylor Swift"), "/m/0dl567");
    } catch (SQLException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testActorIdToName() {
    // Test for correct actor name returned
    try {
      assertEquals(o.actorIdToName("/m/0dl567"), "Taylor Swift");
    } catch (SQLException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testActorIdToFilmIds() {
    // Obtains set of film IDs
    HashSet<String> filmIds = new HashSet<String>();
    try {
      filmIds = o.actorIdToFilmIds("/m/0dl567");
    } catch (SQLException e) {
      fail("Exception thrown");
    }
    // Test for exact film ID present
    assertEquals(filmIds.size(), 1);
    assertEquals(filmIds.iterator().next(), "/m/06_wqk4");
  }

  @Test
  public void testFilmIdToActorIds() {
    // Obtains set of actor IDs
    HashSet<String> actorIds = new HashSet<String>();
    try {
      actorIds = o.filmIdToActorIds("/m/06_wqk4");
    } catch (SQLException e) {
      fail("Exception thrown");
    }
    // Test for exact actor IDs present
    assertEquals(actorIds.size(), 2);
    assertTrue(actorIds.contains("/m/0dl567"));
    assertTrue(actorIds.contains("/m/0g4jm8q"));
  }

  @Test
  public void testFilmIdToName() {
    // Test for correct resulting film name
    try {
      assertEquals(o.filmIdToName("/m/06_wqk4"), "Valentine's Day");
    } catch (SQLException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testGetActors() {
    // Obtain actor names
    HashSet<String> actorNames = new HashSet<String>();
    try {
      actorNames = o.getActors();
    } catch (SQLException e) {
      fail("Exception thrown");
    }
    // Test that names are, in fact, included
    assertTrue(actorNames.contains("Taylor Swift"));
    // TEST
    Iterator<String> i = actorNames.iterator();
    while (i.hasNext()) {
      System.out.println(i.next());
    }
    // Test for correct number of names
    assertEquals(actorNames.size(), 32);
  }

  @Test
  public void testMakeVertex() {
    // Test for correct vertex returned
    ActorVertex taylor = o.makeVertex("/m/0dl567", 0);
    assertEquals(taylor.getName(), "Taylor Swift");
  }

  @Before
  public void setUp() {
    // Initializes new BaconDbOp() instance
    try {
      o = new BaconDbOp();
      o.setSqlDb("data/bacon/smallBacon.sqlite3");
    } catch (ClassNotFoundException | SQLException e) {
      fail("Exception thrown");
    }
  }

  @After
  public void tearDown() {
    // Clears instance field
    o = null;
  }
}
