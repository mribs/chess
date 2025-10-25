package dataaccess.memory;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.MemoryDatabase;
import model.GameData;

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
    public void getGame(String gameID) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void listGames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateGame(String gameID, String game) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearGames() {
        MemoryDatabase.gameMap.clear();
    }

}
