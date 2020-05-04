package edu.brown.cs.student.scheduler;

import java.io.IOException;
import java.time.LocalTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Class to store local time
 */
public class LocalTimeDeserializer extends StdDeserializer<LocalTime> {

  /**
   * Constructor for LocalTimeDeserializer
   */
  protected LocalTimeDeserializer() {
    super(LocalTime.class);
  }

  @Override
  public LocalTime deserialize(JsonParser parser, DeserializationContext context)
      throws IOException {
    return LocalTime.parse(parser.readValueAs(String.class));
  }
}