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

import java.security.Key;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

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
  private String myEncryptionKey;
  private String myEncryptionScheme;
  SecretKey key;

  public Boolean execute(String email, String password) {        
    MongoCollection<org.bson.Document> userCollection = Main.getDatabase().getCollection("users");
    System.out.println(userCollection);
    Document currUser = userCollection.find(eq("email", email)).first();
    System.out.println(currUser.toJson());
    if (currUser.isEmpty()) {
      throw new UserAuthenticationException("User does not exist on system");
    }
    else {
      String encryptedPassword = currUser.getString("encryptedPassword");
      String salt = currUser.getString("salt");
      String decryptionResult = decryptePassword(encryptedPassword, salt);
      if (!password.equals(decryptionResult)) {
        throw new UserAuthenticationException("User authentication failed; password mismatch");
      }
    }
    return true; 
  }

  public String decryptePassword(String userInputPassword, String salt) {

    //convert bytes to string
    byte[] saltDecoded = Base64.getDecoder().decode(salt);

    //convert user password to string
    byte[] bb = new byte[userInputPassword.length()];
    for (int i = 0; i < userInputPassword.length(); i++) {
      bb[i] = (byte) userInputPassword.charAt(i);
    }
    
    // decrypt the text
    try {
    Key aesKey = new SecretKeySpec(saltDecoded, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, aesKey);
    String decrypted = new String(cipher.doFinal(bb));
    return decrypted;
  } catch (Exception e) {
    throw new NullPointerException("Unable to decrypte password");

    }
  }

  

  
}