package service.services;

import dataAccess.DAO.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedException;

public class LogoutService {
  public void logOut(String authToken) throws DataAccessException, UnauthorizedException {
    AuthDAO authDAO = new AuthDAO();
    authDAO.deleteToken(authToken);
  }

}
