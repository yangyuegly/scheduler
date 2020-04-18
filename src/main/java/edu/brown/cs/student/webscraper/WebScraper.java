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
  // public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64;
  // x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157
  // Safari/537.36";
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
  private String collegeName = "";
  private Map<String, List<String>> deptToCourses = new HashMap<>();
  private Map<String, String> conflict = new HashMap<>();
  private Map<String, String> coursesToIDs = new HashMap<>();
  private int conventionID;

  public WebScraper(Integer conventionID) {
    collegeName = "";
    deptToCourses = new HashMap<>();
    conflict = new HashMap<>();
    getAllColleges();
    this.conventionID = conventionID;
  }

  public void setCollege(String collegeName) {
    this.collegeName = collegeName;
  }

  public Map<String, String> getcoursesToIDs() {
    return this.coursesToIDs;
  }

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
        coursesToIDs.put(id, fullname);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public String scrape() {
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
          if (courseTitle != "") {
            allCoursesinDept.add(courseTitle);
          }

        }
        deptToCourses.put(departmentTitle, allCoursesinDept);
        allCoursesinDept = new ArrayList<>();
      }
      addConflicts();
      // In case of any IO errors, we want the messages written to the console
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public String getKeyword() {
    return "scrape";
  }

  public void addConflicts() {
    Set<String> keys = deptToCourses.keySet();
    int eventID = 0;
    int count = 0;
    org.bson.Document nestDoc = new org.bson.Document("conventionID", conventionID)
        .append("conflicts", Arrays.asList());
    MongoCollection<org.bson.Document> collection = Main.getDatabase().getCollection("conflicts");
    collection.insertOne(nestDoc);
    Gson gson = new Gson();
    List<BasicDBObject> conflictArray = new ArrayList<>();

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
              Conflict conflict = new Conflict(event1, event2, 100);
              BasicDBObject obj = BasicDBObject.parse(gson.toJson(conflict));
              conflictArray.add(obj);
              count++;
            }
          } else if (conflict.containsKey(second)) {
            if (!conflict.get(second).equals(first)) {
              Event event1 = new Event(eventID, first);
              eventID++;
              Event event2 = new Event(eventID, second);
              eventID++;
              Conflict conflict = new Conflict(event1, event2, 100);
              BasicDBObject obj = BasicDBObject.parse(gson.toJson(conflict));
              conflictArray.add(obj);
              count++;
            }
          } else {
            Event event1 = new Event(eventID, first);
            eventID++;
            Event event2 = new Event(eventID, second);
            eventID++;
            Conflict conflict = new Conflict(event1, event2, 100);
            BasicDBObject obj = BasicDBObject.parse(gson.toJson(conflict));
            conflictArray.add(obj);
            count++;
          }

        }
      }
    }

    org.bson.Document doc = new org.bson.Document("conventionID", conventionID).append("conflicts",
        conflictArray);
    Main.getDatabase().getCollection("conflicts").insertOne(doc);
  }

}
