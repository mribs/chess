package ui;

import exceptions.DataAccessException;
import model.*;
import server.ServerFacade;

public class PreLogin {
  private ServerFacade serverFacade;

  public PreLogin(ServerFacade serverFacade) {
    this.serverFacade=serverFacade;
  }

  /* things I can do:
   * list options
   * quit program
   * login
   * register a new user
   */
  public String helpMenu() {
    String help = """
      Help Menu
      Login : Login as an existing user
      Register : Register a new user
      Quit : Exit Chess
     """;
    return help;
  }

  public AuthToken register(String username, String password, String email) {
    try {
      User user=new User(username, password, email);
      AuthToken authToken=serverFacade.registerUser(user);
      return authToken;
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public AuthToken login(String username, String password) {
    try {
      User user = new User(username, password);
      AuthToken authToken = serverFacade.login(user);
      return authToken;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


}
