package edu.brown.cs.student.webscraper;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class WebScraperTest {
  WebScraper web = new WebScraper("testWebScraper");
  WebScraper web1 = new WebScraper("testWebScraper");

  @Before
  public void setUp() {
    WebScraper.setCollege("brown");
  }

  // test for getting all colleges
  @Test
  public void testAllColleges() {
    web.getAllColleges();
    Map<String, String> coursesToIDs = web.getcoursesToIDs();
    // check for first and last to see if all have been satisfied
    assertEquals(coursesToIDs.get("University of Notre Dame"), "nd");
    assertEquals(coursesToIDs.get("Albany State University"), "asurams");
  }

  @Test
  public void testScrape() {
    assertEquals("616997", web.scrape());
//    assertEquals(null, web1.scrape());
  }

}
