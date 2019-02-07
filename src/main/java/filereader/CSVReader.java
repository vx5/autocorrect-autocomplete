package filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.brown.cs.vnaraya2.stars.StarsLoadingException;

/**
 * Houses static method that parses input CSV file.
 *
 * @author vx5
 */
public final class CSVReader {

  /**
   * Private constructor given utility class.
   */
  private CSVReader() {
  }

  /**
   * Reads a .csv file line-by-line using a BufferedReader.
   *
   * @param readerFilename CSV filename in String form
   * @param regex          delimiter to split each line of CSV file in String
   *                       form
   * @return ArrayList of all lines of CSV file, split
   * @throws IOException           if there is an error in opening, reading, or
   *                               closing the file
   * @throws StarsLoadingException if the given file is empty
   */
  public static ArrayList<String[]> readFile(String readerFilename,
      String regex) throws IOException, StarsLoadingException {
    try {
      // Creates BufferedReader to read the given file
      BufferedReader br = new BufferedReader(
          new FileReader(new File(readerFilename)));
      // Creates new ArrayList<String[]> to hold all read data
      ArrayList<String[]> contents = new ArrayList<String[]>();
      // Checks for empty file error
      String firstLine = br.readLine();
      if (firstLine == null) {
        // Closes BufferedReader
        br.close();
        // Throws new error exception
        throw new StarsLoadingException("given file is empty");
      } else {
        // Triggers the proper read sequence
        // Stores string that will be used as checker
        String currentLine = firstLine;
        // Main reading loop that reads file
        // null is used as the break, since readLine returns it
        // once it reaches the end of the file
        while (currentLine != null) {
          contents.add(currentLine.split(regex));
          // Iterate to the next line
          currentLine = br.readLine();
        }
        // Closes BufferedReader
        br.close();
        // Returns all the contents
        return contents;
      }
    } catch (FileNotFoundException e) {
      // Throws new Exception with message attached
      throw new FileNotFoundException("file " + readerFilename + " not found");
    } catch (IOException e) {
      // Throws new Exception with specific message
      throw new IOException(
          "IOException found when reading from " + readerFilename);
    }
  }
}
