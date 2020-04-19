package edu.brown.cs.student.scheduler;

import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.exception.UserAuthenticationException;
import edu.brown.cs.student.main.Main;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.ServerAddress;
import com.mongodb.MongoCredential;


import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
public class LoginCommand {

  public static void execute(String email, String password) {        
    MongoCollection<org.bson.Document> userCollection = Main.getDatabase().getCollection("users");
    Document currUser = userCollection.find(eq("email", email)).first();
    System.out.println(currUser.toJson());
    if (currUser.isEmpty()) {
      throw new UserAuthenticationException("User does not exist on system");
    }
    else {
      if (!password.equals(currUser.get("password"))) {
        throw new UserAuthenticationException("User authentication failed; password mismatch");
      }
    }
  }
  
}