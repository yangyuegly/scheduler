package edu.brown.cs.student.webscraper;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.main.Main;
import edu.brown.cs.student.scheduler.Conflict;
import edu.brown.cs.student.scheduler.Event;

public class WebScraper {

  // might have to change this
  // public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64;
  // x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169
  // Safari/537.36";
//   public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36";
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
  private String collegeName = "";
  private Map<String, List<String>> deptToCourses = new HashMap<>();
  private Map<String, String> conflict = new HashMap<>();
  private Map<String, String> coursesToIDs = new HashMap<>();
  private String conventionID;

  /**
   * Constructor for webscraper
   * @param conventionID - convention id
   */
  public WebScraper(String conventionID) {
    collegeName = "";
    deptToCourses = new HashMap<>();
    conflict = new HashMap<>();
    getAllColleges();
    this.conventionID = conventionID;
  }

  /**
   * Set college name
   * @param collegeName - name of college to be set to
   */
  public void setCollege(String collegeName) {
    this.collegeName = collegeName;
  }

  /**
   * Get all the courses from coursicle website
   * @return coursesToIDs
   */
  public Map<String, String> getcoursesToIDs() {
    return this.coursesToIDs;
  }

  /**
   * Get all the conflicts from a particular project
   * @return
   */
  public Map<String, String> getconflicts() {
    return this.conflict;
  }

  /**
   * Method to get all colleges from coursicle website
   */
  public void getAllColleges() {
    try {
      String website = "https://www.coursicle.com/";
      URLConnection connection = (new URL(website)).openConnection();

      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } // Delay to comply with rate limiting
      connection.setRequestProperty("User-Agent", USER_AGENT);

      // Here we create a document object and use JSoup to fetch the website
      Document doc = Jsoup.connect(website).userAgent(USER_AGENT).timeout(0).get();

      Elements colleges = doc.getElementsByClass("tileElement");

      for (Element c : colleges) {
        String id = c.getElementsByTag("a").attr("href");
        System.out.println("ID: " + id.replace("/", ""));
        if (id.equals("")) {
          break;
        }
        String fullname = c.getElementsByTag("a").attr("fullname");
        System.out.println("Full name: " + fullname);
        coursesToIDs.put(fullname, id);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * Scrapes all courses from a given college and adds conflict
   */
  public void scrape() {
    try {
      // check if website exists
      String website = "https://www.coursicle.com/" + collegeName + "/courses/";
      URLConnection connection = (new URL(website)).openConnection();
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } // Delay to comply with rate limiting
      connection.setRequestProperty("User-Agent", USER_AGENT);

      // Here we create a document object and use JSoup to fetch the website
      Document doc = Jsoup.connect(website).userAgent(USER_AGENT).timeout(0).get();

      Elements departments = doc.getElementsByClass("tileElement");

      for (Element dep : departments) {
        String departmentTitle = dep.getElementsByClass("tileElementText subjectName").text();
        if (departmentTitle.equals("")) {
          break;
        }
        String src = website + departmentTitle + "/";
        URLConnection connection1 = (new URL(src)).openConnection();
        connection1.setRequestProperty("User-Agent", USER_AGENT);
        Document doc1 = Jsoup.connect(src).userAgent(USER_AGENT).timeout(0).get();
        Elements courses = doc1.getElementsByClass("tileElement");
        List<String> allCoursesinDept = new ArrayList<>();
        for (Element course : courses) {
          // String courseNum = course.getElementsByClass("tileElementText
          // tileElementTextWithSubtext").text();
          String courseTitle = course.getElementsByClass("tileElementHiddenText").text();
          if (courseTitle != "" || courseTitle!= "\n") {
            allCoursesinDept.add(courseTitle);
          }
          System.out.println(courseTitle);
        }
        deptToCourses.put(departmentTitle, allCoursesinDept);
        allCoursesinDept = new ArrayList<>();
      }
      addConflicts();
      // In case of any IO errors, we want the messages written to the console
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Method to add conflicts to the database
   */
  private void addConflicts() {
    Set<String> keys = deptToCourses.keySet();
    int eventID = 0;
    org.bson.Document nestDoc = new org.bson.Document("conventionID", conventionID)
        .append("conflicts", Arrays.asList());
    if(Main.getDatabase() == null) {
      return;
    }
    MongoCollection<org.bson.Document> collection = Main.getDatabase().getCollection("conflicts");;
    collection.insertOne(nestDoc);
    Gson gson = new Gson();
    List<BasicDBObject> conflictArray = new ArrayList<>();
    List<BasicDBObject> eventArray = new ArrayList<>();

    BasicDBObject query = new BasicDBObject();

    for (String k : keys) {
      List<String> courses = deptToCourses.get(k);

      // MongoCollection<org.bson.Document> collection =
      // Main.getDatabase().getCollection("conflicts");

      for (int i = 0; i < courses.size(); i++) {
        String first = courses.get(i);
        for (int j = i + 1; j < courses.size(); j++) {
          String second = courses.get(j);
          // make a new edge from courses.get(0) and courses.get(i)
          // org.bson.Document doc = new org.bson.Document("id", count).append("class",
          // courses.get(0))
          // .append("conflict", courses.get(i));
          conflict.put(first, second);
          if (conflict.containsKey(first)) {
            if (!conflict.get(first).equals(second)) {
              // collection.insertOne(doc);
              Event event1 = new Event(eventID, first);
              eventID++;
              Event event2 = new Event(eventID, second);
              eventID++;
              BasicDBObject eventObject = BasicDBObject.parse(gson.toJson(event1));
              eventArray.add(eventObject);
              BasicDBObject eventObject1 = BasicDBObject.parse(gson.toJson(event2));
              eventArray.add(eventObject1);
              Conflict conflict = new Conflict(event1, event2, 100);
              BasicDBObject obj = BasicDBObject.parse(gson.toJson(conflict));
              conflictArray.add(obj);
            }
          } else if (conflict.containsKey(second)) {
            if (!conflict.get(second).equals(first)) {
              Event event1 = new Event(eventID, first);
              eventID++;
              Event event2 = new Event(eventID, second);
              eventID++;
              BasicDBObject eventObject = BasicDBObject.parse(gson.toJson(event1));
              eventArray.add(eventObject);
              BasicDBObject eventObject1 = BasicDBObject.parse(gson.toJson(event2));
              eventArray.add(eventObject1);
              Conflict conflict = new Conflict(event1, event2, 100);
              BasicDBObject obj = BasicDBObject.parse(gson.toJson(conflict));
              conflictArray.add(obj);
            }
          } else {
            Event event1 = new Event(eventID, first);
            eventID++;
            Event event2 = new Event(eventID, second);
            eventID++;
            BasicDBObject eventObject = BasicDBObject.parse(gson.toJson(event1));
            eventArray.add(eventObject);
            BasicDBObject eventObject1 = BasicDBObject.parse(gson.toJson(event2));
            eventArray.add(eventObject1);
            Conflict conflict = new Conflict(event1, event2, 100);
            BasicDBObject obj = BasicDBObject.parse(gson.toJson(conflict));
            conflictArray.add(obj);
          }

        }
      }
    }

    org.bson.Document doc = new org.bson.Document("conventionID", conventionID).append("conflicts",
        conflictArray);
    Main.getDatabase().getCollection("conflicts").insertOne(doc);

    org.bson.Document currEvent = new org.bson.Document("convention_id", conventionID).append("events", eventArray);
    Main.getDatabase().getCollection("events").insertOne(currEvent);
  }

}
