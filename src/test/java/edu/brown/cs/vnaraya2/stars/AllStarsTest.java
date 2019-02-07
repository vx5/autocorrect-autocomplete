package edu.brown.cs.vnaraya2.stars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import filereader.CSVReader;

public class AllStarsTest {
  private AllStars a;
  private ArrayList<String[]> dataTenStar;
  private ArrayList<String[]> dataOneLine;
  private ArrayList<String[]> dataBadFormat;

  @Test
  public void testConstruction() {
    AllStars b = new AllStars();
    assertNotNull(b);
  }

  @Test
  public void testAddAndGetStars() throws StarsLoadingException {
    a.addStars(dataTenStar);
    Star[] starList = a.getStars();
    assertEquals(starList.length, 10);
  }

  @Test(expected = StarsLoadingException.class)
  public void testAddStarsOneRow() throws StarsLoadingException, IOException {
    a.addStars(CSVReader.readFile("data/stars/one-line.csv", ","));
  }

  @Test
  public void testAddStarsOneRowMsg() {
    try {
      a.addStars(CSVReader.readFile("data/stars/one-line.csv", ","));
    } catch (StarsLoadingException e) {
      //
      assertTrue(true);
    } catch (IOException e) {
      // Fails test
      fail("IOException thrown");
    }
  }

  @Test

  public void setUp() throws IOException, StarsLoadingException {
    a = new AllStars();
    dataTenStar = CSVReader.readFile("data/stars/ten-star.csv", ",");
  }

  public void tearDown() {
    a = null;
  }

}
