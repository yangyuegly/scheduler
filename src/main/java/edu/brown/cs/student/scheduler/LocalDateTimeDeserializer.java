package edu.brown.cs.student.scheduler;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Class to store local date and time
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

  /**
   * Constructor for LocalDateTimeDeserializer
   */
  protected LocalDateTimeDeserializer() {
    super(LocalDateTime.class);
  }

  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    return LocalDateTime.parse(parser.readValueAs(String.class));
  }
}