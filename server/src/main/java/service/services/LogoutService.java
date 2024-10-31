package service.services;

//import dataAccess.DAO.memory.AuthDAO;
import dataAccess.DAO.SQL.AuthDAO;

import exceptions.DataAccessException;
import exceptions.UnauthorizedException;

public class LogoutService {
  public void logOut(String authToken) throws DataAccessException, UnauthorizedException {
    AuthDAO authDAO = new AuthDAO();
    authDAO.deleteToken(authToken);
  }

}
