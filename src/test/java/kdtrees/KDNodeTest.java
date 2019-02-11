package kdtrees;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.brown.cs.vnaraya2.stars.Star;

public class KDNodeTest {
  private KDNode n;
  private Star s;

  @Test
  public void testConstruction() {
    KDNode node = new KDNode(s, 0);
    // Tests construction
    assertNotNull(node);
  }

  @Test
  public void testGetDimension() {
    // Tests getDimension() method
    assertEquals(n.getDimension(), 0);
  }

  /**
   * This method tests the setChild() and getChild() methods in tandem, since
   * the setChild() method's success is best verified by the getChild() method's
   * output.
   */
  @Test
  public void testSetGetChild() {
    // Constructs two new nodes
    KDNode belowChild = new KDNode(s, 1);
    KDNode aboveChild = new KDNode(s, 1);
    // Sets those two nodes as children of n
    n.setChild(belowChild, Constants.BELOW_CHILD);
    n.setChild(aboveChild, Constants.ABOVE_CHILD);
    // Checks that getChild() shows them as the children
    assertEquals(n.getChild(Constants.BELOW_CHILD), belowChild);
    assertEquals(n.getChild(Constants.ABOVE_CHILD), aboveChild);
  }

  @Test
  public void testGetNodeObject() {
    // Checks that n's node object is, in fact, s
    assertEquals(n.getNodeObject(), s);
  }

  @Before
  public void setUp() {
    float[] coords = {
        (float) 2.0, (float) 2.0, (float) 2.0
    };
    // Constructs new Star
    s = new Star("StarID", "StarName", coords);
    // Feeds that Star to constructor for new KDNode
    n = new KDNode(s, 0);
  }

  @After
  public void tearDown() {
    // Clears the variables
    s = null;
    n = null;
  }

}
