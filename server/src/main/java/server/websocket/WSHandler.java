package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.Authorizer;
import dataaccess.dao.sql.GameDAO;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import server.Server;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
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
        case MAKE_MOVE -> makeMove(session, authToken, gson.fromJson(message, MakeMoveCommand.class));
//        case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
//        case RESIGN -> resign(session, username, (ResignCommand) command);
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }

  private void connect(Session session,String authToken, UserGameCommand command) {
    try {
      connections.add(authToken, session);
      Authorizer authorizer = new Authorizer();
      authorizer.authorize(authToken);
      String username = command.getUserName(authToken);
      String message = String.format("%s has joined the game", username);
      ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

      GameDAO gameDAO = new GameDAO();
      ChessGame game = null;
      if (gameDAO.find(command.getGameID()) != null) {
        game = gameDAO.find(command.getGameID()).getGame();
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.sendToRoot(authToken, loadGameMessage);
        connections.broadcast(authToken, notification);
      }
      else {
        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game cannot be found");
        connections.sendToRoot(authToken, errorMessage);
      }
    } catch (IOException | DataAccessException e) {
      System.out.println("message did not work");
    } catch (UnauthorizedException e) {
      ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized");
      connections.sendToRoot(authToken, errorMessage);
    }
  }

  private void makeMove(Session session, String authToken, MakeMoveCommand command) {
    try {
      Authorizer authorizer = new Authorizer();
      authorizer.authorize(authToken);
      ChessMove move=command.move;
      GameDAO gameDAO=new GameDAO();
      ChessGame game=null;
      int gameID=command.getGameID();
      if (gameDAO.find(gameID) != null) {
        game=gameDAO.find(gameID).getGame();
        game.makeMove(move);
        gameDAO.updateGame(gameID, game);
      } else {
        ErrorMessage errorMessage=new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game cannot be found");
        connections.sendToRoot(authToken, errorMessage);
      }
      ServerMessage serverMessage=new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
      String username = command.getUserName(authToken);
      String message = String.format("%s made a move", username);
      ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.sendToAll(serverMessage);
      connections.broadcast(authToken, notification);
    } catch (IOException e) {
      System.out.println("load game no good");
    } catch (DataAccessException e) {
      System.out.println("game not found");
    } catch (InvalidMoveException e) {
      System.out.println("make move (WSHandler) didn't work??");
    } catch (UnauthorizedException e) {
      ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized");
      connections.sendToRoot(authToken, errorMessage);
    }
  }
}
