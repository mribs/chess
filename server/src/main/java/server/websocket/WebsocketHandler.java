package server.websocket;

import com.google.gson.Gson;
import dataaccess.sql.*;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

@WebSocket
public class WebsocketHandler implements WsCloseHandler, WsConnectHandler, WsMessageHandler {
    private final Gson gson = new Gson();
    private final ConnectionManager connectionManager = new ConnectionManager();
    private Session session;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        this.session = session;
        try {
            UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
            String authToken = command.getAuthToken();

            switch (command.getCommandType()) {
                case CONNECT -> connect(authToken, command);
                case LEAVE -> leaveGame(authToken, command);
                case RESIGN -> resign(authToken, command);
                case MAKE_MOVE -> makeMove(authToken, command);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void leaveGame(String authToken, UserGameCommand command) {
    }

    private void resign(String authToken, UserGameCommand command) {
    }

    private void connect(String authToken, UserGameCommand command) {
        try {
            connectionManager.add(command.getGameID(), authToken, session);
            AuthData authData = new SQLAuthDAO().getAuth(authToken);
            String username = authData.username();
            String color = command.getColor();
            String message = String.format("%s has joined the game as %s", username, color);
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(command.getGameID());
            if (game != null) {
                ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
                connectionManager.sendToRoot(command.getGameID(), authToken, loadGameMessage);
                connectionManager.broadcast(command.getGameID(), authToken, notification);
            } else {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: game cannot be found");
                connectionManager.sendToRoot(command.getGameID(), authToken, errorMessage);
            }
        } catch (Exception e) {
            System.out.println("Message Error");
        }
    }

    private void makeMove(String authToken, UserGameCommand command) {
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("handleClose called");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) throws Exception {
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {
        onMessage(wsMessageContext.session, wsMessageContext.message());
    }
}
