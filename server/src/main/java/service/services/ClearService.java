package service.services;

import dataaccess.DAO.*;

public class ClearService {
  public void clear() {
    AuthDAO authDAO = new AuthDAO();
    GameDAO gameDAO = new GameDAO();
    UserDAO userDAO = new UserDAO();

    authDAO.clearTokens();
    gameDAO.clearGames();
    userDAO.clearUsers();
  }
}