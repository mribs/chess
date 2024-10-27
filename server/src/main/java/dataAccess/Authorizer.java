package dataAccess;

import dataAccess.DAO.AuthDAO;
import dataAccess.models.AuthToken;

public class Authorizer{
  private String username;

  public String authorize(String authToken) throws UnauthorizedException {
    AuthDAO authDAO = new AuthDAO();

    AuthToken token = authDAO.readToken(authToken);

    if (token == null) throw new UnauthorizedException();

    this.username = token.getUsername();
    return this.username;
  }

}
