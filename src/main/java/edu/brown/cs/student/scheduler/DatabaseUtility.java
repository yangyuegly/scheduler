package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.main.Main;

public class DatabaseUtility {
  public static Boolean checkPermision(String userEmail, String conventionID) {
    MongoCollection<Document> userCollection = Main.getDatabase().getCollection("users");
    // String formattedQuery = String.format("{ id: %1$s }", conventionID);
    // Bson conventionFilter = Filters.eq("conventions", Document.parse(formattedQuery));
    // Bson emailFilter = Filters.eq("email", userEmail);

    // FindIterable<Document> findIterable = userCollection.find(and(eq("conventions.id", conventionID), eq("email", userEmail)));

    BasicDBObject andQuery = new BasicDBObject();
    List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    obj.add(new BasicDBObject("email", userEmail));
    obj.add(new BasicDBObject("conventions.id", conventionID));
    andQuery.put("$and", obj);

    long count = userCollection.countDocuments(andQuery);
    return count!=0;

  }

  public static void createConvention(String userEmail, Convention convention) {
    MongoCollection<Document> userCollection = Main.getDatabase().getCollection("users");
  }

/**
 * Get a list of events associated with a convention id from the database
 * @param conventionID
 * @return list of events
 */
public static List<Event> getEventsFromConventionID(String conventionID) {
  List<Event> result = new ArrayList<Event>();
  MongoCollection<Document> eventCollection = Main.getDatabase().getCollection("events");
  BasicDBObject query = new BasicDBObject();
  query.put("conventionID", conventionID);
  Document doc = eventCollection.find(query).first();

  //iterate through the events found
  BasicDBList eventList = (BasicDBList) doc.get("events");
  for (int i = 0; i < eventList.size(); i++) {
    BasicDBObject eventObj = (BasicDBObject) eventList.get(i);
    Event e = new Event(eventObj.getInt("id"), eventObj.getString("name"));
    result.add(e);
  }
  return result;
}

/**
 * adds the convention data to the database
 * the ID for this should already be in the database
 * @param convention
 * @return a boolean if the given conention id already exist in the database
 */
public Boolean addConventionData(Convention convention) {
  MongoCollection<Document> conventionCollection = Main.getDatabase().getCollection("conventions");
  MongoCollection<Document> userCollection = Main.getDatabase().getCollection("users");
  Gson gson = new Gson();
  BasicDBObject obj = BasicDBObject.parse(gson.toJson(convention));
  //the key to this document is the convention id
  BasicDBObject query = new BasicDBObject("conventions.id",
      new BasicDBObject("$eq", convention.getID()));
  Document currCon = userCollection.find(query).first();
  if (currCon != null && !currCon.isEmpty()) {
    Document doc = new Document(convention.getID(), obj);
    conventionCollection.insertOne(doc);
    return true;
  }
  return false;
}

  /**
  * adds the convention data to the database
  * first checks if there are any existing conventions with conventionID;
  * if there is a convention with that ID, return false; otherwise, add the conventionID to the user with userEmail and return true
  * @param userEmail
  * @param conventionID
  * @return a boolean if the given conention id already exist in the database
  */
  public static Boolean addConvID(String userEmail, String conventionID){
    MongoCollection<Document> userCollection = Main.getDatabase().getCollection("users");
    Gson gson = new Gson();
    //the key to this document is the convention id
    BasicDBObject query = new BasicDBObject();
    query.put("email", userEmail);
    query.put("conventions.id", conventionID);
    Document currCon = userCollection.find(query).first();

    if (currCon != null && !currCon.isEmpty()) {
      return false;

      return true;
    } else {
      Document doc = new Document(conventionID, conventionID);
      conventionCollection.insertOne(doc);
    }
    return false;
  }
}
