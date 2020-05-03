package edu.brown.cs.student.scheduler;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Class to store local time
 */
public class LocalTimeSerializer extends StdSerializer<LocalTime> {

  /**
   * Constructor for LocalTimeSerializer
   */
  public LocalTimeSerializer() {
    super(LocalTime.class);
  }

  @Override
  public void serialize(LocalTime value, JsonGenerator generator, SerializerProvider provider)
      throws IOException {
    generator.writeString(value.format(DateTimeFormatter.ISO_LOCAL_TIME));
  }
}