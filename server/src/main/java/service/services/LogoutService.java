package service.services;

import dataaccess.*;
import dataaccess.dao.sql.AuthDAO;
import model.DataAccessException;

public class LogoutService {
  public void logOut(String authToken) throws UnauthorizedException, DataAccessException {
    AuthDAO authDAO = new AuthDAO();
    authDAO.deleteToken(authToken);
  }

}
