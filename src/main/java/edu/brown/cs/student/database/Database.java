package edu.brown.cs.student.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import edu.brown.cs.student.database.Database;

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

}
