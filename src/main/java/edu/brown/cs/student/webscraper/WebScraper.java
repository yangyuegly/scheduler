package edu.brown.cs.student.webscraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper{

  private String collegeName = "";
  private Map<String, List<String>> deptToCourses = new HashMap<>();

  public void setCollege(String collegeName) {
    this.collegeName = collegeName;
  }


  public String scrape() {
      try {
        String website = "https://www.coursicle.com/" + collegeName + "/courses/";
        // Here we create a document object and use JSoup to fetch the website
        Document doc = Jsoup.connect(website).get();

        Elements departments = doc.getElementsByClass("tileElement");

        for(Element dep : departments) {
          String departmentTitle = dep.getElementsByClass("tileElementText subjectName").text();
          if(departmentTitle.equals("")) {
            break;
          }
          Document doc1 = Jsoup.connect("https://www.coursicle.com/clemson/courses/" + departmentTitle +"/").get();
          Elements courses = doc1.getElementsByClass("tileElement");
          List<String> allCoursesinDept = new ArrayList<>();
          for(Element course: courses) {
//            String courseNum = course.getElementsByClass("tileElementText tileElementTextWithSubtext").text();
            String courseTitle = course.getElementsByClass("tileElementHiddenText").text();
            allCoursesinDept.add(courseTitle);
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
    for(String k: keys) {
      List<String> courses = deptToCourses.get(k);
      //make a set of IEdges
      for(int i = 1; i < courses.size(); i++) {
        //make a new edge from courses.get(0) and courses.get(i)
        //add to the set above
      }
    }
  }

}
