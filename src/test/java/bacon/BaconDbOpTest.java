package bacon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

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

  }

  @Before
  public void setUp() {
    try {
      o = new BaconDbOp();
      o.setSqlDb("data/bacon/smallBacon.sqlite3");
    } catch (ClassNotFoundException | SQLException e) {
      fail("Exception thrown");
    }
  }

  @After
  public void tearDown() {
    o = null;
  }
}
