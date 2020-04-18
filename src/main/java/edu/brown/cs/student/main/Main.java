package edu.brown.cs.student.main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
// import com.mongodb.MongoClientOptions;

import edu.brown.cs.student.gui.AccountHomeHandler;
import edu.brown.cs.student.gui.ConventionHomeHandler;
import edu.brown.cs.student.gui.CreateAccountHandler;
import edu.brown.cs.student.gui.CreateAccountSubmitHandler;
import edu.brown.cs.student.gui.CreateConvSubmitHandler;
import edu.brown.cs.student.gui.CreateConventionHandler;
import edu.brown.cs.student.gui.CreateExamConvHandler;
import edu.brown.cs.student.gui.HomeHandler;
import edu.brown.cs.student.gui.LoginHandler;
import edu.brown.cs.student.gui.SaveConventionHandler;
import edu.brown.cs.student.gui.SchedExamSubmitHandler;
import edu.brown.cs.student.gui.UploadHandler;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 *
 */
public final class Main {

  /**
   * The following are fields for this class.
   *
   * DEFAULT_PORT - an int, which represents the default port of this program
   *
   */
  private static final int DEFAULT_PORT = 4567;

  MongoClient mongo = MongoClients.create();
  

  // Creating Credentials
  // private MongoCredential credential;


  //Accessing the database
  static MongoDatabase database;
  // field for each command

  // Is this where these go? concurrency??
//  LoadCommand loadCommand;
//  LoginCommand loginCommand;
//  ScheduleCommand schedCommand;
//

  /**
   * The initial method called when execution begins.
   *
   * @param args - an array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  public static MongoDatabase getDatabase() {
    return Main.database;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
    .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

//     credential = MongoCredential.createCredential("sampleUser", "myDb",
//         "password".toCharArray());
//    database = mongo.getDatabase("myDb");
    //    System.out.println("created db?");
//    database.createCollection("events");
//    database.createCollection("conflicts");
//    System.out.println("created conflicts?");
//    database.createCollection("users");
//    System.out.println("created users?");
//    database.createCollection("conflicts");
//    database.createCollection("users");

//    WebScraper web = new WebScraper();
//    web.setCollege("clemson");
//    web.scrape();

    // create new objects to assist with running the program
    // initialize commands

    Map<String, ICommand> commands = new HashMap<>();
    // add commmand objects to commands Map

    CommandManager manager = new CommandManager(commands);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));

    }
    
  //allowing us to upload a file ???????????????????????????????????????????????????????????????????
    File uploadDir = new File("uploaded-file");
    uploadDir.mkdir();

    manager.run();
    
    
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    System.out.println("port");
    Spark.port(port);
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();

    // Setup Spark Routes
    //Spark.get("/stars", new StarsFrontHandler(), freeMarker);
    // handlers and such
    Spark.get("/home", new HomeHandler(), freeMarker);
    Spark.get("/create_account", new CreateAccountHandler(), freeMarker);
    Spark.post("/create_account", new CreateAccountSubmitHandler(), freeMarker);
    Spark.get("/account", new AccountHomeHandler(), freeMarker);
    Spark.post("/account", new LoginHandler(), freeMarker);
    Spark.get("/create_convention", new CreateConventionHandler(), freeMarker);
    Spark.post("/create_convention/:id", new CreateConvSubmitHandler(), freeMarker);
    Spark.post("/save_convention", new SaveConventionHandler());
    Spark.get("/create_exam_conv", new CreateExamConvHandler(), freeMarker);
    Spark.post("/exam_schedule", new SchedExamSubmitHandler(), freeMarker);
    Spark.post("/upload_convention/:id", new UploadHandler(), freeMarker);
    Spark.get("/convention/:id", new ConventionHomeHandler(), freeMarker);


  }

  // Know who's attending? Upload a file with everything or add them manually.
  // Don't know? Send out a form to find out.


  /**
   * Display an error page when an exception occurs in the server.
   *
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(500);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
