package edu.brown.cs.student.csvparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to parse a CSV file into a list of a list of Strings.
 *
 */
public class CSVParser {
  
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
