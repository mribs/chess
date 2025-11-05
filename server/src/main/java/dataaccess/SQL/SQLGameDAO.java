package dataaccess.SQL;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

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
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(int gameID, GameData gameUpdate) {

    }

    @Override
    public void clearGames() {

    }
}
