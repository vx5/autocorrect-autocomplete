package fileReading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
	// Stores method that reads file from filename, regex
	public static ArrayList<String[]> readFile(String readerFilename,
			String regex) {
		try {
			// Creates BufferedReader to read the given file
			BufferedReader br = new BufferedReader(new FileReader(
					new File(readerFilename)));
			// Creates new ArrayList<String[]> to hold all read data
			ArrayList<String[]> contents = new ArrayList<String[]>();
			// Checks for empty file error
			String firstLine = br.readLine();
			if (firstLine == null) {
				// Prints error message 
				System.out.println("ERROR: Given file is empty");
				// Closes BufferedReader
				br.close();
				// Returns null as signal to where method is called
				return null;
			}
			// Triggers the proper read sequence
			else {
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
			// Prints error message for file not found
			System.out.println("ERROR: File not found");
			// Uses auto-generated error message
			e.printStackTrace();
		} catch (IOException e) {
			// Prints error message for IOException
			System.out.println("ERROR: IOException");
			// Uses auto-generated error message
			e.printStackTrace();
		}
		// Returning null is a signal to where method is called
		return null;
	}	
}
