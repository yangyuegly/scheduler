package edu.brown.cs.student.scheduler;

import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.exception.UserAuthenticationException;
import edu.brown.cs.student.main.ICommand;
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

import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;

import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;

public class LoginCommand {
  
  private static final String UNICODE_FORMAT = "UTF8";
  public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
  private KeySpec ks;
  private SecretKeyFactory skf;
  private Cipher cipher;
  byte[] arrayBytes;
  private String myEncryptionKey;
  private String myEncryptionScheme;
  SecretKey key;

  public void execute(String email, String password) {        
    MongoCollection<org.bson.Document> userCollection = Main.getDatabase().getCollection("users");
    Document currUser = userCollection.find(eq("email", email)).first();
    System.out.println(currUser.toJson());
    if (currUser.isEmpty()) {
      throw new UserAuthenticationException("User does not exist on system");
    }
    else {
      String encryptedPassword = currUser.getString("encryptedPassword");
      byte[] salt = currUser.("encryptedPassword");
      if (!password.equals()) {
        throw new UserAuthenticationException("User authentication failed; password mismatch");
      }
    }
  }

  public String decryptePassword(String userInputPassword) {
                
            // decrypt the text
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(encrypted));
            System.err.println(decrypted);
  }

  

  
}