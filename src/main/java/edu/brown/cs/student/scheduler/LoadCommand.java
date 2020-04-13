package edu.brown.cs.student.scheduler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.exception.UserAuthenticationException;
import edu.brown.cs.student.main.ICommand;
import edu.brown.cs.student.main.Main;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

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

  public static void execute(List<String> input) {
    String conventionID = "";
    int count = 0;
    //map conflict to number of conflicts
    Map<Conflict, Integer> frequencyMap = new HashMap<>();
    Map<String, Integer> nameToId = new HashMap<>();
    //loop through rows in csv
    for (String r : input) {
      List<String> row = Arrays.asList(r.split(","));

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

          Conflict conflict = new Conflict(nameToId.get(first), nameToId.get(second));
          //add to the weight if the conflict doesn't exist or add the conflict itself
          frequencyMap.put(conflict, frequencyMap.getOrDefault(conflict, 0) + 1);
        }
      }

    }

    Document nestDoc = new Document("convention_id", conventionID).append("conflicts",
        Arrays.asList());
    MongoCollection<Document> collection = Main.getDatabase().getCollection("conflicts");
    collection.insertOne(nestDoc);

    //set weight for each conflict
    for (Map.Entry<Conflict, Integer> entry : frequencyMap.entrySet()) {

      BasicDBObject query = new BasicDBObject();
      query.put("convention_id", conventionID);
  

      BasicDBObject update = new BasicDBObject();
      update.put("$push", {"conflicts": {"event1id": entry.getKey().getevent1id(), 
        "event2id": entry.getKey().getevent2id, "weight": entry.getValue()});

      Main.getDatabase().getCollection("conflicts").updateOne(query,update);
    }

  }

}
