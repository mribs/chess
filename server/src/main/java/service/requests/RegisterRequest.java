package service.requests;

public class RegisterRequest {
  String username;
  String password;
  String email;

  public RegisterRequest(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;

  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String userName) {
    this.username=userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password=password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email=email;
  }
}
