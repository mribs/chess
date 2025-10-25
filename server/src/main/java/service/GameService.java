package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.result.CreateGameResult;
import service.result.ListGamesResult;

import java.util.Collection;
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

    public ListGamesResult listGames() {
        Collection<GameData> games = gameDAO.listGames();
        return new ListGamesResult(games);
    }

    public void joinGame(JoinGameRequest joinGameRequest, String username) throws DataAccessException {
        String playerColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) {
            throw new DataAccessException("bad request");
        }
        GameData newGameData;
        if (Objects.equals(playerColor, "WHITE")) {
            if (gameData.whiteUsername() != null) {
                throw new DataAccessException("already taken");
            }
            newGameData = new GameData(gameData.gameID(), gameData.gameName(), username, gameData.blackUsername(), gameData.game());
        } else if (Objects.equals(playerColor, "BLACK")) {
            if (gameData.blackUsername() != null) {
                throw new DataAccessException("already taken");
            }
            newGameData = new GameData(gameData.gameID(), gameData.gameName(), gameData.whiteUsername(), username, gameData.game());
        } else {
            throw new DataAccessException("bad request");
        }
        gameDAO.updateGame(gameID, newGameData);
    }
}
