package service.services;

import dataaccess.dao.sql.AuthDAO;
import exceptions.UnauthorizedException;
import exceptions.DataAccessException;

public class LogoutService {
  public void logOut(String authToken) throws UnauthorizedException, DataAccessException {
    AuthDAO authDAO = new AuthDAO();
    authDAO.deleteToken(authToken);
  }

}
