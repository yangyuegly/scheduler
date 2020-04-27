package edu.brown.cs.student.main;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bson.Document;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

/**
 * Class to manage collaboration
 */
@WebSocket
public class ScoringWebSocket {
  private static final Gson GSON = new Gson();
  private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
  private static int nextId = 0;

  private static enum MESSAGE_TYPE {
    CONNECT,
    EVENT,
    UPDATE
  }

  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    System.out.println("Connect: " + session.getRemoteAddress().getAddress());
    sessions.add(session);
    JsonObject message = new JsonObject();
    message.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    JsonObject payload = new JsonObject();
    payload.addProperty("id", nextId);
    message.add("payload", payload);
    session.getRemote().sendString(GSON.toJson(message));
    nextId++;
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public void message(Session session, String message) throws IOException {
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.EVENT.ordinal();

    JsonObject payload = received.get("payload").getAsJsonObject();

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());
    JsonObject newPayload = new JsonObject();
    newPayload.add("id", payload.get("id"));

    // //get events from conventionID
    // MongoCollection<Document> eventCollection = Main.getDatabase().getCollection("events");
    // BasicDBObject query = new BasicDBObject();
    // query.put("conventionID", payload.get("conventionID").getAsString());
    // Document doc = eventCollection.find(query).first();

    //iterate through the events found
    // String score = "";
    // BasicDBList eventList = (BasicDBList)doc.get("events");
    // for (int i = 0; i < eventList.size(); i++) {
    //   BasicDBObject eventObj = (BasicDBObject) eventList.get(i);
    //   score = score + " " + eventObj.getString("name");

    // }

    newPayload.addProperty("text", payload.get("text").getAsString());

    toSend.add("payload", newPayload);

    String toSendStr = GSON.toJson(toSend);
    for (Session s : sessions) {
      s.getRemote().sendString(toSendStr);
    }
  }
}
