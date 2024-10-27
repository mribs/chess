package service.requests;

public class LoginRequest {
  private String username;
  private String password;
  public LoginRequest(String name, String password) {
    this.username = name;
    this.password = password;
  }
  //getters and Setters


  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username=username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password=password;
  }
}
