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

import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.main.Main;

public class WebScraper{

  //might have to change this
  public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36";

  private String collegeName = "";
  private Map<String, List<String>> deptToCourses = new HashMap<>();
  private Map<String, String> conflict = new HashMap<>();

  public void setCollege(String collegeName) {
    this.collegeName = collegeName;
  }


  public String scrape() {
      try {
        //check if website exists
        String website = "https://www.coursicle.com/" + collegeName + "/courses/";
        URLConnection connection = (new URL(website)).openConnection();
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } //Delay to comply with rate limiting
        connection.setRequestProperty("User-Agent", USER_AGENT);

        // Here we create a document object and use JSoup to fetch the website
        Document doc = Jsoup.connect(website).userAgent(USER_AGENT).get();

        Elements departments = doc.getElementsByClass("tileElement");

        for(Element dep : departments) {
          String departmentTitle = dep.getElementsByClass("tileElementText subjectName").text();
          if(departmentTitle.equals("")) {
            break;
          }
          String src = website + departmentTitle +"/";
          URLConnection connection1 = (new URL(src)).openConnection();
          connection1.setRequestProperty("User-Agent", USER_AGENT);
          Document doc1 = Jsoup.connect(src).userAgent(USER_AGENT).get();
          Elements courses = doc1.getElementsByClass("tileElement");
          List<String> allCoursesinDept = new ArrayList<>();
          for(Element course: courses) {
//            String courseNum = course.getElementsByClass("tileElementText tileElementTextWithSubtext").text();
            String courseTitle = course.getElementsByClass("tileElementHiddenText").text();
            allCoursesinDept.add(courseTitle);
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

      return null;
  }

  public String getKeyword() {
    return "scrape";
  }

  private void addConflicts() {
    Set<String> keys = deptToCourses.keySet();
    int count = 0;
    for(String k: keys) {
      List<String> courses = deptToCourses.get(k);

      MongoCollection<org.bson.Document> collection = Main.getDatabase().getCollection("conflicts");

      for(int i = 1; i < courses.size(); i++) {
        //make a new edge from courses.get(0) and courses.get(i)
        org.bson.Document doc = new org.bson.Document("id", count).append("class", courses.get(0))
            .append("conflict", courses.get(i));
        conflict.put(courses.get(0), courses.get(i));
        if(!(conflict.get(courses.get(i)) != null &&
            conflict.get(courses.get(i)).equals(courses.get(0)) &&
            conflict.get(courses.get(0))!= null &&
            conflict.get(courses.get(0)).equals(courses.get(i)))) {
          collection.insertOne(doc);
          count++;
        }
      }

    }
  }

}
