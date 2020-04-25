package edu.brown.cs.student.scheduler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This class is used to represents a convention. A convention is a group of smaller events.
 */
public class Convention {

  /**
   * These are fields for this class.
   *
   * name - a String, which represents the name of this convention
   *
   * id - a String, which represents the id of this convention
   *
   * startDateTime - a LocalDateTime, which represents the start date of the convention at time the
   * convention starts
   *
   * numDays - an int, which represents the number of days the convention lasts
   *
   * eventDuration - how long each event at the convention lasts, in minutes
   *
   * endTime - a LocalTime, which represents the last time that events can end on a given day
   *
   * events - a List of Events, which represents the events in this conference
   */
  private String name = null;
  private String id = null;
  private LocalDateTime startDateTime;
  private int numDays = -1; // setting this to -1 so we know that it has not been set yet
  private int eventDuration = -1;
  private LocalTime endTime;
  private List<Event> events = null;
  DatabaseUtility du = new DatabaseUtility();

  /**
   * This is a constructor for this class.
   *
   * @param convId - a String, which represents the id of this convention
   */

  public Convention(String convId) {
    id = convId;
  }

  /**
   * This is another constructor for this class.
   *
   * @param convName - a String, which represents the name of this convention
   * @param convId - a String, which represents the id of this convention
   * @param startDateTime - a LocalDateTime, which represents the start date of the convention at
   *        time the convention starts
   * @param numDays - an int, which represents the number of days the convention lasts
   * @param eventDuration - an int, which represents how long each event at the convention lasts, in
   *        minutes
   * @param endTime - a LocalTime, which represents the last time that events can end on a given day
   */
  public Convention(String convId, String convName, LocalDateTime startDateTime, int numDays,
      int eventDuration, LocalTime endTime) {
    this.id = convId;
    this.name = convName;
    this.numDays = numDays;
    this.eventDuration = eventDuration;
    this.endTime = endTime;
  }

  /**
   * Alternative constructor
   *
   * @param convId -- a String, which represents the id of the convention
   *
   * @param convName -- a String, which represents the name of the convention
   * @param startDate -- a String of the format "yyyy-mm-dd", which represents the start date of the
   *        convention
   * @param numDays -- an int, which represents number of days the convention lasts
   * @param eventDuration -- an int, which represents the length of the events in the convention in
   *        minutes
   * @param startTime -- a String of the format "hh:mm" (in military time), which represents the
   *        earliest the convention can begin on a given day
   * @param endTime -- a String of the format "hh:mm" (in military time), which represents the
   *        latest the convention can end on a given day
   *
   * @throws NumberFormatException
   */
  public Convention(String convId, String convName, String startDate, int numDays,
      int eventDuration, String startTime, String endTime) throws NumberFormatException {

    this.id = convId;
    this.name = convName;
    this.numDays = numDays;
    this.eventDuration = eventDuration;

    String[] startDateSplit = startDate.split("-");
    String[] startTimeSplit = startTime.split(":");
    String[] endTimeSplit = endTime.split(":");

    int year = Integer.parseInt(startDateSplit[0]);
    int month = Integer.parseInt(startDateSplit[1]);
    int dayOfMonth = Integer.parseInt(startDateSplit[2]);
    int startHour = Integer.parseInt(startTimeSplit[0]);
    int startMinute = Integer.parseInt(startTimeSplit[1]);
    int endHour = Integer.parseInt(endTimeSplit[0]);
    int endMinute = Integer.parseInt(endTimeSplit[1]);

    LocalDate startDateLocalDate = LocalDate.of(year, month, dayOfMonth);
    LocalTime startTimeLocalTime = LocalTime.of(startHour, startMinute);
    this.endTime = LocalTime.of(endHour, endMinute);
    this.startDateTime = LocalDateTime.of(startDateLocalDate, startTimeLocalTime);
  }

  /**
   * This method is a getter for the name field.
   *
   * @return a String, which represents the name of this convention
   */
  public String getName() {
    if (name == null) {
      // Database.getConventionNameFromID(this.getID());
      return "name is null"; // delete, see above
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
   *
   * @param name -- name of the convention
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Method to get the events associated with the convention.
   *
   * @return -- a list of all the associated events
   */
  public List<Event> getEvents() {
    if (events == null) {
      return du.getEventsFromConventionID(this.id);
      // what will this return if there are none?
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
   *
   * @return -- id
   */
  public String getId() {
    return id;
  }

  /**
   * Getter to get the start date and time.
   *
   * @return -- a LocalDateTime object, which represents the start date and time of the convention
   */
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  /**
   * Getter to get the number of days the convention lasts.
   *
   * @return -- number of days the convention lasts.
   */
  public int getNumDays() {
    return numDays;
  }

  /**
   * Getter to get the duration of each of the events in the convention.
   *
   * @return -- duration of the events
   */
  public int getEventDuration() {
    return eventDuration;
  }

  /**
   * Getter to get the end time of the convention on a given day.
   *
   * @return -- the end time
   */
  public LocalTime getEndTime() {
    return endTime;
  }

  /**
   * This method is used to get the max number of time slots on a given da. It uses the time
   * constraints given when the Convention was created to determine how many events of the given
   * duration can fit in the time range.
   *
   * @return an int, which represents the max number of time slots on a given day
   */
  public int getNumTimeSlotsPerDay() {
    LocalDate startDateLocalDate = startDateTime.toLocalDate();
    LocalDateTime endTimeOnStartDay = LocalDateTime.of(startDateLocalDate, endTime);

    Duration durationInDay = Duration.between(startDateTime, endTimeOnStartDay);
    Long numMinutesInDay = durationInDay.toMinutes();

    return Math.floorDiv(numMinutesInDay.intValue(), eventDuration);
  }

  public HashSet<Conflict> getConflicts() {
    return du.getConflictsFromConventionID(this.id);
  }
}
