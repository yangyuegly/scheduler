package edu.brown.cs.student.scheduler;

import static com.mongodb.client.model.Filters.all;
import static com.mongodb.client.model.Filters.elemMatch;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;

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
  MongoDatabase database;
  ObjectMapper mapper = new ObjectMapper();

  public DatabaseUtility() {
    mapper.registerModule(new JavaTimeModule());

    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k.mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource=admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      database = mongo.getDatabase("test");
      // created db in cluster in MongoDBAtlas including collections: users, events,
      // conflicts
    } else {
      database = Main.getDatabase();
    }
    userCollection = database.getCollection("users");
    eventCollection = database.getCollection("events");
    conflictCollection = database.getCollection("conflicts");
    conventionCollection = database.getCollection("conventions");
    eventCollection = database.getCollection("events");
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

//    System.out.println("eventList's length is " + eventList.size()); // delete

    for (Document event : eventList) {
      Event e = new Event(event.getInteger("id"), event.getString("name"),
          event.getString("description"));
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

    Map<String, Object> conventionString = new HashMap<>();
    conventionString.put("id", convention.getID());
    conventionString.put("name", convention.getName());
    conventionString.put("startDateTime", convention.getStartDateTime());

    conventionString.put("numDays", convention.getNumDays());
    conventionString.put("eventDuration", convention.getEventDuration());
    conventionString.put("endDateTime", convention.getEndDateTime());

    Gson gson = new Gson();
    String eventString;
    try {
      eventString = gson.toJson(convention.getEvents());
      conventionString.put("events", eventString);
    } catch (NullPointerException e) {
      System.out.println("currently no events associated with the convention");
    }

    // the key to this document is the convention id
    Document conventionExist = userCollection.find(all("conventions", convention.getID())).first();
    if (conventionExist != null && !conventionExist.isEmpty()) {
      BasicDBObject query = new BasicDBObject("id", convention.getID());
      Document doc = new Document(conventionString);
      ReplaceOptions options = new ReplaceOptions().upsert(true);
      // check if convention collection already has this convention
      conventionCollection.replaceOne(query, doc, options);
      return true;
    }
    return false;
  }

  /**
   * Add conflicts both ways for the convenience of the graph
   *
   * @param conventionID
   * @param newConflict
   *
   * @return
   */
  public Boolean addConflict(String conventionID, Conflict newConflict) {
    Conflict reverse = new Conflict(newConflict.getTail(), newConflict.getHead(),
        newConflict.getWeight());
    Boolean err = addConflictHelper(conventionID, newConflict);
    Boolean err2 = addConflictHelper(conventionID, reverse);
    return err && err2;

  }

  public Boolean addConflictHelper(String conventionID, Conflict newConflict) {
    Gson gson = new Gson();
    BasicDBObject obj = BasicDBObject.parse(gson.toJson(newConflict));
    // =====checking if convention collection contains current conventionID
    BasicDBObject cQuery = new BasicDBObject("id", conventionID);
    Document document = conventionCollection.find(cQuery).first();
    if (document == null) {
      System.out.println(newConflict + "could not be added due to convention not found.");
      return false;
    }

    // =====checking if conflict collection contains current conventionID
    BasicDBObject checkExistInConflict = new BasicDBObject("conventionID", conventionID);
    Document findConvInConflict = conflictCollection.find(checkExistInConflict).first();
    Map<String, Object> newConventionString = new HashMap<>();
    if (findConvInConflict == null || findConvInConflict.isEmpty()) {
      newConventionString.put("conventionID", conventionID);
      conflictCollection.insertOne(new Document(newConventionString));
    }

    String str1 = String.format("{event1: %s, event2: %s, weight: {$gt: 0}}",
        gson.toJson(newConflict.event1), gson.toJson(newConflict.event2));
    // String str2 = String.format(
    // "{name: \"%s\"}", newConflict.event2.getName());

    // findIterable = collection.find(eq("instock", Document.parse("{ qty: 5, warehouse: 'A' }")));
    System.out.println(str1);

    FindIterable<Document> existingConflict = conflictCollection
        .find(elemMatch("conflicts", Document.parse(str1)));
    // FindIterable<Document> existingConflict = conflictCollection
    // .find(and(eq("conventionID", conventionID),
    // elemMatch("conflicts.event1.id",newConflict.event1.getID()),
    // eq("conflicts.event2.id",newConflict.event2.getID())
    // ));
    // elemMatch("conflicts.event2", BasicDBObject.parse(gson.toJson(newConflict.event2)))));

    // AggregateIterable<Document> existingConflict = conflictCollection.
    // (Arrays.asList(Aggregates.match(Filters.eq("conventionID", conventionID)),
    // Aggregates.match(Filters.eq("conflicts.event1", gson.toJson(newConflict.event1))),
    // Aggregates.match(Filters.eq("conflicts.event2", gson.toJson(newConflict.event2)))));
    // criteria.add(new BasicDBObject("conventionID", new BasicDBObject("$eq", conventionID)));
    List<BasicDBObject> findDuplicate = new ArrayList<BasicDBObject>();
    findDuplicate.add(new BasicDBObject("conflicts.event1",
        BasicDBObject.parse(gson.toJson(newConflict.event1))));
    findDuplicate.add(new BasicDBObject("conflicts.event2",
        BasicDBObject.parse(gson.toJson(newConflict.event2))));
    BasicDBObject andDuplicate = new BasicDBObject();
    andDuplicate.append("conventionID", conventionID);
    andDuplicate.append("$and", findDuplicate);

    // BasicDBObject criteria = new
    // BasicDBObject("conflicts.event1",BasicDBObject.parse(gson.toJson(newConflict.event1)));
    // criteria
    // (new BasicDBObject("conflicts.event1",
    // new BasicDBObject("$eq", BasicDBObject.parse(gson.toJson(newConflict.event1)))));
    // criteria.add(new BasicDBObject("conflicts.event2",
    // new BasicDBObject("$eq", BasicDBObject.parse(gson.toJson(newConflict.event2)))));
    // FindIterable<Document> findIterable = conflictCollection
    // .find(andDuplicate);
    if (existingConflict.first() == null || existingConflict.first().isEmpty()) {
      BasicDBObject update = new BasicDBObject();
      BasicDBObject query = new BasicDBObject("conventionID",
          new BasicDBObject("$eq", conventionID));
      update.put("$push", new BasicDBObject("conflicts", obj));
      conflictCollection.updateOne(query, update);
      return true;
    } else {
      System.out.println("found document");
      List<BasicDBObject> conflictArray = new ArrayList<>();
      Document doc = existingConflict.first();
//      System.out.println("adding conflict " + newConflict);
//      System.out.println("found an existing conflict " + doc);
      if (existingConflict.first() == null) {
        System.out.println("null");
      }
      // System.out.println(doc.toJson());
//      String weight = doc.getString("conventionID");
      List<Document> conflicts = (List<Document>) doc.get("conflicts");
      for (Document d : conflicts) {

        Document e1 = (Document) d.get("event1");
        Event event1 = new Event(e1.getInteger("id"), e1.getString("name"),
            e1.getString("description"));
        Document e2 = (Document) d.get("event2");
        Event event2 = new Event(e2.getInteger("id"), e2.getString("name"),
            e1.getString("description"));
        Conflict conflict = new Conflict(event1, event2, d.getInteger("weight"));
        if (conflict.equals(newConflict)) {
          System.out.println(conflict.weight);
          conflict.weight++;
        }
        BasicDBObject obj1 = BasicDBObject.parse(gson.toJson(conflict));
        conflictArray.add(obj1);
      }
      BasicDBObject update = new BasicDBObject();
      // BasicDBObject query = new BasicDBObject("$and", criteria);
      update.put("$set", new BasicDBObject("conflicts", conflictArray));
      conflictCollection.updateOne(andDuplicate, update);
      return true;
    }
    // check if event is already there
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

  public Boolean addCollaborator(String userEmail, String conventionID) {
    // try to load existing document from MongoDB
    Document document = userCollection
        .find(new BasicDBObject("email", new BasicDBObject("$eq", userEmail))).first();
    if (document == null) {
      return false;// user doesn't exist
    }

    List<BasicDBObject> criteria = new ArrayList<BasicDBObject>();
    criteria.add(new BasicDBObject("email", new BasicDBObject("$eq", userEmail)));
    criteria.add(new BasicDBObject("conventions", new BasicDBObject("$eq", conventionID)));

    FindIterable<Document> findIterable = userCollection.find(new BasicDBObject("$and", criteria));
    if (findIterable.first() == null || findIterable.first().isEmpty()) {
//      System.out.println("no duplicate");
      BasicDBObject update = new BasicDBObject();
      BasicDBObject query = new BasicDBObject("$and", criteria);
      update.put("$push", new BasicDBObject("convention", conventionID));
      userCollection.updateOne(query, update);
      return true;
    }
    return false;
  }

  // another attempt at addCollaborator
  public Boolean addConvIDCollaborator(String userEmail, String conventionID) {
    Document document = userCollection
        .find(new BasicDBObject("email", new BasicDBObject("$eq", userEmail))).first();
    if (document == null) {
      return false;// user doesn't exist
    }

    // the key to this document is the convention id
    BasicDBObject query = new BasicDBObject();
    Document conventionExist = userCollection.find(all("conventions", conventionID)).first();

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
  public Set<Conflict> getConflictsFromConventionID(String conventionIDParam) {
    Set<Conflict> edges = new HashSet<>();
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionIDParam);
    System.out.println(conventionIDParam);
    // iterate through the events found
    List<Document> conflictList = (List<Document>) conflictCollection.find(query)
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
      Event e1 = new Event(event1Doc.getInteger("id"), event1Doc.getString("name"),
          event1Doc.getString("description"));
      Event e2 = new Event(event2Doc.getInteger("id"), event2Doc.getString("name"),
          event2Doc.getString("description"));
      Conflict c = new Conflict(e1, e2, weight);
      // System.out.println("got conflicts:" + c);
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
   * @return true if the given convention id exists and event was not duplicate false otherwise
   */
  public Boolean addEvent(String conventionID, Event newEvent) {
    Gson gson = new Gson();
    BasicDBObject obj = BasicDBObject.parse(gson.toJson(newEvent));
    BasicDBObject cQuery = new BasicDBObject("id", conventionID);
    // try to load existing document from MongoDB
    Document document = conventionCollection.find(cQuery).first();
    if (document == null) {
      return false;
    }
    BasicDBObject equery = new BasicDBObject("conventionID", conventionID);
    Document findConvInEvent = eventCollection.find(equery).first();
    Document findIterable = eventCollection.find(eq("event.name", newEvent.getName())).first();

    Map<String, Object> newConventionString = new HashMap<>();
    if ((findConvInEvent == null || findConvInEvent.isEmpty())
        && (findIterable == null || findIterable.isEmpty())) {
      List<BasicDBObject> eventArray = new ArrayList<>();
      eventArray.add(obj);
      newConventionString.put("conventionID", conventionID);
      newConventionString.put("events", eventArray);
      eventCollection.insertOne(new Document(newConventionString));
      return true;
    } else if (findIterable == null || findIterable.isEmpty()) {
//      System.out.println("obj:" + obj);
//      System.out.println("conventionIDLL: " + conventionID);
      eventCollection.updateOne(eq("conventionID", conventionID), Updates.addToSet("events", obj));
//      System.out.println("in addEvent4");
      return true;
    } else {
      return false;
    }
    // check if event is already there
  }

//  public Boolean addEvent(String conventionID, Event newEvent) {
//
//    Gson gson = new Gson();
//
//    BasicDBObject obj = BasicDBObject.parse(gson.toJson(newEvent));
//    System.out.println("addEvent");
//
//    // try to load existing document from MongoDB
//    Document document = eventCollection.find(eq("conventionID", conventionID)).first();
//    if (document == null) {
//      System.out.println("cannot find given convention");
//      return false;
//    }
//
//    FindIterable<Document> findIterable = eventCollection
//        .find(eq("event.name", newEvent.getName()));
//    System.out.println(findIterable.first().toJson());
//    if (findIterable.first() == null || findIterable.first().isEmpty()) {
//      System.out.println("no duplicate");
//      BasicDBObject update = new BasicDBObject();
//      BasicDBObject query = new BasicDBObject();
//      update.put("$push", new BasicDBObject("events", obj));
//      eventCollection.updateOne(query, update);
//      System.out.println("in addEvent2");
//      return true;
//    } else {
//      return false;
//    }
//
//    // check if event is already there
//  }

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
    Date sdtDateTime = (Date) doc.get("startDateTime");
    System.out.println("stdDateTime" + sdtDateTime);
    Date et = (Date) doc.get("endDateTime");
    System.out.println(sdtDateTime);

//    LocalDateTime ldt = sdtDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    ZoneOffset offset = ZoneOffset.ofHours(0);
    LocalDateTime ldt = sdtDateTime.toInstant().atZone(ZoneId.ofOffset("GMT", offset))
        .toLocalDateTime();

    int numDays = doc.getInteger("numDays");
    int eventDuration = doc.getInteger("eventDuration");
    LocalDateTime endDateTime = et.toInstant().atZone(ZoneId.ofOffset("GMT", offset))
        .toLocalDateTime();

    System.out.println("start local: " + ldt);
    System.out.println("end local: " + endDateTime);

    // Document et = (Document) doc.get("endTime");
    // LocalTime endTime = LocalTime.of(et.getInteger("hour"), et.getInteger("minute"),
    // et.getInteger("second"), et.getInteger("nano"));

    return new Convention(id, name, ldt, numDays, eventDuration, endDateTime);
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
