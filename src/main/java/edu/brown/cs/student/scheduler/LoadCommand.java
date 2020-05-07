package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
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
 * This class is used to load in a file.
 */

public class LoadCommand {
  /**
   * Fields for conflicts and connecting to database.
   */
  // private List<Conflict> conflict;
  private MongoDatabase database;

  /**
   * Constructor for LoadCommand.
   */
  public LoadCommand() {
    // conflict = new ArrayList<>();
    // for unit testing purposes
    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017"
              + ",scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k."
              + "mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource="
              + "admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      // created db in cluster in MongoDBAtlas
      this.database = mongo.getDatabase("test");
    } else {
      this.database = Main.getDatabase();
    }
  }

  /**
   * Method to insert data in NoSQL.
   *
   * @param input - data from csv file in the form of a List of Lists of Strings, where each inner
   *        list is a row from the file
   * @param convention - the convention containing all the events in the file
   */
  public void execute(List<List<String>> input, Convention convention) {
    DatabaseUtility du = new DatabaseUtility();
    int count = 0;
    // map conflict to number of conflicts
    // Map<Conflict, Integer> frequencyMap = new HashMap<>();
    Map<String, Integer> nameToId = new HashMap<>();
    // loop through rows in csv
    for (List<String> row : input) {
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
          Conflict currConflict = new Conflict(new Event(nameToId.get(first), first, ""),
              new Event(nameToId.get(second), second, ""), 1);
          // this.conflict.add(currConflict);
          Conflict reverse = new Conflict(new Event(nameToId.get(second), second, ""),
              new Event(nameToId.get(first), first, ""), 1);
          du.addConflict(convention.getID(), currConflict);
          du.addConflict(convention.getID(), reverse);

          // // add to the weight if the conflict doesn't exist or add the conflict itself
          // frequencyMap.put(currConflict, frequencyMap.getOrDefault(conflict, 0) + 1);
          // frequencyMap.put(reverse, frequencyMap.getOrDefault(reverse, 0) + 1);

        }
      }

    }

    Gson gson = new Gson();
    // List<BasicDBObject> conflictArray = new ArrayList<>();
    List<BasicDBObject> eventArray = new ArrayList<>();

    // insert into the event table
    for (Map.Entry<String, Integer> entry : nameToId.entrySet()) {
      Event e = new Event(entry.getValue(), entry.getKey(), "");
      BasicDBObject eventObject = BasicDBObject.parse(gson.toJson(e));
      eventArray.add(eventObject);
    }

    Document currEvent = new Document("conventionID", convention.getID()).append("events",
        eventArray);

    database.getCollection("events").insertOne(currEvent);

    // for (Map.Entry<Conflict, Integer> entry : frequencyMap.entrySet()) {
    //   // set weight for each conflict
    //   entry.getKey().setWeight(entry.getValue());
    //   BasicDBObject obj = BasicDBObject.parse(gson.toJson(entry.getKey()));
    //   conflictArray.add(obj);
    // }

    // Document doc = new Document("conventionID", convention.getID()).append("conflicts",
    //     conflictArray);

    // database.getCollection("conflicts").insertOne(doc);

  }

}
