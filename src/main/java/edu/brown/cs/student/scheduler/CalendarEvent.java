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
  String title;
  String start;
  String end;

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

  public String getTitle() {
    return this.title;
  }

  public String getStart() {
    return this.start;
  }

  public String getEnd() {
    return this.end;
  }
}
