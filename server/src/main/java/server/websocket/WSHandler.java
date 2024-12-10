package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WSHandler {
  private final Gson gson = new Gson();

  private final ConnectionManager connections = new ConnectionManager();
  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    try {
      UserGameCommand command = gson.fromJson(message, UserGameCommand.class);
      // Throws a custom UnauthorizedException. Yours may work differently.
      String authToken = command.getAuthToken();

      switch (command.getCommandType()) {
        case CONNECT -> connect(session, authToken, command);
//        case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//        case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//        case RESIGN -> resign(session, username, (ResignCommand) command);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void connect(Session session,String authtoken, UserGameCommand command) {
    try {
      connections.add(authtoken, session);
      String username = command.getUserName(authtoken);
      String message = String.format("%s has joined the game", username);
      Notification notification = new Notification(message);
      connections.broadcast(authtoken, notification);
    } catch (IOException e) {
      System.out.println("message did not work");
    }
  }
}
