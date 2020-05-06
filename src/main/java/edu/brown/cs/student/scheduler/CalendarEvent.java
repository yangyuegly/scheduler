package edu.brown.cs.student.scheduler;

/**
 * This class is used to represent a scheduled event, so it is in a format that is readable for the
 * FullCalendar API that is used to display the schedule.
 */
public class CalendarEvent {

  /**
   * This are fields for this class.
   *
   * title - a String, which represents the name of the event
   *
   * start - a String, which represents the start time and day of the event (the format of this
   * matches that of a LocalDateTime that has been converted to a String (yyyy-mm-ddThh:mm)
   *
   * end - a String, which represents the end time and day of the event (the format of this matches
   * that of a LocalDateTime that has been converted to a String (yyyy-mm-ddThh:mm)
   */
  private String title;
  private String start;
  private String end;

  /**
   * This is a constructor for this class.
   *
   * @param title - a String, which represents the name of the event
   * @param start - a String, which represents the start time and day of the event (the format of
   *        this matches that of a LocalDateTime that has been converted to a String
   *        (yyyy-mm-ddThh:mm)
   * @param end - a String, which represents the end time and day of the event (the format of this
   *        matches that of a LocalDateTime that has been converted to a String (yyyy-mm-ddThh:mm)
   */
  public CalendarEvent(String title, String start, String end) {
    this.title = title;
    this.start = start;
    this.end = end;
  }

  /**
   * This method gets the title (name) of this CalendarEvent.
   *
   * @return a String, which represents the name of the event
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * This method gets the start date and time of this event.
   *
   * @return a String, which represents the start time and day of the event (the format of this
   *         matches that of a LocalDateTime that has been converted to a String (yyyy-mm-ddThh:mm)
   */
  public String getStart() {
    return this.start;
  }

  /**
   * This method gets the end date and time of this event.
   *
   * @return - a String, which represents the end time and day of the event (the format of this
   *         matches that of a LocalDateTime that has been converted to a String (yyyy-mm-ddThh:mm)
   */
  public String getEnd() {
    return this.end;
  }
}
