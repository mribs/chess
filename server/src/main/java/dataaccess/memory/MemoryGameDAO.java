package dataaccess.memory;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.MemoryDatabase;
import model.GameData;

import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    private static int nextGameID = 1;

    @Override
    public int createGame(String gameName) {
        int gameID = nextGameID;
        GameData gameData = new GameData(gameID, gameName, null, null, new ChessGame());
        MemoryDatabase.gameMap.put(gameID, gameData);
        nextGameID++;
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) {
        return MemoryDatabase.gameMap.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return MemoryDatabase.gameMap.values();
    }

    @Override
    public void updateGame(int gameID, GameData gameUpdate) {
        MemoryDatabase.gameMap.put(gameID, gameUpdate);
    }

    @Override
    public void clearGames() {
        MemoryDatabase.gameMap.clear();
    }

}
