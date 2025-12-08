package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

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
            existingConnections = new ArrayList<>();
            existingConnections.add(connection);
        }
        games.put(gameID, existingConnections);
    }

    public void remove(int gameID, String authToken) {
    }

    public void sendConnectionFailure(String authToken, Session session, ServerMessage message) {
    }

    public void sendToRoot(int gameID, String authToken, ServerMessage message) {
    }

    public void broadcast(int gameID, String excludeAuthToken, ServerMessage notification) {
    }

    public void sendToAll(int gameID, ServerMessage message) {
    }

}
