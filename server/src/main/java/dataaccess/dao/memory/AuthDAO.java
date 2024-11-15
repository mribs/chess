package dataaccess.dao.memory;

import dataaccess.DataAccessException;
import dataaccess.Database;
import dataaccess.UnauthorizedException;
import model.AuthToken;

import java.util.UUID;

public class AuthDAO {
//creates a new authToken object
  public AuthToken createToken(String username) throws DataAccessException {
    String authTokenString = UUID.randomUUID().toString();
    AuthToken newAuthToken = new AuthToken(username, authTokenString);

    Database.authTokenMap.put(authTokenString, newAuthToken);
    return newAuthToken;
  }
  //reads an authToken object
  public AuthToken readToken(String authToken) {
    AuthToken authToken1 = Database.authTokenMap.get(authToken);
    return authToken1;
  }
  public AuthToken deleteToken(String authToken) throws UnauthorizedException {
    AuthToken oldAuthToken = Database.authTokenMap.remove(authToken);
    if (oldAuthToken == null) {
      throw new UnauthorizedException();
    }
    return oldAuthToken;
  }
  public void clearTokens() {
    Database.authTokenMap.clear();
  }
}