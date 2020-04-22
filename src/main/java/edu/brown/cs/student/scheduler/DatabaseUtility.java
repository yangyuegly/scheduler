package edu.brown.cs.student.scheduler;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;

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
}
