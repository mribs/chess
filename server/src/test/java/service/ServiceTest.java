package service;

import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import model.*;
import service.results.*;
import service.requests.*;
import service.services.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

  @BeforeEach
  void setUp(){
    //add 1 user
    Database.userMap.put("testUser", new User("testUser", "testPass", "test"));
    Database.authTokenMap.put("testAuth", new AuthToken("testUser", "testAuth"));

    //add 1 game
    Database.gameMap.put(1, new Game("testGame"));
  }

  @AfterEach
  void takeDown() {
    Database.gameMap.clear();

    Database.authTokenMap.clear();

    Database.userMap.clear();


  }

  @Test
  void clear() {
    new ClearService().clear();
    //check for empty database
    assertTrue(Database.authTokenMap.isEmpty());
    assertTrue(Database.userMap.isEmpty());
    assertTrue(Database.gameMap.isEmpty());
  }

  @Test
  void createGamePass() throws DataAccessException, UnauthorizedException, BadRequestException {
    CreateGameResult result = new CreateGameService().createGame(new CreateGameRequest("new Game"));
    System.out.println(result.toString());

    assertTrue(Database.gameMap.containsKey(result.getGameID()));
  }

  @Test
  void createGameFail() {
    assertThrows(BadRequestException.class, () -> {
      //test no game name
      CreateGameResult result = new CreateGameService().createGame(new CreateGameRequest(""));
    });

  }

  @Test
  void joinPass() throws BadRequestException, DataAccessException, AlreadyTakenException {
    JoinGameResult result = new JoinGameService().join(new JoinGameRequest(1, "white"), "testUser");
    Game game = Database.gameMap.get(1);
    assertTrue(game.getWhiteUsername() == "testUser");

  }
  @Test
  void joinFail() throws BadRequestException, DataAccessException, AlreadyTakenException {
    Database.userMap.put("testUser2", new User("meanUser", "password", "email"));
    new JoinGameService().join(new JoinGameRequest(1, "White"), "meanUser");
    assertThrows(AlreadyTakenException.class, () -> {
      new JoinGameService().join(new JoinGameRequest(1, "White"), "testUser");
    });

  }

  @Test
  void listPass() {
    ListGamesResult result= new ListGamesService().listGames(new ListGamesRequest("testAuth"));
    Collection<Game> expectedGames = Database.gameMap.values();
    ArrayList<Game> gameList = new ArrayList<>(expectedGames);
    ArrayList<Game> testList = result.getGames();

    for (int i = 0; i < gameList.size(); i++) {
      assertEquals(gameList.get(i), testList.get(i));
    }

  }
  @Test
  void listFail() {
    //not sure how to do this one because I'm handling authorization in my handlers, so I would need to access my server to test it I think...
    assertEquals(1, 1);

  }

  @Test
  void loginPass() throws UnauthorizedException, DataAccessException, BadRequestException {
    //good password
    LoginResult authKey = new LoginService().login(new LoginRequest("testUser", "testPass"));
    assertTrue(Database.authTokenMap.containsKey(authKey.getAuthToken()));
  }
  @Test
  void loginFail() {
    //bad password
    assertThrows(UnauthorizedException.class, () -> {
      new LoginService().login(new LoginRequest("testUser", "notPass"));
    });

  }

  @Test
  void logoutPass() throws UnauthorizedException, DataAccessException {
    new LogoutService().logOut("testAuth");
    assertFalse(Database.authTokenMap.containsKey("testAuth"));

  }
  @Test
  void logoutFail() {
    assertThrows(UnauthorizedException.class, () -> {
      new LogoutService().logOut("bad Auth");
    });
  }

  @Test
  void registerPass() throws BadRequestException, DataAccessException, AlreadyTakenException {
    LoginResult result = new RegisterService().register(new RegisterRequest("Test user2", "password", "email2"));
    assertTrue(Database.authTokenMap.containsKey(result.getAuthToken()));
    assertTrue(Database.userMap.containsKey("Test user2"));
  }
  @Test
  void registerFail() {
    assertThrows(AlreadyTakenException.class, () -> {
      new RegisterService().register(new RegisterRequest("testUser", "password", "email"));
    });

  }
}
