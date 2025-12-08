package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ArrayList<Connection>> games = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {
        var connection = new Connection(authToken, session);
        ArrayList<Connection> existingConnections;
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
        ArrayList<Connection> connections;
        if (games.containsKey(gameID)) {
            connections = games.get(gameID);
            for (Connection c : connections) {
                if (c.authToken.equals(authToken)) {
                    connections.remove(c);
                    break;
                }
            }
        }
    }

    public void sendConnectionFailure(String authToken, Session session, ServerMessage message) {
        try {
            Gson gson = new Gson();
            String jsonMessage = gson.toJson(message);
            new Connection(authToken, session).sendMessage(jsonMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendToRoot(int gameID, String authToken, ServerMessage message) {
        try {
            ArrayList<Connection> connections = games.get(gameID);
            for (Connection c : connections) {
                if (c.session.isOpen()) {
                    if (c.authToken.equals(authToken)) {
                        Gson gson = new Gson();
                        String jsonMessage = gson.toJson(message);
                        c.sendMessage(jsonMessage);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcast(int gameID, String excludeAuthToken, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        ArrayList<Connection> connections = games.get(gameID);
        for (Connection c : connections) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    Gson gson = new Gson();
                    String jsonMessage = gson.toJson(notification);
                    c.sendMessage(jsonMessage);
                } else {
                    removeList.add(c);
                }
            }
        }
//        clean up, clean up, everybody everywhere
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public void sendToAll(int gameID, ServerMessage message) throws IOException {
        var removeList = new ArrayList<Connection>();
        ArrayList<Connection> connections = games.get(gameID);
        for (Connection c : connections) {
            if (c.session.isOpen()) {
                Gson gson = new Gson();
                String jsonMessage = gson.toJson(message);
                c.sendMessage(jsonMessage);
            } else {
                removeList.add(c);
            }
        }
        // Clean up again
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }
}