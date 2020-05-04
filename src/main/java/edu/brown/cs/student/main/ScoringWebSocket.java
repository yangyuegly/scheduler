package edu.brown.cs.student.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Class to manage collaboration
 */
@WebSocket
public class ScoringWebSocket {
  private static final Gson GSON = new Gson();
  private static Map<String, List<Session>> sessions = new ConcurrentHashMap<>();
  private static int nextId = 0;
  static Map<String, String> map = new ConcurrentHashMap<>();

  private static enum MESSAGE_TYPE {
    CONNECT, EVENT, UPDATE
  }



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
    JsonObject message = new JsonObject();
    message.addProperty("type", MESSAGE_TYPE.CONNECT.ordinal());
    JsonObject payload = new JsonObject();
    payload.addProperty("id", nextId);
    payload.addProperty("text", payloadText);
    message.add("payload", payload);
    session.getRemote().sendString(GSON.toJson(message));
    nextId++;
  }

  @OnWebSocketError
  public void onWebSocketError(Throwable e) {
    System.out.println(e);
  }

  @OnWebSocketClose
  public void closed(Session session, int statusCode, String reason) {
    sessions.remove(session);
  }

  @OnWebSocketMessage
  public synchronized void message(Session session, String message) throws IOException {
    String currURI = session.getUpgradeRequest().getRequestURI().toString();
    JsonObject received = GSON.fromJson(message, JsonObject.class);
    assert received.get("type").getAsInt() == MESSAGE_TYPE.EVENT.ordinal();

    JsonObject payload = received.get("payload").getAsJsonObject();

    JsonObject toSend = new JsonObject();
    toSend.addProperty("type", MESSAGE_TYPE.UPDATE.ordinal());
    JsonObject newPayload = new JsonObject();
    newPayload.add("id", payload.get("id"));

    String currString = map.get(currURI);
    currString = payload.get("text").getAsString();
    map.put(currURI, currString);
    newPayload.addProperty("text", currString);

    toSend.add("payload", newPayload);

    for (Map.Entry<String, String> m : map.entrySet()) {
      System.out.println(m.getKey() + " " + m.getValue());
    }
    String toSendStr = GSON.toJson(toSend);
    for (Session s : sessions.get(currURI)) {
      s.getRemote().sendString(toSendStr);
    }
  }
}