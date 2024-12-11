package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<Integer, List<Connection>> games = new ConcurrentHashMap<>();

  public void add(int gameID, String authToken, Session session) {
    var connection = new Connection(authToken, session);
    List existingConnections = new ArrayList();
    if (games.containsKey(gameID)) {
      existingConnections = games.get(gameID);
      existingConnections.add(connection);
    } else {
      existingConnections = new ArrayList();
      existingConnections.add(connection);
    }
    games.put(gameID, existingConnections);
  }
  public void remove(int gameID, String authToken) {
    List<Connection> connections;
    if (games.containsKey(gameID)) {
      connections=games.get(gameID);
      for (Connection c : connections) {
          if (c.authToken.equals(authToken)) {
            connections.remove(c);
            break;
          }
      }
    }
  }

  public void sendNoConnect(String authToken, Session session, ServerMessage message) {
    try {
      new Connection(authToken, session).send(message.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void sendToRoot(int gameID, String authToken, ServerMessage message){
    try {
      var removeList=new ArrayList<Connection>();
      List<Connection> connections = games.get(gameID);
      for (Connection c : connections) {
        if (c.session.isOpen()) {
          if (c.authToken.equals(authToken)) {
            c.send(message.toString());
            break;
          }
        } else {
          removeList.add(c);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void broadcast(int gameID, String excludeAuthToken, ServerMessage notification) throws IOException {
    var removeList=new ArrayList<Connection>();
    List<Connection> connections = games.get(gameID);
    for (Connection c : connections) {
      if (c.session.isOpen()) {
        if (!c.authToken.equals(excludeAuthToken)) {
          c.send(notification.toString());
        }
      } else {
        removeList.add(c);
      }
    }
    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.authToken);
    }
  }

  public void sendToAll(int gameID, ServerMessage message) throws IOException {
    var removeList=new ArrayList<Connection>();
    List<Connection> connections = games.get(gameID);
    for (Connection c : connections) {
      if (c.session.isOpen()) {
        c.send(message.toString());
      } else {
        removeList.add(c);
      }
    }
    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.authToken);
    }
  }
}