package service.services;

import dataaccess.*;
import dataaccess.dao.memory.AuthDAO;

public class LogoutService {
  public void logOut(String authToken) throws UnauthorizedException {
    AuthDAO authDAO = new AuthDAO();
    authDAO.deleteToken(authToken);
  }

}
