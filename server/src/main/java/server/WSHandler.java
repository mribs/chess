package server;

import com.google.gson.Gson;
import dataaccess.UnauthorizedException;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WSHandler {
  private final Gson gson = new Gson();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) {
    try {
      UserGameCommand command = gson.fromJson(message, UserGameCommand.class);

      // Throws a custom UnauthorizedException. Yours may work differently.
      String username = command.getUserName((command.getAuthToken()));

//      saveSession(command.getGameID(), session);

      switch (command.getCommandType()) {
        case CONNECT -> connect(session, username, command);
//        case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
//        case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//        case RESIGN -> resign(session, username, (ResignCommand) command);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
//      sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
    }
  }

  private void connect(Session session,String username, Object command) {
    try {
      sendMessage(session, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
    } catch (IOException e) {
      System.out.println("sendMessager did not work");
    }
  };

  public void sendMessage(Session session, ServerMessage message) throws IOException {
    session.getRemote().sendString(new Gson().toJson(message));
  }

}
