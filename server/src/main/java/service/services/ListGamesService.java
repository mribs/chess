package service.services;

import dataaccess.DAO.GameDAO;
import service.requests.ListGamesRequest;
import service.results.ListGamesResult;

public class ListGamesService {
  public ListGamesResult listGames(ListGamesRequest listGamesRequest){
    GameDAO gameDAO = new GameDAO();
    return new ListGamesResult(gameDAO.getGames());
  }
}