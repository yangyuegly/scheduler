package edu.brown.cs.student.scheduler;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Updates.push;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.google.gson.Gson;
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
  MongoCollection<Document> conflictCollection;

  public DatabaseUtility() {
    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      // created db in cluster in MongoDBAtlas including collections: users, events,
      // conflicts
      MongoDatabase database = mongo.getDatabase("test");
      userCollection = database.getCollection("users");
    } else {
      userCollection = Main.getDatabase().getCollection("users");
    }

    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      // created db in cluster in MongoDBAtlas including collections: users, events,
      // conflicts
      MongoDatabase database = mongo.getDatabase("test");
      conflictCollection = database.getCollection("conflicts");
    } else {
      conflictCollection = Main.getDatabase().getCollection("conflicts");
    }

    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      // created db in cluster in MongoDBAtlas including collections: users, events,
      // conflicts
      MongoDatabase database = mongo.getDatabase("test");
      conventionCollection = database.getCollection("conventions");
    } else {
      conventionCollection = Main.getDatabase().getCollection("conventions");
    }

    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      // created db in cluster in MongoDBAtlas including collections: users, events,
      // conflicts
      MongoDatabase database = mongo.getDatabase("test");
      eventCollection = database.getCollection("events");
    } else {
      eventCollection = Main.getDatabase().getCollection("events");
    }
  }

  /**
   * Method to check if user has access to a particular convention
   *
   * @param userEmail - email of the user
   * @param conventionID - convention ID to check
   *
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
  // * -- adds Conflicts between all pairs of events in eventsToAttend (and stores
  // this in the
  // database with currConv)
  // * @param userEmail
  // * @param convention
  // */
  // public static addConflicts (Convention currConv, List<Event> eventsToAttend)
  // {

  // }

  /**
   * Get a list of events associated with a convention id from the database
   *
   * @param conventionID
   *
   * @return list of events
   */
  public List<Event> getEventsFromConventionID(String conventionID) {
    List<Event> result = new ArrayList<Event>();
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionID);
    Document doc = eventCollection.find(query).first();

    if (doc == null) {
      // there are currently no events with this ID
      return new ArrayList<>();
    }

    // iterate through the events found
    List<Document> eventList = (List<Document>) doc.get("events");

    System.out.println("eventList's length is " + eventList.size()); // delete

    for (Document event : eventList) {
      Event e = new Event(event.getInteger("id"), event.getString("name"));
      result.add(e);
    }
    return result;
  }

  /**
   * adds the convention data to the database the ID for this should already be in the database
   *
   * @param convention
   *
   * @return true if the given convention id already exist in the database and data was able to
   *         added; false if operation fails
   */
  public Boolean addConventionData(Convention convention) {
    Gson gson = new Gson();
    BasicDBObject obj = BasicDBObject.parse(gson.toJson(convention));
    // the key to this document is the convention id
    Document conventionExist = userCollection.find(all("conventions", convention.getID())).first();
    if (conventionExist != null && !conventionExist.isEmpty()) {
      Document doc = new Document(obj.toMap());
      // check if convention collection already has this convention
      conventionCollection.insertOne(doc);
      Document ce = new Document("conventionID", convention.getID()).append("events",
          Arrays.asList());
      eventCollection.insertOne(ce);
      return true;
    }
    return false;
  }

  /**
   * adds the convention data to the database first checks if there are any existing conventions
   * with conventionID; if there is a convention with that ID, return false; otherwise, add the
   * conventionID to the user with userEmail and return true
   *
   * @param userEmail
   * @param conventionID
   *
   * @return a boolean if the given convention id already exist in the database
   */
  public Boolean addConvID(String userEmail, String conventionID) {
    Gson gson = new Gson();
    // the key to this document is the convention id
    BasicDBObject query = new BasicDBObject();
    Document conventionExist = userCollection.find(all("conventions", conventionID)).first();

    if (conventionExist != null && !conventionExist.isEmpty()) {
      return false;// there already exists a convention with the given id
    }

    userCollection.updateOne(new Document("email", userEmail),
        new Document("$push", new Document("conventions", conventionID)));

    return true;
  }

  /**
   * Method to get conflicts based on a convention id.
   *
   * @param conventionID -- the id of the convention
   *
   * @return -- a hashset of all the conflicts in this convention
   */
  public HashSet<Conflict> getConflictsFromConventionID(String conventionID) {
    HashSet<Conflict> edges = new HashSet<>();
//    MongoCollection<Document> conflicCollection = Main.getDatabase().getCollection("conflicts");
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionID);

    // iterate through the events found
    List<Document> conflictList = (List<Document>) conflictCollection.find()
        .projection(fields(include("conflicts"), excludeId()))
        .map(document -> document.get("conflicts")).first();

    if (conflictList == null) {
      // there are no conflicts
      return new HashSet<>();
    }

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
   *
   * @param userEmail
   * @param conventionID
   *
   * @return a boolean if the given convention id exists
   */
  public Boolean addEvent(String conventionID, Event newEvent) {
//    MongoCollection<Document> eventCollection = Main.getDatabase().getCollection("events");
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

    // check if event is already there
    eventCollection.updateOne(filter, change);
    return true;
  }

  /**
   * gets the convention data for a certain convention
   *
   * @param conventionID
   *
   * @return a Convention
   */
  public Convention getConvention(String conventionID) {
    BasicDBObject query = new BasicDBObject();
    query.put("id", conventionID);
    Document doc = conventionCollection.find(query).first();
    if (doc == null) {
      return null;
    }

    String id = doc.getString("id");
    String name = doc.getString("name");
    Document sdt = (Document) doc.get("startDateTime");
    Document date = (Document) sdt.get("date");
    Document time = (Document) sdt.get("time");
    LocalDate ld = LocalDate.of(date.getInteger("year"), date.getInteger("month"),
        date.getInteger("day"));
    LocalTime lt = LocalTime.of(time.getInteger("hour"), time.getInteger("minute"),
        time.getInteger("second"), time.getInteger("nano"));
    LocalDateTime startDateTime = LocalDateTime.of(ld, lt);
    if (startDateTime == null) {
      System.out.println("startdatetime is null");
    } else {
      System.out.println("startdatetime is not null");
    }
    int numDays = doc.getInteger("numDays");
    int eventDuration = doc.getInteger("eventDuration");
    Document et = (Document) doc.get("endTime");
    LocalTime endTime = LocalTime.of(et.getInteger("hour"), et.getInteger("minute"),
        et.getInteger("second"), et.getInteger("nano"));

    return new Convention(id, name, startDateTime, numDays, eventDuration, endTime);
  }

  /**
   * Gets all the conventions of the User
   *
   * @param userEmail
   *
   * @return a List<Convention> representing all conventions of the user
   */
  public List<Convention> getUserConventions(String userEmail) {
    List<Convention> result = new ArrayList<Convention>();
    BasicDBObject query = new BasicDBObject();
    query.put("email", userEmail);
    Document doc = userCollection.find(query).first();
    if (doc == null) {
      return null;
    }
    // iterate through the events found
    List<String> conventionList = (List<String>) doc.get("conventions");

    for (String convention : conventionList) {
      Convention c = new Convention(convention);
      result.add(c);
    }

    return result;
  }

}
