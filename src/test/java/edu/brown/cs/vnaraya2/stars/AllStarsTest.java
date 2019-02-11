package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import filereader.CSVReader;

public class AllStarsTest {
  private AllStars a;
  private ArrayList<String[]> dataTenStar;

  @Test
  public void testConstruction() {
    AllStars b = new AllStars();
    // Tests that instance is not null
    assertNotNull(b);
  }

  /**
   * Tests the addStars(), getStars(), and makeStar() methods of the AllStars
   * class
   *
   * @throws StarsLoadingException if test does not run correctly
   * @throws IOException           if test does not run correctly
   */
  @Test
  public void testAddGetMakeStars() throws StarsLoadingException, IOException {
    a.addStars(dataTenStar);
    Star[] starList = a.getStars();
    // Test that Star was made correctly
    assertEquals(starList[0].getID(), dataTenStar.get(1)[0]);
    assertEquals(starList.length, 10);
  }

  @Test(expected = StarsLoadingException.class)
  public void testAddStarsOneRow() throws Exception {
    // Attempts to build Stars from .csv file with only one line
    a.addStars(CSVReader.readFile("data/stars/one-line.csv", ","));
  }

  @Test(expected = StarsLoadingException.class)
  public void testAddStarsBadForm() throws Exception {
    // Attempts to build Stars from .csv file with bad format
    a.addStars(CSVReader.readFile("data/stars/bad-format.csv", ","));
  }

  @Test
  public void testAddStarsOneRowMsg() throws Exception {
    try {
      // Attempts to read stars from a one-line .csv file
      a.addStars(CSVReader.readFile("data/stars/one-line.csv", ","));
    } catch (StarsLoadingException e) {
      // Test passes if correct two-row-message is stored in exception
      assertEquals(e.getMessage(), "at least two rows required "
          + "in file; one for headers, one for Star attributes");
    } catch (IOException e) {
      // Fails test if IOException is thrown
      fail("IOException thrown");
    }
  }

  @Test
  public void testAddStarsBadFormMsg() throws Exception {
    try {
      a.addStars(CSVReader.readFile("data/stars/bad-format.csv", ","));
    } catch (StarsLoadingException e) {
      // Passes test automatically
      assertEquals(e.getMessage(),
          "Star in row " + 7 + " of .csv file could not be constructed");
    } catch (IOException e) {
      // Fails test
      fail("IOException thrown");
    }
  }

  @Test
  public void testNameToCoordinates() throws InputValidityException {
    // Stores results of nameToCoordinates()
    float[] coords = a.nameToCoordinates("Sol");
    float deltaTolerated = 0;
    // Checks results against correct values with no delta tolerated
    assertEquals(coords[0], 0, deltaTolerated);
    assertEquals(coords[1], 0, deltaTolerated);
    assertEquals(coords[2], 0, deltaTolerated);
  }

  @Test(expected = InputValidityException.class)
  public void testNameToCoordinatesBadName() throws InputValidityException {
    // Attempts to get coordinates of Star which does not exist
    a.nameToCoordinates("FAKESTARNAME");
  }

  @Test
  public void testNameToCoordinatesBadNameMsg() {
    try {
      // Attempts to get coordinates of Star which does not exist
      // (expects Exception)
      a.nameToCoordinates("FAKESTARNAME");
    } catch (InputValidityException e) {
      // Checks that Exception message is as expected
      assertEquals(e.getMessage(),
          "Star with name \'" + "FAKESTARNAME" + "\' could not be found");
    }
  }

  @Test
  public void testNameToID() throws InputValidityException {
    String ID = a.nameToID("Sol");
    // Checks that nameToID() correctly outputs Sol's ID of 0
    assertEquals(ID, "0");
  }

  @Test(expected = InputValidityException.class)
  public void testNameToIDBadName() throws InputValidityException {
    // Attempts to get ID for a Star that does not exist
    a.nameToID("FAKESTARNAME");
  }

  @Test
  public void testNameToIDBadNameMsg() {
    try {
      // Attempts to get ID for Stars that does not exist
      // (expects Exception)
      a.nameToID("FAKESTARNAME");
    } catch (InputValidityException e) {
      // Checks that Exception's message matches expected message
      assertEquals(e.getMessage(),
          "Star with name \'" + "FAKESTARNAME" + "\' could not be found");
    }
  }

  @Test
  public void testIDToName() throws InputValidityException {
    String name = a.idToName("0");
    // Tests that idToName() associates ID "0" with the Star "Sol"
    assertEquals(name, "Sol");
  }

  @Test(expected = InputValidityException.class)
  public void testIDToNameBadID() throws InputValidityException {
    // Checks whether idToName throws correct Exception when
    // invalid Star ID is used
    a.idToName("FAKEID");
  }

  @Test
  public void testIDToNameBadIDMsg() {
    try {
      // Attempts to call idToName() with fake ID
      // (expects Exception)
      a.idToName("FAKEID");
    } catch (InputValidityException e) {
      // Checks that Exception's message is as expected
      assertEquals(e.getMessage(),
          "Star with ID \'" + "FAKEID" + "\' could not be found");
    }
  }

  @Test
  public void testStarsLoaded() {
    // Checks that starsLoaded method works correctly
    assertTrue(a.starsLoaded());
  }

  @Test
  public void testClearStars() throws StarsLoadingException {
    // Checks that Stars have in fact been loaded
    assertTrue(a.starsLoaded());
    a.clearStars();
    // Checks that clearStars() did in fact remove those Stars
    assertFalse(a.starsLoaded());
  }

  @Before
  public void setUp() throws Exception {
    // Initializes new AllStars instance
    a = new AllStars();
    // Initializes the output from readFile() for the ten-star file
    dataTenStar = CSVReader.readFile("data/stars/ten-star.csv", ",");
    // Adds Stars from the ten-star file
    a.addStars(dataTenStar);
  }

  @After
  public void tearDown() {
    // Clears the AllStars instance variables
    a = null;
  }
}
