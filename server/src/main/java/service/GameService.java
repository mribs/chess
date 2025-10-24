package service;

import dataaccess.GameDAO;

public class GameService {

    private GameDAO gameDAO;

    public void clearGames() {
        gameDAO.clearGames();

    }
}
