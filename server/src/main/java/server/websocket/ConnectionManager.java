package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String username, Session session) {
    var connection = new Connection(username, session);
    connections.put(username, connection);
  }
  public void sendToRoot(String authToken, ServerMessage message){
    try {
      var removeList=new ArrayList<Connection>();
      for (var c : connections.values()) {
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

  public void broadcast(String excludeAuthToken, ServerMessage notification) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
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

  public void sendToAll(ServerMessage message) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
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