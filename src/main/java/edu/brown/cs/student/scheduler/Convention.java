package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.client.MongoCollection;

import org.bson.Document;

import edu.brown.cs.student.main.Main;

/**
 * This class is used to represents a convention.  A convention is a group of smaller events.
 */
public class Convention {

  /**
   * These are fields for this class.
   * startDate - a String, the start date of the convention
   * numDays - the number of days the convention lasts
   * eventDuration - how long each event at the convention lasts
   * startTime - the earliest time that events can begin at the convention, as a String
   * endTime - the last time that events can end at the convention, as a String
   * name - a String, which represents the name of this convention
   * id - a String, which represents the id of this convention
   * events - a List of Events, which represents the events in this conference
   * eventToTimeMap - a Map of Events to Strings, where the String represents the time block
   *   that in which the key event is scheduled 
   */
  private String startDate; 
  private int numDays;
  private int eventDuration;
  private String startTime;
  private String endTime;
  
  private String name = null;
  private String id;
  private List<Event> events = new ArrayList<>();
 
  
  
  // more?

  /**
   * This is a constructor for this class.
   *
   * @param convId  - a String, which represents the id of this convention
   */

  public Convention(String convId) {
    id = convId;
  }
  
  
/**
 * Alternative constructor
 * @param convId -- id of the convention
 * @param startDate -- start date of the convention
 * @param numDays -- number of days the convention lasts
 * @param eventDuration -- the length of the events in the convention
 * @param startTime -- earliest the convention can begin
 * @param endTime -- latest the convention can end
 */
  public Convention(String convId, String startDate, int numDays, int eventDuration,
      String startTime, String endTime) {
    this.id = convId;
    this.startDate = startDate;
    this.numDays = numDays;
    this.eventDuration = eventDuration;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  /**
   * This method is a getter for the name field.
   *
   * @return a String, which represents the name of this convention
   */
  public String getName() {
    if (name == null) {
     // Database.getConventionNameFromID(this.getID());
      return "name is null"; //delete, see above
    } else {
      return name;
    }
  }

  /**
   * This method is a getter for the id field.
   *
   * @return a String, which represents the id of this convention
   */
  public String getID() {
    return id;
  }
/**
 * Method to set the name of the convention.
 * @param name -- name of the convention
 */
  public void setName(String name) {
    this.name = name;
  }
  /**
   * Method to get the events associated with the convention.
   * @return -- a list of all the associated events
   */
  public List<Event> getEvents() {
    if (events == null || events.isEmpty()) {
      return DatabaseUtility.getEventsFromConventionID(this.id);
    } else {
      return events;
    }
  }
  
  /**
   * This method adds an event to this convention.
   * 
   * @param newEvent - an Event object, which represents the event being added
   */
  public void addEvent(Event newEvent) {
    if (events == null) {
      events = new ArrayList<>();
    }

    events.add(newEvent);
  }
  /**
   * Getter to get the id. 
   * @return -- id
   */
  public String getId() {
    return id;
  }
/**
 * Getter to get the start date.
 * @return -- start date of convention
 */
  public String getStartDate() {
    return startDate;
  }
/**
 * Getter to get the number of days the convention lasts.
 * @return -- number of days the convention lasts.
 */
  public int getNumDays() {
    return numDays;
  }

/**
 * Getter to get the duration of each of the events in the convention.
 * @return -- duration of the events
 */
  public int getEventDuration() {
    return eventDuration;
  }

/**
 * Getter to get the earliest start time of the convention on a given day.
 * @return -- start time as a String
 */
  public String getStartTime() {
    return startTime;
  }

/**
 * Getter to get the end time of the convention on a given day.
 * @return -- the end time
 */
  public String getEndTime() {
    return endTime;
  }

}
