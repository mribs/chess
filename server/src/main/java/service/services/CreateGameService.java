package service.services;

import dataAccess.Authorizer;
import dataAccess.BadRequestException;
import dataAccess.DAO.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedException;
import dataAccess.models.Game;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;

public class CreateGameService {
  public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException, UnauthorizedException, BadRequestException {
   if (request.getGameName() == "") {
     throw new BadRequestException();
   }
    GameDAO gameDAO = new GameDAO();

    Game game = gameDAO.insert(request.getGameName());
    return new CreateGameResult(game.getGameID());
  }
}
