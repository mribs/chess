package service;

import dataaccess.GameDAO;

public class GameService {

    private GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void clearGames() {
        gameDAO.clearGames();

    }
}
