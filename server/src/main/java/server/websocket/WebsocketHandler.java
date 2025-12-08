package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebsocketHandler {
    private final Gson gson = new Gson();
    private final ConnectionManager connectionManager = new ConnectionManager();
    private Session session;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        this.session = session;
    }

    private void leaveGame(String authToken, UserGameCommand command) {
    }

    private void resign(String authToken, UserGameCommand command) {
    }

    private void connect(String authToken, UserGameCommand command) {
    }

    private void makeMove(String authToken, UserGameCommand command) {
    }
}
