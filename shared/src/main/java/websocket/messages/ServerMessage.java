package websocket.messages;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    String message;
    String errorMessage;
    ChessGame game;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ServerMessage(ServerMessageType serverMessageType, String message) {
        this.serverMessageType=serverMessageType;
        this.message=message;
    }
    public ServerMessage(ServerMessageType serverMessageType, String message, String errorMessage) {
        this.serverMessageType=serverMessageType;
        this.message=message;
        this.errorMessage=errorMessage;
    }
    public ServerMessage(ServerMessageType serverMessageType, ChessGame game) {
        this.serverMessageType = serverMessageType;
        this.game = game;
    }
    public ChessGame getGame() {
        return this.game;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
