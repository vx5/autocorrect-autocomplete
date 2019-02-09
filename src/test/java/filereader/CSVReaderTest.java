package filereader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import edu.brown.cs.vnaraya2.stars.StarsLoadingException;

public class CSVReaderTest {

  @Test
  public void testReadFile() throws IOException, StarsLoadingException {
    // Stores ArrayList output from readFile() method
    ArrayList<String[]> data = CSVReader.readFile("data/stars/ten-star.csv",
        ",");
    // Checks size for correctness
    assertTrue(data.size() == 11);
    String[] solLine = data.get(1);
    // Checks all details of Sol's element in the ArrayList for correctness
    assertEquals(solLine[0], "0");
    assertEquals(solLine[1], "Sol");
    assertEquals(solLine[2], "0");
    assertEquals(solLine[3], "0");
    assertEquals(solLine[4], "0");
  }

  @Test(expected = StarsLoadingException.class)
  public void testReadFileEmptyFile()
      throws IOException, StarsLoadingException {
    // Tests that readFile() throws correct Exception when empty file passed
    CSVReader.readFile("data/stars/empty.csv", ",");
  }

  @Test
  public void testReadFileEmptyFileMsg() {
    try {
      // Attempts to pass empty file to readFile()
      // (expects Exception)
      CSVReader.readFile("data/stars/empty.csv", ",");
    } catch (IOException e) {
      // Fails test in case of IOException
      fail("IOException thrown");
    } catch (StarsLoadingException e) {
      // Checks that Exception's message is as desired
      assertEquals(e.getMessage(), "given file is empty");
    }
  }

  @Test(expected = FileNotFoundException.class)
  public void testReadFileBadFileName()
      throws IOException, StarsLoadingException {
    // Checks that invalid filename draws correct Exception
    CSVReader.readFile("fakefilename", ",");
  }

  @Test
  public void testReadFileBadFileNameMsg() {
    try {
      // Attempts to feed invalid filename to readFile()
      // (expects Exception)
      CSVReader.readFile("fakefilename", ",");
    } catch (FileNotFoundException e) {
      // Verifies that correct message was stored in Exception
      assertEquals(e.getMessage(), "file fakefilename not found");
    } catch (IOException | StarsLoadingException e) {
      // Fails test if one of these unexpected exceptions was thrown
      fail("Unexpected exception was thrown");
    }
  }
}
