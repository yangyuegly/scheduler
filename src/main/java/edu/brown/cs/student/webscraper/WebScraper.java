package edu.brown.cs.student.webscraper;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
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
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.brown.cs.student.main.Main;
import edu.brown.cs.student.scheduler.Conflict;
import edu.brown.cs.student.scheduler.DatabaseUtility;
import edu.brown.cs.student.scheduler.Event;

public class WebScraper {

  // might have to change this

  public static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36";
  private String collegeName = "";
  public Map<String, List<String>> deptToCourses = new HashMap<>();
  private Map<String, String> conflict = new HashMap<>();
  private Map<String, String> coursesToIDs = new HashMap<>();
  private String conventionID;
  DatabaseUtility du = new DatabaseUtility();
  MongoDatabase database;

  /**
   * Constructor for webscraper
   *
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
   *
   * @param collegeName - name of college to be set to
   */
  public void setCollege(String collegeName) {
    this.collegeName = collegeName;
  }

  /**
   * Get all the courses from coursicle website
   *
   * @return coursesToIDs
   */
  public Map<String, String> getcoursesToIDs() {
    return this.coursesToIDs;
  }

  /**
   * Get all the conflicts from a particular project
   *
   * @return
   */
  public Map<String, String> getconflicts() {
    return this.conflict;
  }

  /**
   * Method to get all colleges from coursicle website
   */
  public void getAllColleges() {
    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      // created db in cluster in MongoDBAtlas including collections: users, events, conflicts
      database = mongo.getDatabase("test");
    } else {
      database = Main.getDatabase();
    }

    MongoCollection<org.bson.Document> collegesCollection = database.getCollection("colleges");
    FindIterable<org.bson.Document> docs = collegesCollection.find();

    for (org.bson.Document d : docs) {
      String fullname = d.getString("fullname");
      String id = d.getString("id");
      coursesToIDs.put(fullname, id.replaceAll("/", ""));
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

      for (int i = 0; i < 4; i++) {
        Element dep = departments.get(i);
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
        deptToCourses.put(departmentTitle, allCoursesinDept);
        for (Element course : courses) {
          // String courseNum = course.getElementsByClass("tileElementText
          // tileElementTextWithSubtext").text();
          String courseTitle = course.getElementsByClass("tileElementHiddenText").text();
          if (courseTitle != "" || courseTitle != "\n" || courseTitle.isBlank()
              || courseTitle.isEmpty()) {
            List<String> coursesList = deptToCourses.get(departmentTitle);
            if (!coursesList.contains(courseTitle)) {
              coursesList.add(courseTitle);
            }
            deptToCourses.put(departmentTitle, coursesList);
          }
        }
//        System.out.println(deptToCourses.get(departmentTitle));
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
  public void addConflicts() {
    Set<String> keys = deptToCourses.keySet();
    int eventID = 0;
    MongoCollection<org.bson.Document> collection;
    MongoCollection<org.bson.Document> ecollection;
    System.out.println("here");
    // for unit testing purposes
//    if (Main.getDatabase() == null) {
//      ConnectionString connString = new ConnectionString(
//          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");
//
//      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
//          .retryWrites(true).build();
//      MongoClient mongo = MongoClients.create(settings);
//      // created db in cluster in MongoDBAtlas including collections: users, events, conflicts
//      MongoDatabase database = mongo.getDatabase("test");
//      collection = database.getCollection("conflicts");
//      ecollection = database.getCollection("events");
//    } else {
//      collection = Main.getDatabase().getCollection("conflicts");
//      ecollection = Main.getDatabase().getCollection("events");
//    }

    Gson gson = new Gson();
    List<BasicDBObject> conflictArray = new ArrayList<>();
    List<BasicDBObject> eventArray = new ArrayList<>();

    BasicDBObject query = new BasicDBObject();

    for (String k : keys) {
      List<String> courses = deptToCourses.get(k);

      for (int i = 0; i < courses.size(); i++) {
        String first = courses.get(i);
        Event event1 = new Event(eventID, first, "");
        eventID++;
        du.addEvent(conventionID, event1);
        for (int j = i + 1; j < courses.size(); j++) {
          String second = courses.get(j);
          System.out.println("here2");
          Event event2 = new Event(eventID, second, "");
          eventID++;

//          du.addEvent(conventionID, event2);

          BasicDBObject eventObject = BasicDBObject.parse(gson.toJson(event1));
          eventArray.add(eventObject);
          BasicDBObject eventObject1 = BasicDBObject.parse(gson.toJson(event2));
          eventArray.add(eventObject1);
          Conflict conflict = new Conflict(event1, event2, 100);

          BasicDBObject obj = BasicDBObject.parse(gson.toJson(conflict));
          if (!event1.equals(event2)) {
            du.addConflict(conventionID, conflict);
            conflictArray.add(obj);
          }

        }
      }
    }
    System.out.println("here4");
    deptToCourses = new HashMap<>();
    conflict = new HashMap<>();
//
//    org.bson.Document doc = new org.bson.Document("conventionID", conventionID).append("conflicts",
//        conflictArray);
//    collection.insertOne(doc);
//
//    org.bson.Document currEvent = new org.bson.Document("conventionID", conventionID)
//        .append("events", eventArray);
//    ecollection.insertOne(currEvent);
  }

}
