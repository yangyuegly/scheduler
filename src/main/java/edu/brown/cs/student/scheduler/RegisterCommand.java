package edu.brown.cs.student.scheduler;

import com.mongodb.client.MongoCollection;

import edu.brown.cs.student.accounts.User;
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
import java.util.Base64;
import org.bson.Document;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.Object;
import com.mongodb.BasicDBList;
import com.mongodb.Block;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import static java.util.Base64.

public class RegisterCommand {

  private static final String UNICODE_FORMAT = "UTF8";
  public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
  private KeySpec ks;
  private SecretKeyFactory skf;
  private Cipher cipher;
  byte[] arrayBytes;
  private String myEncryptionKey;
  private String myEncryptionScheme;
  SecretKey key;



  public static void execute(String email, String password) {
    BasicDBList list = new BasicDBList();
    byte[] salt = getSalt();
    String encryptedPassword = encrypt(password, salt);

    String saltToString = Base64.getEncoder().encodeToString(salt);//convert salt to string

    MongoCollection<Document> userCollection = Main.getDatabase().getCollection("users");
    Document user = new Document("email", email).append("encryptedPassword", encryptedPassword)
    .append("salt", saltToString)
    .append("conventions",
        list);
    userCollection.insertOne(user);
  }
  
  private static String encrypt(String password, byte[] salt){
    try{

            // Create key and cipher
            Key aesKey = new SecretKeySpec(salt, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            // encrypt the text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            System.err.println(new String(encrypted));
            
            StringBuilder sb = new StringBuilder();
            for (byte b: encrypted) {
                sb.append((char)b);
            }
            // the encrypted String
            String encryptedPw = sb.toString();
            System.out.println("encrypted:" + encryptedPw);
            
            return encryptedPw; 
        } catch(Exception e) {
          e.printStackTrace();
          throw new NullPointerException("Unable to encrypte password");
        }
  }

  private static byte[] getSalt() {
    byte[] salt = new byte[16];//bytes to be filled
    SecureRandom sr = new SecureRandom(); // secureRandom number
    sr.nextBytes(salt);// fill bytes
    return salt; 
  }


}
