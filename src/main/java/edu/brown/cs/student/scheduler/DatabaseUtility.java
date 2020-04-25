package edu.brown.cs.student.scheduler;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.push;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.brown.cs.student.main.Main;

/**
 * This class contains all the Database CRUD functions necessary
 */
public class DatabaseUtility {

  /**
   * Collections stored in MongoDB database
   */

  MongoCollection<Document> userCollection;
  MongoCollection<Document> conventionCollection;
  MongoCollection<Document> eventCollection;

  public DatabaseUtility() {
    if(Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority"
      );

      MongoClientSettings settings = MongoClientSettings.builder()
          .applyConnectionString(connString)
          .retryWrites(true)
          .build();
      MongoClient mongo = MongoClients.create(settings);
      //created db in cluster in MongoDBAtlas including collections: users, events, conflicts
      MongoDatabase database = mongo.getDatabase("test");
      userCollection = database.getCollection("users");
    }else {
      userCollection = Main.getDatabase().getCollection("users");
    }

    if(Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority"
      );

      MongoClientSettings settings = MongoClientSettings.builder()
          .applyConnectionString(connString)
          .retryWrites(true)
          .build();
      MongoClient mongo = MongoClients.create(settings);
      //created db in cluster in MongoDBAtlas including collections: users, events, conflicts
      MongoDatabase database = mongo.getDatabase("test");
      conventionCollection = database.getCollection("conventions");
    }else {
      conventionCollection = Main.getDatabase().getCollection("conventions");
    }

    if(Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority"
      );

      MongoClientSettings settings = MongoClientSettings.builder()
          .applyConnectionString(connString)
          .retryWrites(true)
          .build();
      MongoClient mongo = MongoClients.create(settings);
      //created db in cluster in MongoDBAtlas including collections: users, events, conflicts
      MongoDatabase database = mongo.getDatabase("test");
      eventCollection = database.getCollection("events");
    }else {
      eventCollection = Main.getDatabase().getCollection("events");
    }
  }

  /**
   * Method to check if user has access to a particular convention
   * @param userEmail - email of the user
   * @param conventionID - convention ID to check
   * @return - true if user has access and false otherwise
   */
  public boolean checkPermission(String userEmail, String conventionID) {

    BasicDBObject andQuery = new BasicDBObject();
    List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
    obj.add(new BasicDBObject("email", userEmail));
    obj.add(new BasicDBObject("conventions", conventionID));
    andQuery.put("$and", obj);

    long count = userCollection.countDocuments(andQuery);
    return count != 0;

  }

  // /**
  //  * -- adds Conflicts between all pairs of events in eventsToAttend (and stores this in the database with currConv)
  //  * @param userEmail
  //  * @param convention
  //  */
  // public static addConflicts (Convention currConv, List<Event> eventsToAttend) {


  // }

/**
 * Get a list of events associated with a convention id from the database
 * @param conventionID
 * @return list of events
 */
public List<Event> getEventsFromConventionID(String conventionID) {
  List<Event> result = new ArrayList<Event>();
  BasicDBObject query = new BasicDBObject();
  query.put("convention_id", conventionID);
  Document doc = eventCollection.find(query).first();

  //iterate through the events found
  List<Document> eventList = (List<Document>) doc.get("events");
  for (Document event: eventList) {
      Event e = new Event(event.getInteger("id"), event.getString("name"));
    result.add(e);
  }
  return result;
}

/**
 * adds the convention data to the database
 * the ID for this should already be in the database
 * @param convention
 * @return true if the given convention id already exist in the database and
 * data was able to added; false if operation fails
 */
