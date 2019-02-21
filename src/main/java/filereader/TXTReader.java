package filereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import stringmanipulation.StringOps;

/**
 * @author vx5
 *
 *         Class that houses static readFile() method that reads from a file
 *         that is readable by a BufferedReader and returns an ArrayList of the
 *         String words in that file. Contents are sanitized by converting input
 *         to lowercase, and removing unwanted punctuation, using the
 *         StringOps.cleanInput() method.
 *
 */
public final class TXTReader {

  /**
   * Private constructor since this is a utility class.
   */
  private TXTReader() {
  }

  /**
   * Reads contents of file and returns an ArrayList of Strings that represents
   * all the words in the file, with punctuation removed and in lower case,
   * thanks to the StringOps.cleanInput() method.
   *
   * @param filepath relative filepath of file to be read (exe.
   *                 "data/autocorrect/file.txt")
   * @param regex    the word-separating token particular to this file
   * @return an ArrayList of Strings that represents all the words in the file
   * @throws Exception if file is empty or cannot be found
   */
  public static ArrayList<String> readFile(String filepath, String regex)
      throws Exception {
    // Stores LinkedList to be populated
    ArrayList<String> endList = new ArrayList<String>();
    // Creates BufferedReader to read the given file
    try {
      // Creates new BufferedReader to read file
      BufferedReader br = new BufferedReader(
          new FileReader(new File(filepath)));
      // Checks for empty file error
      String firstLine = br.readLine();
      if (firstLine == null) {
        // Closes BufferedReader
        br.close();
        // Throws new error exception
        // generic type so it works for all types of files
        throw new Exception("given file is empty");
      } else {
        // Triggers the proper read sequence
        // Stores string that will be used as checker
        String currentLine = firstLine;
        // Main reading loop that reads file
        // null is used as the break, since readLine returns it
        // once it reaches the end of the file
        while (currentLine != null) {
          // Sanitize the input
          String cleanLine = StringOps.cleanInput(currentLine);
          // String cleanLine = currentLine;
          // Split all contents
          String[] splitLine = cleanLine.split(" ");
          // Add all elements in splitLine to the endList
          for (int i = 0; i < splitLine.length; i++) {
            endList.add(splitLine[i]);
          }
          // Iterate to the next line
          currentLine = br.readLine();
        }
        // Closes BufferedReader
        br.close();
        // Returns all the contents
        return endList;
      }
    } catch (FileNotFoundException e) {
      // Throws new, more descriptive exception
      throw new FileNotFoundException("file " + filepath + " not found");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    // Returns unmodified list
    return endList;
  }

}
