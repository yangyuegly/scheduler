package edu.brown.cs.student.webscraper;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import edu.brown.cs.student.scheduler.DatabaseUtility;

public class WebScraperTest {
  WebScraper web = new WebScraper("testWebScraper");

  @Before
  public void setUp() {
    web.setCollege("clemson");
    Map<String, List<String>> deptToCourse = new HashMap<>();
    List<String> aah = new ArrayList<>();
    aah.add("e1");
    aah.add("e2");
    deptToCourse.put("AAH", aah);
    List<String> YDP = new ArrayList<>();
    YDP.add("a1");
    YDP.add("a2");
    deptToCourse.put("YDP", YDP);
    web.deptToCourses = deptToCourse;
  }

  // test for getting all colleges
//  @Test
  public void testAllColleges() {
    web.getAllColleges();
    Map<String, String> coursesToIDs = web.getcoursesToIDs();
    // check for first and last to see if all have been satisfied
    assertEquals(coursesToIDs.get("Notre Dame"), "nd");
    assertEquals(coursesToIDs.get("Ivy Tech Community College, Greencastle"), "ivytechgreencastle");
  }

  // test for getting courses and conflicts
//  @Test
  public void testConflicts() {
    setUp();
    DatabaseUtility du = new DatabaseUtility();
//    web.scrape();
    web.addConflicts();
//    Map<String, String> conflict = web.getconflicts();
//    assertEquals(conflict.get("e1"), "e2");
    // test to check in database
  }

}