public Boolean addConventionData(Convention convention) {
  Gson gson = new Gson();
  BasicDBObject obj = BasicDBObject.parse(gson.toJson(convention));
  //the key to this document is the convention id
  Document conventionExist = userCollection.find(all("conventions", convention.getID())).first();
  if (conventionExist != null && !conventionExist.isEmpty()) {
    Document doc = new Document(obj.toMap());
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
  * @return a boolean if the given convention id already exist in the database
  */
  public Boolean addConvID(String userEmail, String conventionID) {
    Gson gson = new Gson();
    //the key to this document is the convention id
    BasicDBObject query = new BasicDBObject();
    Document conventionExist = userCollection.find(all("conventions", conventionID)).first();

    if (conventionExist != null && !conventionExist.isEmpty()) {
      return false;//there already exists a convention with the given id
    }

    userCollection.updateOne(new Document("email", userEmail),
          new Document("$push", new Document("conventions", conventionID)));

    return true;
  }

  /**
   * Method to get conflicts based on a convention id.
   * @param conventionID -- the id of the convention
   * @return -- a hashset of all the conflicts in this convention
   */
  public static HashSet<Conflict> getConflictsFromConventionID(String conventionID) {
    HashSet<Conflict> edges = new HashSet<>();
    MongoCollection<Document> conflicCollection= Main.getDatabase().getCollection("conflicts");
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionID);

    //iterate through the events found
    List<Document> conflictList = (List<Document>)
    conflicCollection.find().projection(fields(include("conflicts"),
        excludeId()))
        .map(document -> document.get("conflicts")).first();

    //unsure if this works
    for (int i = 0; i < conflictList.size(); i++) {
      Document conflictDoc = conflictList.get(i);
      Document event1Doc = (Document) conflictDoc.get("event1");
      Document event2Doc = (Document) conflictDoc.get("event2");
      Integer weight = conflictDoc.getInteger("weight");
      Event e1 = new Event(event1Doc.getInteger("id"), event1Doc.getString("name"));
      Event e2 = new Event(event2Doc.getInteger("id"), event2Doc.getString("name"));
      Conflict c = new Conflict(e1, e2, weight);
      edges.add(c);
    }
    return edges;
  }


  /**
  * adds an event to a pre-existing convention with conventionID
  * @param userEmail
  * @param conventionID
  * @return a boolean if the given conention id exists
  */
  public Boolean addEvent(String conventionID, Event newEvent) {
    MongoCollection<Document> eventCollection = Main.getDatabase().getCollection("events");
    Gson gson = new Gson();

    BasicDBObject obj = BasicDBObject.parse(gson.toJson(newEvent));

    // try to load existing document from MongoDB
    Document document = eventCollection.find(eq("conventionID", conventionID)).first();
    if (document == null) {
      System.out.println("cannot find given convention");
      return false;
    }

    Bson change = push("events", obj);
    Bson filter = eq("conventionID", conventionID);

    eventCollection.updateOne(filter, change);
    return true;
  }

/**gets the convention data for a certain convention
 *
 * @param conventionID
 * @return string[0] = id, string[1] = name
 */
  public String[] getConventionData(String conventionID) {
    String[] result = new String[2];
    BasicDBObject query = new BasicDBObject();
    query.put("id", conventionID);
    Document doc = conventionCollection.find(query).first();
    if (doc == null) {
      throw new NullPointerException("Cannot find the convention with given id");
    }
    result[0] = doc.getString("id");
    result[1] = doc.getString("name");
    return result;
  }


  /**
   * Gets all the conventions of the User
   * @param userEmail
   * @return a List<Convention> representing all conventions of the user
   */
  public List<Convention> getUserConventions(String userEmail) {
    List<Convention> result = new ArrayList<Convention>();
    BasicDBObject query = new BasicDBObject();
    query.put("email", userEmail);
    long count = userCollection.countDocuments(query);
    Document doc = userCollection.find(query).first();
    if (doc == null) {
      throw new NullPointerException("user email doesn't exist");
    }
    BasicDBList conventionList = (BasicDBList)doc.get("conventions");
    for (int i = 0; i < conventionList.size(); i++) {
      String id = (String) conventionList.get(i);
      System.out.println("casting convention ID to a string: " + id);

      Convention c = new Convention(id);
      result.add(c);
    }

    return result;

  }

}
