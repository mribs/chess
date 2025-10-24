package dataaccess.memory;

import dataaccess.GameDAO;
import dataaccess.MemoryDatabase;

public class MemoryGameDAO implements GameDAO {

    @Override
    public void createGame(String gameName) {
        throw new UnsupportedOperationException("Not supported yet.");
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
