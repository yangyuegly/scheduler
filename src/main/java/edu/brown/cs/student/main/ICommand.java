package edu.brown.cs.student.main;

/**
 * This class is used to represent a command that can be typed into a REPL
 *   and then executed by the program.
 */
public interface ICommand {

  /**
   * This method returns a String that represents the keyword for this
   *   ICommand (for example, if the command was Echo, this method would
   *   return "echo").
   *
   * @return a String, which represents the keyword of this ICommand
   */
  String getKeyword();

  /**
   * This method executes the command, prints messages to System.out or
   *   System.err, and returns a String that represents the response to be
   *   printed by the GUI.
   *
   * @param inputs - a String, which represents the input from the user
   *
   * @return a String, which represents the message to print to the user of
   *   the GUI
   */
  String execute(String input);
}
