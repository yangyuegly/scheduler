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
   * Method to parse the CSV file.
   *
   * @param textBuilder - filename
   *
   * @return - list of rows, where each row is a List of string (row split by commas)
   */
  public List<List<String>> parse(StringBuilder textBuilder) {
    String text = textBuilder.toString();
    List<String> textList = Arrays.asList(text.split("\n"));
    List<List<String>> inputList = new ArrayList<>();
    for (String row : textList) {
      String[] splitRow = row.split(",");
      List<String> splitRowMinusBlanks = new ArrayList<>();

      for (String elem : splitRow) {
        String trimmedElem = elem.trim();

        if (!trimmedElem.equals("")) {
          // don't include blanks or spaces
          splitRowMinusBlanks.add(trimmedElem);
        }
      }

      inputList.add(splitRowMinusBlanks);
    }
    return inputList;
  }

}
