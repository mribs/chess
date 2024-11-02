package service.services;

import dataaccess.dao.sql.AuthDAO;
import dataaccess.dao.sql.GameDAO;
import dataaccess.dao.sql.UserDAO;

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
