package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import service.request.CreateGameRequest;
import service.result.CreateGameResult;

import java.util.Objects;

public class GameService {

    private GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void clearGames() {
        gameDAO.clearGames();

    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (Objects.equals(createGameRequest.gameName(), "") || createGameRequest.gameName() == null) {
            throw new DataAccessException("bad request");
        }
        int gameID = gameDAO.createGame(createGameRequest.gameName());
        return new CreateGameResult(gameID);
    }
}
