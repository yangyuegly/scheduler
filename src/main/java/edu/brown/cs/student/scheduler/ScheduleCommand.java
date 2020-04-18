package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

<<<<<<< HEAD
import com.google.common.base.Objects;
import com.mongodb.client.MongoDatabase;

import edu.brown.cs.student.accounts.User;
=======
>>>>>>> 17ee6c0cdd60ddddaa6a31d2996aeadc992dbb0d
import edu.brown.cs.student.graph.UndirectedWeightedGraph;
import edu.brown.cs.student.main.ICommand;
import edu.brown.cs.student.main.Main;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
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
import com.mongodb.client.result.UpdateResult;
/**
 * This class is used to schedule the user's convention. It implements the
 * ICommand interface.
 */
public class ScheduleCommand implements ICommand {
  UndirectedWeightedGraph<Event, Conflict> graph = null;
  List<Event> nodes;
  Set<Conflict> edges;
  Convention convention; 

  public ScheduleCommand(UndirectedWeightedGraph<Event, Conflict> graph, Convention convention) {
    this.graph = graph;
    this.nodes = new ArrayList<>();
    this.edges = new HashSet<>();
    this.convention = convention; 
  }

  public UndirectedWeightedGraph<Event, Conflict> getGraph() {
    return this.graph;
  }
  
  public void execute() {

  }

  public void setGraph(UndirectedWeightedGraph<Event,Conflict> graph) {
    this.graph = graph;
  }

  public List<Event> getNodes() {
    MongoCollection<org.bson.Document> eventCollection = Main.getDatabase().getCollection("events");
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", convention.getID());
    eventCollection.find();
    return this.nodes;
  }

  public void setNodes(List<Event> nodes) {
    this.nodes = nodes;
  }

  public Set<Conflict> getEdges() {
    return this.edges;
  }

  public void setEdges(Set<Conflict> edges) {
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

  public ScheduleCommand edges(Set<Conflict> edges) {
    this.edges = edges;
    return this;
  }


  @Override
  public String toString() {
    return "{" +
      " graph='" + getGraph() + "'" +
      ", nodes='" + getNodes() + "'" +
      ", edges='" + getEdges() + "'" +
      "}";
  }

  @Override
  public String getKeyword() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String execute(String input) {
    // TODO Auto-generated method stub
    return null;
  }

}
>>>>>>> da8c55e7a0af1bdde6dea9967f7ce3e1589095c8
