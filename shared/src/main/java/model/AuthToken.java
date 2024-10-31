package model;

public record AuthToken(String authToken, String username) {
  public String getUsername() {
    return username;
  }

  public String getAuthToken() {
    return authToken;
  }
}
