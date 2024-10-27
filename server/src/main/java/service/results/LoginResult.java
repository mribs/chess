package service.results;

import dataAccess.models.AuthToken;

public class LoginResult {
  String username;
  String authToken;

  public LoginResult(AuthToken authToken) {
    this.username=authToken.getUsername();
    this.authToken=authToken.getAuthToken();
  }

  public String getAuthToken() {
    return this.authToken;
  }
}
