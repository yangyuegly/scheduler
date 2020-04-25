package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import edu.brown.cs.student.main.Main;

/**
 * {
 *  conventionID: 1,
 *  conflicts: [
 *    {
 *      event1: 1,
 *      event2: 2,
 *      weight: 3
 *  },
 *  {
 *      event1: 1,
 *      event2: 3,
 *      weight: 3
 *  }
 * ],
 *
 * }
 *
 */

/**
 * This class is used to load in a file.
 */

// (attendees, event1, event2, event3)
// store the csv title and user id into the user collection
public class LoadCommand {

  /**
   * Constructor for LoadCommand
   */
  public LoadCommand() {
  }

  /**
   * Method to insert data in NoSQL
   *
   * @param input - data from csv file
   * @param convention - the convention containing all the events in the file
   */
  public void execute(List<String[]> input, Convention convention) {
    int count = 0;
    // map conflict to number of conflicts
    Map<Conflict, Integer> frequencyMap = new HashMap<>();
    Map<String, Integer> nameToId = new HashMap<>();
    // loop through rows in csv
    for (String[] r : input) {
      List<String> row = Arrays.asList(r);
      // i = 0 corresponds to attendee
      for (int i = 1; i < row.size(); i++) {
        String first = row.get(i);
        // if an event has not appeared before
        if (!nameToId.containsKey(first)) {
          // assign an id to the event
          nameToId.put(first, count);
          count++;
        }
        for (int j = i + 1; j < row.size(); j++) {
          String second = row.get(j);
          if (!nameToId.containsKey(second)) {
            nameToId.put(second, count);
            count++;
          }
          Conflict conflict = new Conflict(new Event(nameToId.get(first), first),
              new Event(nameToId.get(second), second), null);
          // add to the weight if the conflict doesn't exist or add the conflict itself
          frequencyMap.put(conflict, frequencyMap.getOrDefault(conflict, 0) + 1);
        }
      }

    }

    // fill the vertices
    // for (Map.Entry<String, Integer> entry : nameToId.entrySet()) {
    // nodes.add(new Event(entry.getValue(), entry.getKey()));
    // }

    Gson gson = new Gson();
    List<BasicDBObject> conflictArray = new ArrayList<>();
    List<BasicDBObject> eventArray = new ArrayList<>();

    // insert into the event table
    for (Map.Entry<String, Integer> entry : nameToId.entrySet()) {
      Event e = new Event(entry.getValue(), entry.getKey());
      BasicDBObject eventObject = BasicDBObject.parse(gson.toJson(e));
      eventArray.add(eventObject);
    }
    Document currEvent = new Document("conventionID", convention.getID()).append("events",
        eventArray);
    // for unit testing purposes
    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      // created db in cluster in MongoDBAtlas including collections: users, events, conflicts
      MongoDatabase database = mongo.getDatabase("test");
      database.getCollection("events").insertOne(currEvent);
    } else {
      Main.getDatabase().getCollection("events").insertOne(currEvent);
    }

    for (Map.Entry<Conflict, Integer> entry : frequencyMap.entrySet()) {
      // set weight for each conflict
      entry.getKey().setWeight(entry.getValue());
      // edges.add(entry.getKey());
      BasicDBObject obj = BasicDBObject.parse(gson.toJson(entry.getKey()));
      conflictArray.add(obj);
    }
    Document doc = new Document("conventionID", convention.getID()).append("conflicts",
        conflictArray);

    // for unit testing purposes
    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      // created db in cluster in MongoDBAtlas including collections: users, events, conflicts
      MongoDatabase database = mongo.getDatabase("test");
      database.getCollection("conflicts").insertOne(doc);
    } else {
      Main.getDatabase().getCollection("conflicts").insertOne(doc);
    }
//    Main.getDatabase().getCollection("conflicts").insertOne(doc);

  }

}
