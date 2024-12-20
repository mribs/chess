package dataaccess.dao.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DatabaseManager;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GameDAO extends DAO {

  //inserts new game into database
  public Game insert(String gameName) throws DataAccessException, BadRequestException {
    if (gameName == "" || gameName == null) {
      throw new BadRequestException();
    }
    Game game = new Game(gameName);
    int gameID = game.getGameID();
    var gameJSON = new Gson().toJson(game.getGame());

    var statement = "INSERT INTO game (gameID, gameName, game, whiteUsername, blackUsername) VALUES (?, ?, ?, ?, ?)";
    executeUpdate(statement, String.valueOf(gameID), gameName, gameJSON,null,null);
    return game;
  }

  //finds game by gameID
  public Game find(Integer gameID) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT gameID, gameName, game, whiteUsername, blackUsername FROM game WHERE gameID=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, String.valueOf(gameID));
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readGameInfo(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("something went wrong");
    }
    return null;
  }

  private Game readGameInfo(ResultSet rs) throws SQLException {
    var gameID = rs.getInt("gameID");
    var gameName = rs.getString("gameName");
    var game = rs.getString("game");
    var whiteUsername = rs.getString("whiteUsername");
    var blackUsername = rs.getString("blackUsername");

    ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);
    return new Game(gameID, whiteUsername, blackUsername, gameName, chessGame);
  }

  //uses player's username to "claim" a spot in a game
  public void claimSpot(Integer gameID, Game game) throws DataAccessException {
    if (find(gameID) == null) {
      throw new DataAccessException("cannot find gameID");
    }
    String gameJson = new Gson().toJson(game.getGame());
    var statement = "UPDATE game SET gameName=?, game=?, whiteUsername=?, blackUsername=? WHERE gameID=?";
    executeUpdate(statement, game.getGameName(), gameJson, game.getWhiteUsername(), game.getBlackUsername(), String.valueOf(gameID));
  }

  //clears database
  public void clearGames() {
    var statement = "TRUNCATE game";
    try {
      executeUpdate(statement);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public ArrayList<Game> getGames() {
    var result = new ArrayList<Game>();
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT  gameID, gameName, game, whiteUsername, blackUsername FROM game";
      try (var ps = conn.prepareStatement(statement)) {
        try (var rs = ps.executeQuery()) {
          while (rs.next()) {
            result.add(readGameInfo(rs));
          }
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (DataAccessException | SQLException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public void updateGame(int gameID, ChessGame game) throws DataAccessException {
    if (find(gameID) == null) {
      throw new DataAccessException("cannot find gameID");
    }
    String gameJson = new Gson().toJson(game);
    var statement = "UPDATE game SET game=? WHERE gameID=?";
    executeUpdate(statement, gameJson, String.valueOf(gameID));
  }
  public void updateWhite(int gameID, String white) throws DataAccessException {
    if (find(gameID) == null) {
      throw new DataAccessException("cannot find gameID");
    }
    var statement = "UPDATE game SET whiteUsername=? WHERE gameID=?";
    executeUpdate(statement, white, String.valueOf(gameID));
  }
  public void updateBlack(int gameID, String black) throws DataAccessException {
    if (find(gameID) == null) {
      throw new DataAccessException("cannot find gameID");
    }
    var statement = "UPDATE game SET blackUsername=? WHERE gameID=?";
    executeUpdate(statement, black, String.valueOf(gameID));
  }

}