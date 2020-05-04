package edu.brown.cs.student.scheduler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Class to store local date and time
 */
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

  /**
   * Constructor for LocalDateTimeSerializer
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