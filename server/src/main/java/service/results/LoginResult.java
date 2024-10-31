package service.results;

import model.AuthToken;

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
