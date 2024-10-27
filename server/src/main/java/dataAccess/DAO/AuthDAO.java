package dataAccess.DAO;

import dataAccess.DataAccessException;
import dataAccess.TempDatabase;
import dataAccess.UnauthorizedException;
import dataAccess.models.AuthToken;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthDAO {
//creates a new authToken object
  public AuthToken createToken(String username) throws DataAccessException {
    String authTokenString = UUID.randomUUID().toString();
    AuthToken newAuthToken = new AuthToken(username, authTokenString);

    TempDatabase.authTokenMap.put(authTokenString, newAuthToken);
    return newAuthToken;
  }

  //reads an authToken object
  public AuthToken readToken(String authToken) {
    AuthToken authToken1 = TempDatabase.authTokenMap.get(authToken);
    return authToken1;
  }
//update an authToken object
  public void updateToken(String currAuthToken, AuthToken newAuthToken) throws UnauthorizedException {
    if (TempDatabase.authTokenMap.containsKey(currAuthToken)) {
      TempDatabase.authTokenMap.put(currAuthToken, newAuthToken);
    }
    else throw new UnauthorizedException();
  }
//delete an authToken object
  public AuthToken deleteToken(String authToken) throws UnauthorizedException {
    AuthToken oldAuthToken = TempDatabase.authTokenMap.remove(authToken);
    if (oldAuthToken == null) {
      throw new UnauthorizedException();
    }
    return oldAuthToken;
  }

  public void clearTokens() {
    TempDatabase.authTokenMap.clear();
  }
}