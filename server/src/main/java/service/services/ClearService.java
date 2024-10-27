package service.services;

import model.DAO.AuthDAO;
import model.DAO.GameDAO;
import model.DAO.UserDAO;

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
