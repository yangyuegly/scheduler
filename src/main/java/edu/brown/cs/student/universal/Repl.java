package edu.brown.cs.student.universal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to create a Read-Evaluate-Print Loop.
 *
 */
public class Repl {

  /**
   * This is a field for this class.
   *
   * possibleCommands - a Map of Strings to ICommands, where the Strings
   *   represent the keywords for certain commands, and the ICommands
   *   represent the commands that can be put into this Repl
   */
  private Map<String, ICommand> possibleCommands;

  /**
   * This method is a constructor for a Repl.
   *
   * @param instructions - a Map of Strings to ICommands, where the Strings
   *   represent the keywords for certain commands, and the ICommands
   *   represent the commands that can be put into this Repl
   */
  public Repl(Map<String, ICommand> instructions) {
    possibleCommands = instructions;
  }

  /**
   * This method runs the read-eval-print-loop, prompting the user,
   *   evaluating their commands, and printing responses.
   */
  public void runRepl() {
    String input = "";
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));
    boolean accepting = true;

    while (accepting) {
      try {
        input = reader.readLine();

        if (input == null) {
          accepting = false;
        } else if (!input.equals("")) {
          String trimmedInput = input.trim();
          String[] splitInput = input.split(" ");
          
          ICommand comm = possibleCommands.get(splitInput[0]);

          if (comm == null) {
            // the given command is not in possibleCommands
            System.err.println("ERROR: command not found");
          } else {
            comm.execute(trimmedInput);
          }
        } else {
          System.exit(0);
        }

      } catch (IOException excep) {
        // error reading from the command line
        System.err.println("ERROR: " + excep.getMessage());

        try {
          reader.close();
        } catch (IOException closeExcep) {
          System.err.println("ERROR: a problem occurred while closing the "
              + "reader");
        }
      }
    }

    try {
      reader.close();
    } catch (IOException closeExcep) {
      System.err.println("ERROR: a problem occurred while closing the reader");
    }
  }
}
