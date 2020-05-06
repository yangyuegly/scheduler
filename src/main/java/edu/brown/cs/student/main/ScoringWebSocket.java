package edu.brown.cs.student.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Class used to update the convention home page with new events from other windows or
 * collaborators.
 */
@WebSocket
public class ScoringWebSocket {
  private static final Gson GSON = new Gson();
  private static Map<String, List<Session>> sessions = new ConcurrentHashMap<>();
  private static int nextId = 0;
  private static Map<String, String> map = new ConcurrentHashMap<>();

  /**
   * This is an enum. It is used to represent the type of socket message being sent.
   */
  private enum MESSAGETYPE {
    CONNECT, EVENT, UPDATE
  }

  /**
   * This method occurs when a connect message is sent.
   *
   * @param session - a Session
   *
   * @throws IOException if there is a problem related to input and output
   */
  @OnWebSocketConnect
  public void connected(Session session) throws IOException {
    String currURI = session.getUpgradeRequest().getRequestURI().toString();
    String payloadText = "";
    System.out.println("connected");
    if (!map.containsKey(currURI)) {
      map.put(currURI, "");
    } else {
      payloadText = map.get(currURI);
    }
    List<Session> currConvention = sessions.getOrDefault(currURI, new ArrayList<Session>());
    currConvention.add(session);
    sessions.put(currURI, currConvention);

    System.err.println("currURI " + currURI + " added to sessions"); // delete

    JsonObject message = new JsonObject();
    message.addProperty("type", MESSAGETYPE.CONNECT.ordinal());
    JsonObject payload = new JsonObject();
    payload.addProperty("id", nextId);
    payload.addProperty("text", payloadText);
    message.add("payload", payload);
    session.getRemote().sendString(GSON.toJson(message));
    nextId++;
  }

  /**
   * This method is used when there is a socket error.
   *
   * @param e - a Throwable
   */
  @OnWebSocketError
  public void onWebSocketError(Throwable e) {
    System.out.println(e);
  }

  /**
   * This method is used when the socket is closed.
   *
   * @param session - a Session
   * @param statusCode - an int, which represents the status code of the closure
   * @param reason - a String, which represents the reason the socket closed
   */
  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    String currURI = session.getUpgradeRequest().getRequestURI().toString();
    List<Session> sessionsForURI = sessions.get(currURI);

    if (sessionsForURI == null) {
      System.err.println("ERROR: URI is not in sessions when closing");
    }

    // remove the session from the Map
    sessionsForURI.remove(session);
    sessions.replace(currURI, sessionsForURI);
  }

  /**
   * This method is used when a message is sent over the socket.
   *
   * @param session - a Session
   * @param message - a String, which represents the message that was sent
   *
   * @throws IOException if an error occurs
   */
  @OnWebSocketMessage
  public synchronized void message(Session session, String message) throws IOException {
    String currURI = session.getUpgradeRequest().getRequestURI().toString();
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGETYPE.EVENT.ordinal();

    JsonObject payload = received.get("payload").getAsJsonObject();

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGETYPE.UPDATE.ordinal());
    JsonObject newPayload = new JsonObject();
    newPayload.add("id", payload.get("id"));

    // receive data from front end
    String currString = map.get(currURI);
    currString = payload.get("text").getAsString();
    map.put(currURI, currString);
    newPayload.addProperty("text", currString);

    toSend.add("payload", newPayload);

    for (Map.Entry<String, String> m : map.entrySet()) {
      System.out.println(m.getKey() + " " + m.getValue());
    }
    String toSendStr = GSON.toJson(toSend);

    List<Session> sessionsForURI = sessions.get(currURI);

    if (sessionsForURI == null) {
      System.err.println("currURI " + currURI + " is not in sessions");
    } else {
      for (Session s : sessionsForURI) {
        RemoteEndpoint remote = s.getRemote();
        remote.sendString(toSendStr);
      }
    }
  }
}
