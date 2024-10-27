package dataAccess.models;

public class AuthToken {
  String authToken;
  String username;

  public AuthToken(String username, String authToken) {
    this.authToken = authToken;
    this.username = username;
  }

  //getters and setters
  public String getAuthToken() {
    return authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken=authToken;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username=username;
  }
}
