package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;

public class WebSocketClient extends Endpoint {
    public Session session;
    public NotificationHandler notificationHandler;

    public WebSocketClient(NotificationHandler notificationHandler) throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        this.notificationHandler = notificationHandler;

        WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
        this.session = webSocketContainer.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                Gson gson = new Gson();
                ServerMessage notification = gson.fromJson(message, ServerMessage.class);
                notificationHandler.notify(notification);
            }
        });
    }

    public void joinGame(String authToken, int gameID, String color) throws Exception {
        try {
            if (color == null) {
                color = "OBSERVE";
            }
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception("Failed to join game");
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws Exception {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception("Failed to leave game");
        }
    }

    public void resign(String authToken, int gameID) throws Exception {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception("Could not resign");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
