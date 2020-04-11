package edu.brown.cs.student.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import edu.brown.cs.student.accounts.User;
import edu.brown.cs.student.database.Database;
import edu.brown.cs.student.scheduler.Convention;

/**
 * This class is used to explicitly reference the loaded database.
 */
public class Database {

  /**
   * These are fields for this class.
   *
   * filename - a String, which represents the name of the SQL file this class accesses
   *
   * conn - a Connection, which is used to access the SQL file
   */
  private String filename;
  private static Connection conn = null;
  
  /**
   * Instantiates the database. Automatically loads files.
   *
   * @param dbFilename - a String representing the file name of SQLite3 database to open.
   *
   * @throws SQLException if an error occurs in any SQL query.
   */
  public Database(String dbFilename) throws SQLException {
    filename = dbFilename;

    try {
      Class.forName("org.sqlite.JDBC");
      String urlToDB = "jdbc:sqlite:" + filename;
      Database.conn = DriverManager.getConnection(urlToDB);
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
    } catch (ClassNotFoundException e) {
      System.err.println("ERROR: Class not found");
    }
  }
  /** Checks if a login is valid.
   * @param username
   * @param password
   * @return -- the user if they exist, null otherwise
   */
  public User checkLogin(String username, String password) {
    return null;
  }
  
  /**
   * Given a username, returns a list of all the convention names and ids associated with the user.
   * @param username
   * @return -- a list of the name and id of the convention
   */
  public List<String[]> getConventionsNamesIds(String username){
    return null;
  }
  /**
   * Given a convention id, returns a complete Convention object with all of the events, etc.
   * @param convID
   * @return
   */
  public Convention makeConvention(String convID) {
    return null;
  }
  

}
