package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Projections.*;
import edu.brown.cs.student.accounts.User;
import edu.brown.cs.student.graph.UndirectedWeightedGraph;
import edu.brown.cs.student.main.Main;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.DBObject;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;


import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import static org.junit.Assert.assertEquals;

import com.mongodb.client.result.UpdateResult;
/**
 * This class is used to schedule the user's convention. It implements the
 * ICommand interface.
 */
public class ScheduleCommand {
  List<Event> nodes;
  HashSet<Conflict> edges;
  Convention convention;
  Integer TS; 
  Integer CONCURENCY_LIMIT, MAX_SCHEDULE_DAYS;
  UndirectedWeightedGraph<Event, Conflict> graph;
  
  public ScheduleCommand(Convention convention, Integer CONCURENCY_LIMIT,
      Integer MAX_SCHEDULE_DAYS, Integer TS) {
   // this.graph = graph;
    this.TS = TS;
    this.nodes = new ArrayList<>();
    this.edges = new HashSet<>();
    this.convention = convention;
    this.CONCURENCY_LIMIT = CONCURENCY_LIMIT;
    this.MAX_SCHEDULE_DAYS = MAX_SCHEDULE_DAYS; 
  }

  public UndirectedWeightedGraph<Event, Conflict> getGraph() {
    return this.graph;
  }
  
  public void execute() {
    extractNodes();
    extractEdges();
    this.graph = new UndirectedWeightedGraph<Event, Conflict>(this.nodes, this.CONCURENCY_LIMIT,
        this.MAX_SCHEDULE_DAYS, this.TS);
    graph.addAllEdges(this.edges);
    graph.graphColoring(this.TS, this.CONCURENCY_LIMIT);
    
    Map<Integer, Event> nodes = graph.getNodes();
    for (Event event : nodes.values()) {
      List<Integer> color = event.getColor();
      System.out.println("color is" + color.get(0) + " " + color.get(1));
    }
  }

  public void setGraph(UndirectedWeightedGraph<Event,Conflict> graph) {
    this.graph = graph;
  }

  /**
   * Get events stored in the database associated with a unique convention ID
   * @return
   */
  private void extractNodes() {
    this.nodes = convention.getEvents();
//    MongoCollection<Document> eventCollection = Main.getDatabase().getCollection("events");
//    BasicDBObject query = new BasicDBObject();
//    query.put("conventionID", convention.getID());
//    Document doc = eventCollection.find(query).first();
//
//    //iterate through the events found
//    BasicDBList eventList = (BasicDBList)doc.get("events");
//    for (int i = 0; i < eventList.size(); i++) {
//      BasicDBObject eventObj = (BasicDBObject) eventList.get(i);
//      Event e = new Event(eventObj.getInt("id"), eventObj.getString("name"));
//      nodes.add(e);
//    }
  }

  public void setNodes(List<Event> nodes) {
    this.nodes = nodes;
  }

  /**
   * extract
   */
  private void extractEdges() {
    this.edges = this.convention.getConflicts();
//    MongoCollection<Document> conflicCollection= Main.getDatabase().getCollection("conflicts");
//    BasicDBObject query = new BasicDBObject();
//    query.put("conventionID", convention.getID());
//
//    //iterate through the events found
//    List<Document> conflictList = (List<Document>) 
//    conflicCollection.find().projection(fields(include("conflicts"),
//        excludeId()))
//        .map(document -> document.get("conflicts")).first();
//    
//    //unsure if this works
//    for (int i = 0; i < conflictList.size(); i++) {
//      Document conflictDoc = conflictList.get(i);
//      Document event1Doc = (Document) conflictDoc.get("event1");
//      Document event2Doc = (Document) conflictDoc.get("event2");
//      Integer weight = conflictDoc.getInteger("weight");
//      Event e1 = new Event(event1Doc.getInteger("id"), event1Doc.getString("name"));
//      Event e2 = new Event(event2Doc.getInteger("id"), event2Doc.getString("name"));
//      Conflict c = new Conflict(e1, e2, weight);
//      edges.add(c);
//    }
   }

  public void setEdges(HashSet<Conflict> edges) {
    this.edges = edges;
  }

  public ScheduleCommand graph(UndirectedWeightedGraph<Event,Conflict> graph) {
    this.graph = graph;
    return this;
  }

  public ScheduleCommand nodes(List<Event> nodes) {
    this.nodes = nodes;
    return this;
  }

  public ScheduleCommand edges(HashSet<Conflict> edges) {
    this.edges = edges;
    return this;
  }


  @Override
  public String toString() {
    return "{" +
      " graph='" + getGraph() + "'" +
      "}";
  }


}
