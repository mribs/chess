package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exceptions.DataAccessException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketClient extends Endpoint {
  public Session session;
  public NotificationHandler notificationHandler;

  public WebSocketClient(NotificationHandler notificationHandler) throws Exception {
    URI uri = new URI("ws://localhost:8080/ws");
    this.notificationHandler = notificationHandler;

    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    this.session = container.connectToServer(this, uri);

    this.session.addMessageHandler(new MessageHandler.Whole<String>() {
      @Override
      public void onMessage(String message) {
        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
        notificationHandler.notify(notification);
      }
    });
  }

  public void joinGame(String authtoken, int gameID) throws DataAccessException {
    try {
      //FIXME: need different messages for observer and player joining
      UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authtoken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new DataAccessException(ex.getMessage());
    }
  }

  public void makeMove(String authtoken, int gameID, ChessMove move) throws DataAccessException {
    try {
      MakeMoveCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authtoken, gameID, move);
      this.session.getBasicRemote().sendText(new Gson().toJson(command));
    } catch (IOException ex) {
      throw new DataAccessException(ex.getMessage());
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {}
}