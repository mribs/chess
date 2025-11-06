package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public int createGame(String gameName) {
//        not really sure how to do the gameID thing
        GameData gameData = new GameData(0, gameName, null, null, new ChessGame());
        String gameDataJson = new Gson().toJson(gameData);

        var createGameStatement = "INSERT INTO game (gameData) VALUES (?)";
        int gameID;
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(createGameStatement, Statement.RETURN_GENERATED_KEYS)) {
            if (gameName == null) {
                throw new DataAccessException("Bad Request");
            }
            prepStatement.setString(1, gameDataJson);
            prepStatement.executeUpdate();
            try (var result = prepStatement.getGeneratedKeys()) {
                if (result.next()) {
                    gameID = result.getInt(1);
                } else {
                    throw new SQLException("Failed, no gameID generated");
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) {
        var getStatement = "SELECT gameID, gameData FROM game WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(getStatement)) {
            prepStatement.setString(1, String.valueOf(gameID));
            var result = prepStatement.executeQuery();
            if (result.next()) {
                return readGame(result);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private GameData readGame(ResultSet result) throws SQLException {
        int gameID = result.getInt("gameID");
        var gameJson = result.getString("gameData");
        GameData gameData = new Gson().fromJson(gameJson, GameData.class);
        return new GameData(gameID, gameData.gameName(), gameData.whiteUsername(), gameData.blackUsername(), gameData.game());
    }

    @Override
    public Collection<GameData> listGames() {
        var resultList = new ArrayList<GameData>();
        var listStatement = "SELECT gameID, gameData FROM game";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(listStatement)) {
            var queryResult = prepStatement.executeQuery();
            while (queryResult.next()) {
                resultList.add(readGame(queryResult));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    @Override
    public void updateGame(int gameID, GameData gameUpdate) {
        var updateStatement = "UPDATE game SET gameData=? WHERE gameID=?";
        String gameDataJson = new Gson().toJson(gameUpdate);
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(updateStatement)) {
            prepStatement.setString(2, String.valueOf(gameID));
            prepStatement.setString(1, gameDataJson);
            prepStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearGames() {
        var clearStatement = "TRUNCATE TABLE game";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(clearStatement)) {
            prepStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
