package edu.brown.cs.student.scheduler;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
   * endTime - a LocalDateTime, which represents the last time that events can end on the last given
   * day (although all days in a convention have the same start and end times)
   *
   * events - a List of Events, which represents the events in this conference
   *
   * loadedInDb - a boolean, true if the convention was loaded into the database prior to this
   * convention being constructed, else false
   */
  private String name = null;
  private String id = null;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime startDateTime;
  private Integer numDays = -1; // setting this to -1 so we know that it has not been set yet
  private Integer eventDuration = -1;

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime endDateTime;
  private List<Event> events = null;
  boolean loadedInDb = false;

  @Override
  public String toString() {
    return "Convention [name=" + name + ", id=" + id + ", startDateTime=" + startDateTime
        + ", numDays=" + numDays + ", eventDuration=" + eventDuration + ", endDateTime="
        + endDateTime + "]";
  }

  /**
   * This is a constructor for this class.
   *
   * @param convId - a String, which represents the id of this convention
   */

  public Convention(String convId) {
    id = convId;
    // load in the rest of the fields from the database
    DatabaseUtility du = new DatabaseUtility();
    Convention conv = du.getConvention(convId);

    if (conv != null) {
      loadedInDb = true;
      this.name = conv.name;
      this.startDateTime = conv.startDateTime;
      this.numDays = conv.numDays;
      this.eventDuration = conv.eventDuration;
      this.endDateTime = conv.endDateTime;
    }

  }

  /**
   * This is a constructor for this class.
   *
   * @param convId - a String, which represents the id of this convention
   */

  public Convention(String convId, String convName) {
    id = convId;
    // load in the rest of the fields from the database
    this.name = convName;

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
   * @param endDateTime - a LocalDateTime, which represents the last time that events can end on the
   *        last given day (although all days in a convention have the same start and end times)
   */
  public Convention(String convId, String convName, LocalDateTime startDateTime, Integer numDays,
      Integer eventDuration, LocalDateTime endDateTime) {
    this.id = convId;
    this.name = convName;
    this.numDays = numDays;
    this.eventDuration = eventDuration;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    loadedInDb = true;
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
  public Convention(String convId, String convName, String startDate, Integer numDays,
      Integer eventDuration, String startTime, String endTime) throws NumberFormatException {

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

    LocalDate endDateLocalDate = LocalDate.of(year, month, dayOfMonth);
    endDateLocalDate = endDateLocalDate.plusDays(numDays);
    LocalTime endTimeLocalTime = LocalTime.of(endHour, endMinute);

    this.startDateTime = LocalDateTime.of(startDateLocalDate, startTimeLocalTime);
    this.endDateTime = LocalDateTime.of(endDateLocalDate, endTimeLocalTime);
  }

  /**
   * This method determines if the convention has been loaded into the database.
   *
   * @return true if the convention was loaded into the database prior to this convention being
   *         constructed, else false
   */
  public boolean isLoaded() {
    return loadedInDb;
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
      DatabaseUtility du = new DatabaseUtility();
      return du.getEventsFromConventionID(id);
    }

    // so we aren't returning a private mutable field
    return new ArrayList<>(events);
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
   * Getter to get the start date and time.
   *
   * @return -- a LocalDateTime object, which represents the start date and time of the convention
   */
  @JsonGetter
  public LocalDateTime getStartDateTime() {
    return startDateTime;
  }

  /**
   * Getter to get the number of days the convention lasts.
   *
   * @return -- number of days the convention lasts.
   */
  public Integer getNumDays() {
    return numDays;
  }

  /**
   * Getter to get the duration of each of the events in the convention.
   *
   * @return -- duration of the events
   */
  public Integer getEventDuration() {
    return eventDuration;
  }

  /**
   * Getter to get the end time of the convention on last day.
   *
   * @return -- the end time on the end day
   */
  @JsonGetter
  public LocalDateTime getEndDateTime() {
    return endDateTime;
  }

  /**
   * This method is used to get the max number of time slots on a given da. It uses the time
   * constraints given when the Convention was created to determine how many events of the given
   * duration can fit in the time range.
   *
   * @return an int, which represents the max number of time slots on a given day
   */
  public Integer getNumTimeSlotsPerDay() {
    LocalDate startDateLocalDate = startDateTime.toLocalDate();
    LocalTime endTime = endDateTime.toLocalTime();
    LocalDateTime endTimeOnStartDay = LocalDateTime.of(startDateLocalDate, endTime);

    Duration durationInDay = Duration.between(startDateTime, endTimeOnStartDay);
    Long numMinutesInDay = durationInDay.toMinutes();

    return Math.floorDiv(numMinutesInDay.intValue(), eventDuration);
  }

  public Set<Conflict> getConflicts() {
    DatabaseUtility du = new DatabaseUtility();
    return du.getConflictsFromConventionID(this.id);
  }
}
