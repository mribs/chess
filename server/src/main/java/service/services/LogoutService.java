package service.services;

import dataaccess.*;
import dataaccess.DAO.AuthDAO;

public class LogoutService {
  public void logOut(String authToken) throws DataAccessException, UnauthorizedException {
    AuthDAO authDAO = new AuthDAO();
    authDAO.deleteToken(authToken);
  }

}
