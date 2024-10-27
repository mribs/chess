package service.services;

import dataAccess.AlreadyTakenException;
import dataAccess.BadRequestException;
import dataAccess.DAO.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.models.Game;
import service.requests.JoinGameRequest;
import service.results.JoinGameResult;

public class JoinGameService {
  JoinGameRequest joinGameRequest;

  //return request result
  public JoinGameResult join(JoinGameRequest joinGameRequest, String username) throws DataAccessException, AlreadyTakenException, BadRequestException {
    GameDAO gameDAO = new GameDAO();
    Integer gameID = joinGameRequest.getGameID();
    //see if game exists
    Game game = gameDAO.find(gameID);
    if (game == null) throw new BadRequestException();

    //add user where relevant
    String playerColor = joinGameRequest.getPlayerColor();
    if ("WHITE".equals(playerColor)) {
      if (game.getWhiteUsername() != null) throw new AlreadyTakenException();
      game.setWhiteUsername(username);
    }
    else if ("BLACK".equals(playerColor)) {
      if (game.getBlackUsername() != null) throw new AlreadyTakenException();
      game.setBlackUsername(username);
    }
    else {
      game.addObserver(username);
      playerColor = "OBSERVER";
    }

    //update game
    gameDAO.claimSpot(gameID, game);
    return new JoinGameResult(gameID, playerColor);
  }
}
