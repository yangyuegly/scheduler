package edu.brown.cs.student.csvparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to parse a CSV file into a list of a list of Strings.
 *
 */
public class CSVParser {

  /**
   * Method to parse the CSV file
   * @param textBuilder - filename
   * @return - list of rows, where each row is a string [] (row split by spaces)
   */
  public List<String[]> parse(StringBuilder textBuilder){
    String text = textBuilder.toString();
    List<String> textList = Arrays.asList(text.split("\n"));
    List<String[]> inputList = new ArrayList<>();
    for (String row : textList) {
      String[] splitRow = row.split(",");
      System.out.println("row");
      inputList.add(splitRow);
    }
    return inputList;
  }

}
