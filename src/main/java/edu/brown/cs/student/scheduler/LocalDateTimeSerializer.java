package edu.brown.cs.student.scheduler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Class to store local date and time. It extends StdSerializer for LocalDateTime.
 */
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

  /**
   * This is a field for this class.
   *
   * serialVersionUID - a long
   */
  private static final long serialVersionUID = 1684060049056007127L;

  /**
   * Constructor for LocalDateTimeSerializer.
   */
  public LocalDateTimeSerializer() {
    super(LocalDateTime.class);
  }

  @Override
  public void serialize(LocalDateTime value, JsonGenerator generator, SerializerProvider provider)
      throws IOException {
    generator.writeString(value.format(DateTimeFormatter.ISO_DATE_TIME));
  }
}
