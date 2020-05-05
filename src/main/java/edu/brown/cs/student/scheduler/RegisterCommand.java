package edu.brown.cs.student.scheduler;

import java.lang.System.Logger;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import edu.brown.cs.student.exception.UserAuthenticationException;
import edu.brown.cs.student.main.Main;

/**
 * Command that registers the user
 */
public class RegisterCommand {

  /**
   * These are fields for this class.
   */
  public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
  private SecretKey key;
  private static Logger logger;

  /**
   * Method that registers a user.
   *
   * @param email - user email
   * @param password - user password
   *
   * @return - true if successful, false otherwise
   *
   * @throws UserAuthenticationException otherwise
   */
  public boolean execute(String email, String password) throws UserAuthenticationException {
    BasicDBList list = new BasicDBList();
    byte[] salt = getSalt();
    String encryptedPassword = encrypt(password, salt);

    String saltToString = Base64.getEncoder().encodeToString(salt);// convert salt to string

    MongoCollection<org.bson.Document> userCollection;
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
      // created db in cluster in MongoDBAtlas including collections: users, events, conflicts
      MongoDatabase database = mongo.getDatabase("test");
      userCollection = database.getCollection("users");
    } else {
      userCollection = Main.getDatabase().getCollection("users");
    }
    BasicDBObject query = new BasicDBObject("email", new BasicDBObject("$eq", email));
    Document currUser = userCollection.find(query).first();

    // user already exists
    if (!((currUser == null) || currUser.isEmpty())) {
      throw new UserAuthenticationException("User already exists on system");
    }
    Document user = new Document("email", email).append("encryptedPassword", encryptedPassword)
        .append("salt", saltToString).append("conventions", list);
    userCollection.insertOne(user);
    return true;
  }

  /**
   * Encrypt the user's password.
   *
   * @param password - password user input
   * @param salt - salt String
   *
   * @return a String, which represents the encrypted password
   */
  private static String encrypt(String password, byte[] salt) {
    try {
      // Create key and cipher
      Key aesKey = new SecretKeySpec(salt, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      // encrypt the text
      cipher.init(Cipher.ENCRYPT_MODE, aesKey);
      byte[] encrypted = cipher.doFinal(password.getBytes());
      System.err.println(new String(encrypted));

      StringBuilder sb = new StringBuilder();
      for (byte b : encrypted) {
        sb.append((char) b);
      }
      // the encrypted String
      String encryptedPw = sb.toString();

      return encryptedPw;
    } catch (Exception e) {
      e.printStackTrace();
      throw new NullPointerException("Unable to encrypte password");
    }
  }

  /**
   * This method creates a random salt to be used for encryption.
   *
   * @return an array of bytes, which represents a salt
   */
  private static byte[] getSalt() {
    byte[] salt = new byte[16];// bytes to be filled
    SecureRandom sr = new SecureRandom(); // secureRandom number
    sr.nextBytes(salt);// fill bytes
    return salt;
  }

}
