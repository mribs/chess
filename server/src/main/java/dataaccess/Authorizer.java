package dataaccess;

import dataaccess.dao.memory.AuthDAO;

import model.AuthToken;
import model.DataAccessException;

public class Authorizer{
  private String username;

  public String authorize(String authToken) throws UnauthorizedException, DataAccessException {
    AuthDAO authDAO = new AuthDAO();

    AuthToken token = authDAO.readToken(authToken);

    if (token == null) {
      throw new UnauthorizedException();
    }

    this.username = token.getUsername();
    return this.username;
  }

}
