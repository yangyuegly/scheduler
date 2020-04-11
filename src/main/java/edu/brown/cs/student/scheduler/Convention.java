package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is used to represents a convention.  A convention is a group of smaller events.
 */
public class Convention {

  /**
   * These are fields for this class.
   *
   * name - a String, which represents the name of this convention
   * id - a String, which represents the id of this convention
   * events - a List of Events, which represents the events in this conference
   * eventToTimeMap - a Map of Events to Strings, where the String represents the time block
   *   that in which the key event is scheduled 
   */
  private String name;
  private String id;
  private List<Event> events = new ArrayList<>();
  private Map<Event, String> eventToTimeMap = new HashMap<>();
  // more?

  /**
   * This is a constructor for this class.
   *
   * @param convName - a String, which represents the name of this convention
   * @param convId  - a String, which represents the id of this convention
   */
  public Convention(String convName, String convId) {
    name = convName;
    id = convId;
  }

  /**
   * This method is a getter for the name field.
   *
   * @return a String, which represents the name of this convention
   */
  public String getName() {
    return name;
  }

  /**
   * This method is a getter for the id field.
   *
   * @return a String, which represents the id of this convention
   */
  public String getID() {
    return id;
  }

  // more

}
