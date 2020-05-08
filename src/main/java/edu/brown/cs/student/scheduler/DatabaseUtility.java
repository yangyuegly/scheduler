package edu.brown.cs.student.scheduler;

import static com.mongodb.client.model.Filters.all;
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
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.Updates;

import edu.brown.cs.student.main.Main;

/**
 * This class contains all the Database CRUD functions necessary.
 */
public class DatabaseUtility {
  /**
   * These are fields for this class. The first few are collections stored in MongoDB database.
   *
   * database - a MongoDatabase, which is the database to access the MongoDB database
   *
   * mapper - an ObjectMapper, which provides functionality for reading and writing JSON
   */
  private MongoCollection<Document> userCollection;
  private MongoCollection<Document> conventionCollection;
  private MongoCollection<Document> eventCollection;
  private MongoCollection<Document> conflictCollection;
  private MongoCollection<Document> attendeeCollection;
  private MongoDatabase database;
  private ObjectMapper mapper = new ObjectMapper();
  private static final int CONFLICT_MAX = 5000;

  /**
   * Constructor for DatabaseUtility.
   */
  public DatabaseUtility() {
    mapper.registerModule(new JavaTimeModule());

    // for unit testing purposes
    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017,"
              + "scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k."
              + "mongodb.net:27017/test?ssl=true&replicaSet=scheduler-shard-0&authSource="
              + "admin&retryWrites=true&w=majority");

      MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connString)
          .retryWrites(true).build();
      MongoClient mongo = MongoClients.create(settings);
      database = mongo.getDatabase("test");
      // created db in cluster in MongoDBAtlas including collections: users, events, conflicts
    } else {
      database = Main.getDatabase();
    }

    // initialize all collections
    userCollection = database.getCollection("users");
    eventCollection = database.getCollection("events");
    conflictCollection = database.getCollection("conflicts");
    conventionCollection = database.getCollection("conventions");
    attendeeCollection = database.getCollection("attendees");
  }

  /**
   * Method to check if user has access to a particular convention.
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
   * Get a list of events associated with a convention id from the database.
   *
   * @param conventionID - a String, which represents the ID of the convention from which we want
   *        events
   *
   * @return List of Events, which represents the events in the given convention
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
    for (Document event : eventList) {
      Event e = new Event(event.getInteger("id"), event.getString("name"),
          event.getString("description"));
      if (e.getID() != null) {
        result.add(e);
      }
    }
    return result;
  }

  /**
   * Adds the convention data (startDateTime, numDays, etc) to the database.
   *
   * @param convention - a Convention, which represents the Convention to be added. The Convention's
   *        ID should already be in the user collection of the database
   *
   * @return true if the given convention id already exist in the database and data was able to
   *         added; false if operation fails
   */
  public Boolean addConventionData(Convention convention) {
    Map<String, Object> conventionString = new HashMap<>();
    // fields to add
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
      System.err.println("ERROR: currently no events associated with the convention");
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
   * Add a conflicts to the database. Since we use an undirected graph, the given conflict is added
   * in both directions.
   *
   * @param conventionID - a String, which represents the ID of the convention from which we want
   *        events
   * @param newConflict - a Conflict, which represents a Conflict between two events. This conflict
   *        and its reverse (where the order of the events si swapped) are to be added
   *
   * @return a Boolean, true if the method was successful, false otherwise
   */
  public Boolean addConflict(String conventionID, Conflict newConflict) {

    Conflict reverseConflict = new Conflict(newConflict.getTail(), newConflict.getHead(),
        newConflict.getWeight());
    Gson gson = new Gson();

    // checking if the convention is in the database
    BasicDBObject cQuery = new BasicDBObject("id", conventionID);
    Document conventionDoc = conventionCollection.find(cQuery).first();

    if (conventionDoc == null) {
      System.err.println(newConflict + "could not be added due to convention not found.");
      return false;
    }

    BasicDBObject newConflictObj = BasicDBObject.parse(gson.toJson(newConflict));
    BasicDBObject reverseConflictObj = BasicDBObject.parse(gson.toJson(reverseConflict));
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionID);

    List<Document> conflictList = (List<Document>) conflictCollection.find(query)
        .projection(fields(include("conflicts"), excludeId()))
        .map(document -> document.get("conflicts")).first();

    // convention is not in the conflicts at all
    if (conflictList == null) {
      Map<String, Object> newConventionString = new HashMap<>();
      newConventionString.put("conventionID", conventionID);
      conflictCollection.insertOne(new Document(newConventionString));
      conflictList = new ArrayList<>();
    }

    List<BasicDBObject> dbConflictList = new ArrayList<>();
    boolean foundNormalMatch = false;
    boolean foundReverseMatch = false;

    for (Document conflictDoc : conflictList) {
      Document e1 = (Document) conflictDoc.get("event1");
      Event event1 = new Event(e1.getInteger("id"), e1.getString("name"),
          e1.getString("description"));
      Document e2 = (Document) conflictDoc.get("event2");
      Event event2 = new Event(e2.getInteger("id"), e2.getString("name"),
          e1.getString("description"));
      Conflict currConflict = new Conflict(event1, event2, conflictDoc.getInteger("weight"));

      if (currConflict.equals(newConflict)) { // matches with existing conflict
        currConflict.incrementWeight();
        foundNormalMatch = true;
      }

      if (currConflict.equals(reverseConflict)) { // matches with reversed conflict
        currConflict.incrementWeight();
        foundReverseMatch = true;
      }

      BasicDBObject obj1 = BasicDBObject.parse(gson.toJson(currConflict));
      dbConflictList.add(obj1);

    }

    if (foundNormalMatch && foundReverseMatch) { // found matches, need to update
      BasicDBObject andDuplicate = new BasicDBObject();
      andDuplicate.append("conventionID", conventionID);

      BasicDBObject update = new BasicDBObject();
      update.put("$set", new BasicDBObject("conflicts", dbConflictList));
      conflictCollection.updateOne(andDuplicate, update);
      return true;
    } else if (foundNormalMatch ^ foundReverseMatch) {
      System.err.println("ERROR: only found one of the conflict and the conflict's reverse");
      return false;
    } else { // out of for loop without finding a match, need to add it
      BasicDBObject update1 = new BasicDBObject();
      BasicDBObject update2 = new BasicDBObject();
      BasicDBObject queryAdd = new BasicDBObject("conventionID",
          new BasicDBObject("$eq", conventionID));

      update1.put("$push", new BasicDBObject("conflicts", newConflictObj));
      update2.put("$push", new BasicDBObject("conflicts", reverseConflictObj));
      conflictCollection.updateOne(queryAdd, update1);
      conflictCollection.updateOne(queryAdd, update2);

      return true;
    }

  }

  /**
   * Adds the convention ID to the database for the given user. If this ID is already used, this
   * method returns false and does not add the ID.
   *
   * @param userEmail - a String, which represents the email of the given convention's creator
   * @param conventionID - a String, which represents the ID of the convention from which we want
   *        events
   *
   * @return a Boolean, true if the ID was added, false otherwise (if the given convention id
   *         already exist in the database)
   */
  public Boolean addConvID(String userEmail, String conventionID) {
    // the key to this document is the convention id
    Document conventionExist = userCollection.find(all("conventions", conventionID)).first();

    if (conventionExist != null && !conventionExist.isEmpty()) {
      // there already exists a convention with the given id
      return false;
    }

    userCollection.updateOne(new Document("email", userEmail),
        new Document("$push", new Document("conventions", conventionID)));

    return true;
  }

  /**
   * Method to add a collaborator to a convention so that collaborator also has the ability to
   * modify the convention.
   *
   * @param userEmail - email for the collaborator
   * @param conventionID - conventionID to add
   *
   * @return - true if added, false otherwise
   */
  public Boolean addConvIDCollaborator(String userEmail, String conventionID) {
    Document document = userCollection
        .find(new BasicDBObject("email", new BasicDBObject("$eq", userEmail))).first();
    if (document == null) {
      return false; // user doesn't exist
    }

    // the key to this document is the convention id
    Document conventionExist = userCollection.find(all("conventions", conventionID)).first();

    // convention doesn't exist
    if (conventionExist == null) {
      return false;
    }

    userCollection.updateOne(new Document("email", userEmail),
        new Document("$push", new Document("conventions", conventionID)));

    return true;
  }

  /**
   * Method to get conflicts based on a convention id.
   *
   * @param conventionIDParam -- the id of the convention
   *
   * @return -- a Set of all the conflicts in this convention
   */
  public Set<Conflict> getConflictsFromConventionID(String conventionIDParam) {
    Set<Conflict> edges = new HashSet<>();
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionIDParam);

    // iterate through the events found
    List<Document> conflictList = (List<Document>) conflictCollection.find(query)
        .projection(fields(include("conflicts"), excludeId()))
        .map(document -> document.get("conflicts")).first();

    if (conflictList == null) {
      // there are no conflicts
      return new HashSet<>();
    }

    int conflictNum = Math.min(conflictList.size(), CONFLICT_MAX);
    // get all conflicts
    for (int i = 0; i < conflictNum; i++) {
      Document conflictDoc = conflictList.get(i);
      Document event1Doc = (Document) conflictDoc.get("event1");
      Document event2Doc = (Document) conflictDoc.get("event2");
      Integer weight = conflictDoc.getInteger("weight");
      Event e1 = new Event(event1Doc.getInteger("id"), event1Doc.getString("name"),
          event1Doc.getString("description"));
      Event e2 = new Event(event2Doc.getInteger("id"), event2Doc.getString("name"),
          event2Doc.getString("description"));
      Conflict c = new Conflict(e1, e2, weight);
      if (e1.getID() != null && e2.getID() != null) {
        edges.add(c);
      }
    }

    return edges;
  }

  /**
   * This method adds an event to a pre-existing convention with conventionID.
   *
   * @param conventionID - a String, which represents the ID of the convention from which we want
   *        events
   * @param newEvent - an Event, which represents the Event to add to this convention
   *
   * @return a Boolean, true if the Event was added (meaning given convention id exists and event
   *         was not duplicate), false otherwise
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

    // check if event is already there
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
      eventCollection.updateOne(eq("conventionID", conventionID), Updates.addToSet("events", obj));
      return true;
    } else {
      return false;
    }
  }

  /**
   * This method gets the convention data for a certain convention.
   *
   * @param conventionID - a String, which represents the ID of the convention from which we want
   *        events
   *
   * @return a Convention, which represents the Convention with the ID. This Convention will have
   *         all the fields filled in.
   */
  public Convention getConvention(String conventionID) {
    BasicDBObject query = new BasicDBObject();
    query.put("id", conventionID);
    Document doc = conventionCollection.find(query).first();
    if (doc == null) {
      return null;
    }

    // create the convention object to return
    String id = doc.getString("id");
    String name = doc.getString("name");
    Date sdtDateTime = (Date) doc.get("startDateTime");
    Date et = (Date) doc.get("endDateTime");
    ZoneOffset offset = ZoneOffset.ofHours(0);
    LocalDateTime ldt = sdtDateTime.toInstant().atZone(ZoneId.ofOffset("GMT", offset))
        .toLocalDateTime();

    int numDays = doc.getInteger("numDays");
    int eventDuration = doc.getInteger("eventDuration");
    LocalDateTime endDateTime = et.toInstant().atZone(ZoneId.ofOffset("GMT", offset))
        .toLocalDateTime();

    return new Convention(id, name, ldt, numDays, eventDuration, endDateTime);
  }

  /**
   * Gets all the conventions of the User.
   *
   * @param userEmail - a String, which represents the email of the user whose conventions we want
   *
   * @return a List of Conventions, which represents all conventions the user manages
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

  /**
   * This method adds an attendee email to a pre-existing convention with conventionID.
   *
   * @param conventionID - a String, which represents the ID of the convention from which we want
   *        events
   * @param attendeeEmail - a String, which represents the attendee email to add
   *
   * @return a Boolean, true if the given convention id exists and the email was added, false
   *         otherwise
   */
  public Boolean addAttendeeEmail(String conventionID, String attendeeEmail) {
    Gson gson = new Gson();
    // the key to this document is the convention id
    BasicDBObject query = new BasicDBObject();
    Document conventionExist = userCollection.find(all("conventions", conventionID)).first();

    if (conventionExist == null || conventionExist.isEmpty()) {
      return false; // there are no conventions with the given id
    }

    BasicDBObject equery = new BasicDBObject("conventionID", conventionID);
    Document findConvInAttendee = attendeeCollection.find(equery).first();
    Document findIterable = attendeeCollection.find(eq("attendeeEmail", attendeeEmail)).first();
    Map<String, Object> newConventionString = new HashMap<>();

    if ((findConvInAttendee == null || findConvInAttendee.isEmpty())
        && (findIterable == null || findIterable.isEmpty())) {
      // this convention ID is not yet in the attendeeCollection
      List<String> emailArray = new ArrayList<>();
      emailArray.add(attendeeEmail);
      newConventionString.put("conventionID", conventionID);
      newConventionString.put("attendees", emailArray);
      attendeeCollection.insertOne(new Document(newConventionString));
      return true;

    } else if (findIterable == null || findIterable.isEmpty()) {
      // the convention ID is already in the attendee collection, and we just want to add to its
      // emails
      attendeeCollection.updateOne(eq("conventionID", conventionID),
          Updates.addToSet("attendees", attendeeEmail));
      return true;
    } else {
      return false;
    }
  }

  /**
   * Get a list of attendee emails associated with a convention id from the database.
   *
   * @param conventionID a String, which represents the ID of the convention from which we want
   *        events
   *
   * @return List of Strings, which represent the emails of attendees who wish to be notified about
   *         the schedule for the given convention
   */
  public List<String> getAttendeeEmailsFromConventionID(String conventionID) {
    List<String> emails = new ArrayList<String>();
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionID);
    Document doc = attendeeCollection.find(query).first();

    if (doc == null) {
      // there are currently no emails with this ID
      return new ArrayList<>();
    }

    // iterate through the events found
    List<String> emailList = (List<String>) doc.get("attendees");

    for (String email : emailList) {
      emails.add(email);
    }
    return emails;
  }

  /**
   * This method is used to update event IDs in a convention.
   *
   * @param conventionID - a String, which represents the ID of the convention to modify
   *
   * @return a Map of Integers to Integers (original unordered id to order id)
   */
  public Map<Integer, Integer> updateEventID(String conventionID) {
    List<BasicDBObject> eventArray = new ArrayList<>();
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionID);
    Document doc = eventCollection.find(query).first();
    List<Document> eventList = (List<Document>) doc.get("events");
    int eventID = 0;
    Gson gson = new Gson();
    HashMap<Integer, Integer> fixID = new HashMap<>();
    for (Document event : eventList) {
      Event e = new Event(eventID, event.getString("name"), event.getString("description"));
      fixID.put(event.getInteger("id"), eventID);
      eventID++;
      BasicDBObject obj1 = BasicDBObject.parse(gson.toJson(e));
      eventArray.add(obj1);
    }

    BasicDBObject update = new BasicDBObject();
    update.put("$set", new BasicDBObject("events", eventArray));
    eventCollection.updateOne(query, update);

    return fixID;
  }

  /**
   * This method is used to update the conflicts in a convention.
   *
   * @param conventionID - a String, which represents the ID of the convention to modify
   * @param map - a Map of Integers to Integers
   *
   * @return a Boolean, true if the method was successful
   */
  public Boolean updateConflict(String conventionID, Map<Integer, Integer> map) {
    List<BasicDBObject> conflictArray = new ArrayList<>();
    BasicDBObject query = new BasicDBObject();
    query.put("conventionID", conventionID);
    Document doc = conflictCollection.find(query).first();
    Gson gson = new Gson();
    List<Document> conflicts = (List<Document>) doc.get("conflicts");
    for (Document d : conflicts) {
      // change event id according to mapping
      Document e1 = (Document) d.get("event1");
      Integer newID1 = map.get(e1.getInteger("id"));

      Event event1 = new Event(newID1, e1.getString("name"), e1.getString("description"));

      Document e2 = (Document) d.get("event2");
      Integer newID2 = map.get(e2.getInteger("id"));

      Event event2 = new Event(newID2, e2.getString("name"), e1.getString("description"));
      Conflict conflict = new Conflict(event1, event2, d.getInteger("weight"));
      BasicDBObject obj1 = BasicDBObject.parse(gson.toJson(conflict));
      conflictArray.add(obj1);
    }
    // update the conflict if it exists
    BasicDBObject update = new BasicDBObject();
    update.put("$set", new BasicDBObject("conflicts", conflictArray));
    conflictCollection.updateOne(query, update);
    return true;
  }
}
