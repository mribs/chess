package model;

public class User {
  String username;
  String password;
  String email;

  public User(String username, String password, String email) {
    this.username=username;
    this.password=password;
    this.email=email;
  }

  public User(String username, String password) {
    this.username=username;
    this.password=password;
  }

  //getters and setters
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username=username;
  }

  public String getPassword() {
    return password;
  }

  public Object getEmail() {
    return email;
  }
}

