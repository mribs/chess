package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.Authorizer;
import dataaccess.dao.sql.AuthDAO;
import dataaccess.dao.sql.GameDAO;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import model.Game;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
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
        case LEAVE -> leaveGame(session, authToken, command);
        case RESIGN -> resign(session, authToken, command);
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }

  private void leaveGame(Session session, String authToken, UserGameCommand command) {
    try {
      Authorizer authorizer=new Authorizer();
      authorizer.authorize(authToken);
      String username=new AuthDAO().readToken(authToken).getUsername();
      GameDAO gameDAO=new GameDAO();
      ChessGame game=null;
      int gameID=command.getGameID();
      Game gameInfo=gameDAO.find(gameID);
      ChessGame.TeamColor teamColor=null;
      if (gameInfo != null) {
        game=gameInfo.getGame();
        if (username.equals(gameInfo.getWhiteUsername())) {
          gameDAO.updateWhite(gameID, null);
        } else if (username.equals(gameInfo.getBlackUsername())) {
          gameDAO.updateBlack(gameID, null);
        }
      } else {
        ErrorMessage errorMessage=new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game cannot be found");
        connections.sendToRoot(gameID, authToken, errorMessage);
      }
      String message = String.format("%s has left the game", username);
      ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.broadcast(gameID, authToken, notification);
      connections.remove(gameID, authToken);
    } catch (UnauthorizedException e) {
      throw new RuntimeException(e);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void resign(Session session, String authToken, UserGameCommand command) {
    int gameID=command.getGameID();
    try {
      Authorizer authorizer=new Authorizer();
      authorizer.authorize(authToken);
      String username=new AuthDAO().readToken(authToken).getUsername();
      GameDAO gameDAO=new GameDAO();
      ChessGame game=null;
      Game gameInfo=gameDAO.find(gameID);
      ChessGame.TeamColor teamColor=null;
      if (gameInfo != null) {
        game=gameInfo.getGame();
        if (username.equals(gameInfo.getWhiteUsername())) {
          teamColor=ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameInfo.getBlackUsername())) {
          teamColor=ChessGame.TeamColor.BLACK;
        } else {
          ErrorMessage errorMessage=new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Observers can't resign");
          connections.sendToRoot(gameID, authToken, errorMessage);
          return;
        }
        if (game.gameIsOver) {
          ErrorMessage errorMessage=new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Game is already over");
          connections.sendToRoot(gameID, authToken, errorMessage);
          return;
        }
        String message=String.format("%s resigned", username);
        ServerMessage resignation=new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.sendToAll(gameID, resignation);
        game.gameOver();
        gameDAO.updateGame(gameID, game);
      }
    } catch (UnauthorizedException e) {
      connections.add(gameID, authToken, session);
      ServerMessage errorMessage=new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized");
      connections.sendNoConnect(authToken, session, errorMessage);
    } catch (DataAccessException e) {
      ServerMessage errorMessage=new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: something " +
              "strange is happening in oz");
      connections.sendToRoot(gameID, authToken, errorMessage);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void connect(Session session,String authToken, UserGameCommand command) {
    try {
      connections.add(command.getGameID(), authToken, session);
      Authorizer authorizer = new Authorizer();
      authorizer.authorize(authToken);
      String username = new AuthDAO().readToken(authToken).getUsername();
      String message = String.format("%s has joined the game", username);
      ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);

      GameDAO gameDAO = new GameDAO();
      ChessGame game = null;
      if (gameDAO.find(command.getGameID()) != null) {
        game = gameDAO.find(command.getGameID()).getGame();
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.sendToRoot(command.getGameID(), authToken, loadGameMessage);
        connections.broadcast(command.getGameID(), authToken, notification);
      }
      else {
        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game cannot be found");
        connections.sendToRoot(command.getGameID(), authToken, errorMessage);
      }
    } catch (IOException | DataAccessException e) {
      System.out.println("message did not work");
    } catch (UnauthorizedException e) {
      ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized");
      connections.sendNoConnect( authToken, session, errorMessage);
    }
  }

  private void makeMove(Session session, String authToken, MakeMoveCommand command) {
    try {
      Authorizer authorizer = new Authorizer();
      authorizer.authorize(authToken);
      String username = new AuthDAO().readToken(authToken).getUsername();
      ChessMove move=command.move;
      GameDAO gameDAO=new GameDAO();
      ChessGame game=null;
      int gameID=command.getGameID();
      Game gameInfo =gameDAO.find(gameID);
      ChessGame.TeamColor teamColor = null;
      if (gameInfo != null) {
        game=gameInfo.getGame();
        if (username.equals(gameInfo.getWhiteUsername())) {
          teamColor = ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameInfo.getBlackUsername())){
          teamColor = ChessGame.TeamColor.BLACK;
        } else {
          throw new InvalidMoveException("Not a player");
        }
        if (game.getBoard().getPiece(move.getStartPosition()).getTeamColor() != teamColor) {
          throw new InvalidMoveException("It's not your turn");
        }
        game.makeMove(move);
        gameDAO.updateGame(gameID, game);
      } else {
        ErrorMessage errorMessage=new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: game cannot be found");
        connections.sendToRoot(gameID, authToken, errorMessage);
      }
      ServerMessage serverMessage=new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
      String message = String.format("%s made a move", username);
      ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
      connections.sendToAll(gameID, serverMessage);
      connections.broadcast(gameID, authToken, notification);
    } catch (IOException e) {
      System.out.println("load game no good");
    } catch (DataAccessException e) {
      System.out.println("game not found");
    } catch (InvalidMoveException e) {
      ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid Move");
      connections.sendToRoot(command.getGameID(), authToken, errorMessage);
    } catch (UnauthorizedException e) {
      connections.add(command.getGameID(), authToken, session);
      ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized");
      connections.sendNoConnect(authToken, session, errorMessage);
    }
  }
}
