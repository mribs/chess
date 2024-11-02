package service.services;

import dataaccess.dao.memory.AuthDAO;
import dataaccess.dao.memory.GameDAO;
import dataaccess.dao.memory.UserDAO;

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
