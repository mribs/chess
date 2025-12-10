package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.UnauthorizedException;
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

import java.io.IOException;

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
        try {
            String username = new SQLAuthDAO().getAuth(authToken).username();
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData gameData = null;
            int gameID = command.getGameID();
            gameData = gameDAO.getGame(gameID);
            ChessGame.TeamColor teamColor = null;
            if (gameData != null) {
                ChessGame game = gameData.game();
                if (username.equals(gameData.whiteUsername())) {
                    GameData updatedData = new GameData(gameData.gameID(), gameData.gameName(), null, gameData.blackUsername(), game);
                    gameDAO.updateGame(gameID, updatedData);
                } else if (username.equals(gameData.blackUsername())) {
                    GameData updatedData = new GameData(gameData.gameID(), gameData.gameName(), gameData.whiteUsername(), null, game);
                    gameDAO.updateGame(gameID, updatedData);
                }
            } else {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: game cannot be found");
                connectionManager.sendToRoot(gameID, authToken, errorMessage);
            }
            String message = String.format("%s has left the game", username);
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connectionManager.broadcast(gameID, authToken, notification);
            connectionManager.remove(gameID, authToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void resign(String authToken, UserGameCommand command) {
        int gameID = command.getGameID();
        try {
            String username = new SQLAuthDAO().getAuth(authToken).username();
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData gameData = gameDAO.getGame(gameID);
            ChessGame.TeamColor teamColor = null;
            if (gameData != null) {
                ChessGame game = gameData.game();
                if (username.equals(gameData.whiteUsername())) {
                    teamColor = ChessGame.TeamColor.WHITE;
                } else if (username.equals(gameData.blackUsername())) {
                    teamColor = ChessGame.TeamColor.BLACK;
                } else {
                    ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null,
                            "Error: Observers can't resign");
                    connectionManager.sendToRoot(gameID, authToken, errorMessage);
                    return;
                }
                if (game.isOver()) {
                    ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null,
                            "Error: Game is already over, please use leave command to exit");
                    connectionManager.sendToRoot(gameID, authToken, errorMessage);
                    return;
                }
                String message = String.format("%s resigned, please leave the game", username);
                ServerMessage resignation = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connectionManager.broadcast(gameID, authToken, resignation);
                ServerMessage resigned = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "You resigned");
                connectionManager.sendToRoot(gameID, authToken, resigned);
                game.gameOver();
                GameData updatedGame = new GameData(gameID, gameData.gameName(), gameData.whiteUsername(), gameData.blackUsername(), game);
                gameDAO.updateGame(gameID, updatedGame);
                ServerMessage updateGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame);
                connectionManager.sendToAll(gameID, updateGameMessage);
            }
        } catch (UnauthorizedException e) {
            connectionManager.add(gameID, authToken, session);
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: unauthorized");
            connectionManager.sendConnectionFailure(authToken, session, errorMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            GameData gameData = gameDAO.getGame(command.getGameID());
            if (gameData.game() != null) {
                ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData);
                connectionManager.sendToRoot(command.getGameID(), authToken, loadGameMessage);
                connectionManager.broadcast(command.getGameID(), authToken, notification);
            } else {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: game cannot be found");
                connectionManager.sendToRoot(command.getGameID(), authToken, errorMessage);
            }
        } catch (Exception e) {
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: Cannot connect to game");
            connectionManager.sendToRoot(command.getGameID(), authToken, errorMessage);
        }
    }

    private void makeMove(String authToken, UserGameCommand command) {
        int gameID = 0;
        try {
            String username;
            try {
                username = new SQLAuthDAO().getAuth(authToken).username();
            } catch (Exception e) {
                throw new UnauthorizedException("Error: bad authToken");
            }
            ChessMove move = command.getMove();
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData gameData = null;
            gameID = command.getGameID();
            gameData = gameDAO.getGame(gameID);
            ChessGame.TeamColor teamColor;
            if (gameData != null) {
                ChessGame game = gameData.game();
                if (username.equals(gameData.whiteUsername())) {
                    teamColor = ChessGame.TeamColor.WHITE;
                } else if (username.equals(gameData.blackUsername())) {
                    teamColor = ChessGame.TeamColor.BLACK;
                } else {
                    throw new InvalidMoveException("Not a player");
                }
                if (game.getBoard().getPiece(move.getStartPosition()).getTeamColor() != teamColor) {
                    throw new InvalidMoveException("It's not your turn");
                }
                game.makeMove(move);
                if (game.isInCheck(teamColor)) {
                    ServerMessage checkMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "you are in check");
                    connectionManager.sendToRoot(gameID, authToken, checkMessage);
                }
                if (game.isInCheckmate(teamColor)) {
                    ServerMessage checkMateMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, "CHECKMATE");
                    connectionManager.sendToAll(gameID, checkMateMessage);
                }
                if (game.isOver()) {
                    throw new InvalidMoveException("game has ended.");
                }
                gameDAO.updateGame(gameID, gameData);
            } else {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null,
                        "Error: game cannot be found");
                connectionManager.sendToRoot(gameID, authToken, errorMessage);
            }
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData);
            String message = String.format("%s made a move", username);
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connectionManager.broadcast(gameID, authToken, notification);
            connectionManager.sendToAll(gameID, serverMessage);
        } catch (IOException e) {
            System.out.println("load game no good");
        } catch (UnauthorizedException e) {
            connectionManager.add(gameID, authToken, session);
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: unauthorized");
            connectionManager.sendConnectionFailure(authToken, session, errorMessage);
        } catch (Exception e) {
            String msg = ("Error: Invalid Move");
            ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, msg);
            connectionManager.sendToRoot(command.getGameID(), authToken, errorMessage);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
//        removing connections is handled elsewhere... but I need this override.
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
