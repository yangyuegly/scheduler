package edu.brown.cs.student.webscraper;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;

public class WebScraperTest {
  WebScraper web = new WebScraper("test");

  @Before
  public void setUp() {
    web.setCollege("clemson");
  }

  //test for getting all colleges
//  @Test
  public void testAllColleges() {
    web.getAllColleges();
    Map<String, String> coursesToIDs = web.getcoursesToIDs();
    //check for first and last to see if all have been satisfied
    assertEquals(coursesToIDs.get("Notre Dame"), "nd");
    assertEquals(coursesToIDs.get("Ivy Tech Community College, Greencastle"), "ivytechgreencastle");
  }

  //test for getting courses and conflicts
//  @Test
  public void testConflicts() {
   setUp();
   web.scrape();
   Map<String, String> conflict = web.getconflicts();
   assertEquals(conflict.get("Contemporary Art History"), "Twentieth Century Art I");
   //test to check in database
  }

}
