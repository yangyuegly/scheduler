package edu.brown.cs.student.csvparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to parse a CSV file into a list of a list of Strings.
 *
 */
public class CSVParser {

  /**
   * The following are fields for this class.
   *
   * fileName - the name of the CSV file to parse
   * firstLine - a String, which (if non-null) should match the first line of
   *   the CSV file (if it is null, the first line of the file should be
   *   treated normally)
   * fileData - this field is a List<String[]>, and it represents the data
   *   from the CSV file, where each inner array is a row from the file
   * numCols - an int, which represents the number of columns of the CSV file
   */
  private String fileName;
  private String firstLine;
  private List<String[]> fileData;
  private int numCols;

  /**
   * This method is a constructor for a CSVParser.  This constructor should be
   *   used in the case that the first line of the file should be treated
   *   normally.
   *
   * @param file - a String, which represents the name of the file to parse
   */
  public CSVParser(String file) {
    fileName = file;
    firstLine = null;
    fileData = new LinkedList<>();
  }

  /**
   * This method is a constructor for a CSVParser.  This constructor should
   *   be used in the case that the first line should match a certain header
   *   String instead of being stored in the fileData list.
   *
   * @param file - a String, which represents the name of the file to parse
   * @param first - a String, which represents a String that should match
   *   the first line of the CSV file
   */
  public CSVParser(String file, String first) {
    fileName = file;
    firstLine = first;
    fileData = new LinkedList<>();
  }

  /**
   * This method returns the data from the file that has been parsed (or
   *   an empty list if it has not yet been parsed).
   *
   * @return a List of arrays of Strings, which represents the data from
   *    the file, where each inner array represents a line from the file
   */
  public List<String[]> getFileData() {
    return fileData;
  }

  /**
   * This method parses the csv file and puts the data from the file into
   *   fileData.
   *
   * @return a boolean, true if the method was successful, false otherwise
   */
  public boolean parse() {
    boolean valid = true;

    try {
      FileReader fiReader = new FileReader(fileName);
      BufferedReader bufReader = new BufferedReader(fiReader);

      if (firstLine != null) {
        // in this case, the first line should match the firstLine header
        //   String instead of being stored as data in fileData

        String line = bufReader.readLine();

        if (line != null) {
          // the file is non-empty
          String[] splitLine = line.split(",");
          numCols = splitLine.length;

          if (!line.equals(firstLine)) {
            valid = false;
            fileData = null;
            System.err.println("ERROR: The first line of the file should be '"
                + firstLine + "'");
          }

        } else {
          // the file is empty
          System.err.println("ERROR: This file contains no data");
          valid = false;
        }
      }

      String line = bufReader.readLine();

      if (line == null) {
        // the file is empty
        valid = false;
        fileData = null;
        System.err.println("ERROR: This file contains no data");
      } else {
        String[] splitLine1 = line.split(",");
        numCols = splitLine1.length;
      }

      while ((line != null) && valid) {
        String[] splitLine = line.split(",");

        if (numCols != splitLine.length) {
          System.err.println("ERROR: The number of columns is inconsistent");
          valid = false;
          fileData = null;
        } else {
          fileData.add(splitLine);
          line = bufReader.readLine();
        }
      }

      bufReader.close();
      fiReader.close();

    } catch (FileNotFoundException notFoundExcep) {
      System.err.println("ERROR: No such file or directory");
      fileData = null;
      return false;
    } catch (IOException ioExcep) {
      System.err.println("ERROR: " + ioExcep.getMessage());
      fileData = null;
      return false;
    }

    return valid;
  }
}
