package websocket;

import com.google.gson.Gson;
import exceptions.DataAccessException;
import websocket.commands.JoinCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

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
        Notification notification = new Gson().fromJson(message, Notification.class);
        notificationHandler.notify(notification);
      }
    });
  }

  //TODO: implement various messages to send
  public void joinGame(String authtoken, int gameID) throws DataAccessException {
    try {
      var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authtoken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException ex) {
      throw new DataAccessException(ex.getMessage());
    }
  }

  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {}
}