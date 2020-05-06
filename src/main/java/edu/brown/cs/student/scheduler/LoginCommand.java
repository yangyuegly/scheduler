package edu.brown.cs.student.scheduler;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.bson.Document;

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
 * Method that carries out the login.
 */
public class LoginCommand {

  /**
   * Method that logs the user in.
   *
   * @param email - user email
   * @param password - user password
   *
   * @return - true is successfully logged in and UserAuthenticationError if not
   */
  public Boolean execute(String email, String password) {
    MongoCollection<org.bson.Document> userCollection;
    // for unit testing purposes
    if (Main.getDatabase() == null) {
      ConnectionString connString = new ConnectionString(
          "mongodb://sduraide:cs32scheduler@scheduler-shard-00-00-rw75k.mongodb.net:27017"
              + ",scheduler-shard-00-01-rw75k.mongodb.net:27017,scheduler-shard-00-02-rw75k."
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

    if ((currUser == null) || currUser.isEmpty()) {
      throw new UserAuthenticationException("User does not exist on system");
    } else {
      String encryptedPassword = currUser.getString("encryptedPassword");
      String salt = currUser.getString("salt");
      String decryptionResult = decryptePassword(encryptedPassword, salt);
      if (!password.equals(decryptionResult)) {
        throw new UserAuthenticationException("User authentication failed; password mismatch");
      }
    }
    return true;
  }

  /**
   * Method to decrypt the password.
   *
   * @param userInputPassword - password user input
   * @param salt - salt string
   *
   * @return a String, which represents the decrypted password
   */
  public String decryptePassword(String userInputPassword, String salt) {

    // convert bytes to string
    byte[] saltDecoded = Base64.getDecoder().decode(salt);

    // convert user password to string
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
