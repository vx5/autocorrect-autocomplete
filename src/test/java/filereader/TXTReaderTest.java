package filereader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

public class TXTReaderTest {

  @Test
  public void testReadFile() {
    try {
      // Test for standard read procedure
      ArrayList<String> list = TXTReader.readFile("data/autocorrect/norton.txt",
          " ");
      assertEquals(list.size(), 1);
      assertEquals(list.get(0), "norton");
    } catch (Exception e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testReadFileExceptionEmptyMsg() {
    // Tests that proper Exception is thrown for empty file
    try {
      TXTReader.readFile("data/autocorrect/empty.txt", " ");
    } catch (Exception e) {
      assertEquals(e.getMessage(), "given file is empty");
    }
  }

  @Test
  public void testReadFileExceptionNotFoundMsg() {
    // Tests that proper Exception is thrown for nonexistent file path
    try {
      TXTReader.readFile("data/autocorrect/fakefilename.txt", " ");
    } catch (Exception e) {
      assertEquals(e.getMessage(),
          "file data/autocorrect/fakefilename.txt not found");
    }
  }
}
