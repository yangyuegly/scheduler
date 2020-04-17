package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntFunction;

import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.exception.UserAuthenticationException;
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

import org.bson.Document;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

/**
 * {
 *  convention_id: 1,
 *  conflicts: [
 *    {
 *      event1_id: 1,
 *      event2_id: 2,
 *      weight: 3
 *  },
 *  {
 *      event1_id: 1,
 *      event2_id: 3,
 *      weight: 3
 *  }
 * ],
 *  
 * }
 * 
 */

/**
 * This class is used to load in a file. It implements the ICommand interface.
 */

// (attendees, event1, event2, event3)
// store the csv title and user id into the user collection
public class LoadCommand {
  UndirectedWeightedGraph<Event, Conflict> g = null;
  List<Event> node = new ArrayList<>();
  Set<Conflict> edges = new HashSet<>();
  public LoadCommand(UndirectedWeightedGraph<Event, Conflict> g) {
    this.g = g;
    this.node = new ArrayList<Event>();

  }

  public void execute(List<String> input) {
    
    String conventionID = "";
    int count = 0;
    //map conflict to number of conflicts
    Map<Conflict, Integer> frequencyMap = new HashMap<>();
    Map<String, Integer> nameToId = new HashMap<>();
    //loop through rows in csv
    for (String r : input) {
      List<String> row = Arrays.asList(r.split(","));
      //i = 0 corresponds to attendee
      for (int i = 1; i < row.size(); i++) {
        String first = row.get(i);
        //if an event has not appeared before
        if (!nameToId.containsKey(first)) {
          //assign an id to the event
          nameToId.put(first, count);
          count++;
        }
        for (int j = i + 1; j < row.size(); j++) {
          String second = row.get(j);
          if (!nameToId.containsKey(second)) {
            nameToId.put(second, count);
            count++;
          }
          Conflict conflict = new Conflict(new Event(nameToId.get(first), first), new Event(nameToId.get(second),second), null);
          //add to the weight if the conflict doesn't exist or add the conflict itself
          frequencyMap.put(conflict, frequencyMap.getOrDefault(conflict, 0) + 1);
        }
      }

    }

    Document nestDoc = new Document("convention_id", conventionID).append("conflicts",
        Arrays.asList());
    MongoCollection<Document> collection = Main.getDatabase().getCollection("conflicts");
    collection.insertOne(nestDoc);

    for (Map.Entry<Conflict, Integer> entry : frequencyMap.entrySet()) {
      //set weight for each conflict
      entry.getKey().setWeight(entry.getValue());
      edges.add(entry.getKey());
      BasicDBObject query = new BasicDBObject();
      query.put("convention_id", conventionID);

      // BasicDBObject update = new BasicDBObject();
      // update.put("$push", {"conflicts": {"event1id": entry.getKey().getevent1id(), 
      //   "event2id": entry.getKey().getevent2id, "weight": entry.getValue()});

      // Main.getDatabase().getCollection("conflicts").updateOne(query,update);
      }
    }

  }


