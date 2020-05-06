package edu.brown.cs.student.scheduler;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Class to store local date and time. It extends StdDeserializer for LocalDateTime.
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

  /**
   * This is a field for this class.
   *
   * serialVersionUID - a long
   */
  private static final long serialVersionUID = 6452340412808143091L;

  /**
   * Constructor for LocalDateTimeDeserializer.
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
