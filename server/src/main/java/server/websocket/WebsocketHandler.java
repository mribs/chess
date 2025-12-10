package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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
                } else {
//                    "I don't wike it" -Chris Evans
                    ServerMessage notification = new ServerMessage(
                            ServerMessage.ServerMessageType.NOTIFICATION, (username + " is no longer watching"));
                    connectionManager.broadcast(gameID, authToken, notification);
                    connectionManager.remove(gameID, authToken);
                    return;
                }
                String message = String.format("%s has left the game", username);
                ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                connectionManager.broadcast(gameID, authToken, notification);
                connectionManager.remove(gameID, authToken);
            } else {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null, "Error: game cannot be found");
                connectionManager.sendToRoot(gameID, authToken, errorMessage);
            }
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
            if (gameData != null) {
                ChessGame game = gameData.game();
                if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
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

//                The tests don't like it, but sending a load game message to everybody with the update made later steps more effective

                gameDAO.updateGame(gameID, new GameData(gameID, gameData.gameName(), gameData.whiteUsername(), gameData.blackUsername(), game));
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
//            observer gets a different message
            if (color == null || color.equals("OBSERVE")) {
                message = String.format("%s is watching the game", username);
            }
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData gameData = gameDAO.getGame(command.getGameID());
            if (gameData.game() != null) {
                ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData);
                connectionManager.sendToRoot(command.getGameID(), authToken, loadGameMessage);
                connectionManager.broadcast(command.getGameID(), authToken, notification);
            } else {
                ServerMessage errorMessage = new ServerMessage(
                        ServerMessage.ServerMessageType.ERROR, null, "Error: game cannot be found");
                connectionManager.sendToRoot(command.getGameID(), authToken, errorMessage);
            }
        } catch (Exception e) {
            ServerMessage errorMessage = new ServerMessage(
                    ServerMessage.ServerMessageType.ERROR, null, "Error: Cannot connect to game");
            connectionManager.sendToRoot(command.getGameID(), authToken, errorMessage);
        }
    }

    private String toLetterNumber(ChessPosition position) {
        int col = position.getColumn();
        char colLetter = (char) ('a' + col);
        int row = position.getRow() + 1;
        return String.valueOf(colLetter) + row;
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
                if (game.isOver()) {
                    ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null,
                            "Error: Game is already over, please use leave command to exit");
                    connectionManager.sendToRoot(gameID, authToken, errorMessage);
                    return;
                }
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
                ChessGame.TeamColor oppTeamColor;
                String oppTeamUsername;
                if (teamColor == ChessGame.TeamColor.WHITE) {
                    oppTeamColor = ChessGame.TeamColor.BLACK;
                    oppTeamUsername = gameData.blackUsername();
                } else {
                    oppTeamColor = ChessGame.TeamColor.WHITE;
                    oppTeamUsername = gameData.whiteUsername();
                }
                if (game.isInCheckmate(oppTeamColor)) {
//                    I would rather this just sent checkmate, because it's more dramatic, but alas
                    ServerMessage checkMateMessage = new ServerMessage(
                            ServerMessage.ServerMessageType.NOTIFICATION, oppTeamUsername + " is in CheckMate");
                    connectionManager.sendToAll(gameID, checkMateMessage);
                } else if (game.isInStalemate(oppTeamColor)) {
                    ServerMessage stalemateMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, ("Stalemate"));
                    connectionManager.sendToAll(gameID, stalemateMessage);
                } else if (game.isInCheck(oppTeamColor)) {
                    ServerMessage checkMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, (oppTeamUsername + " is in check"));
                    connectionManager.sendToAll(gameID, checkMessage);
                }
                gameDAO.updateGame(gameID, gameData);
            } else {
                ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR, null,
                        "Error: game cannot be found");
                connectionManager.sendToRoot(gameID, authToken, errorMessage);
            }
            ServerMessage serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData);
            String message = username + " moved " + toLetterNumber(move.getStartPosition()) + " to " + toLetterNumber(move.getEndPosition());
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
