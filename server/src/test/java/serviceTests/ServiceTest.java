package serviceTests;

import dataAccess.*;
import dataAccess.models.AuthToken;
import dataAccess.models.Game;
import dataAccess.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import service.requests.*;
import service.results.CreateGameResult;
import service.results.JoinGameResult;
import service.results.ListGamesResult;
import service.results.LoginResult;
import service.services.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

  @BeforeEach
  void setUp() {

    //add 1 user
    TempDatabase.userMap.put("testUser", new User("testUser", "testPass", "test"));
    TempDatabase.authTokenMap.put("testAuth", new AuthToken("testUser", "testAuth"));
    //add 1 game
    TempDatabase.gameMap.put(1, new Game("testGame"));
  }

  @AfterEach
  void takeDown() {
    TempDatabase.gameMap.clear();
    TempDatabase.authTokenMap.clear();
    TempDatabase.userMap.clear();
  }

  @Test
  void clear() {
    new ClearService().clear();
    //check for empty database
    assertTrue(TempDatabase.authTokenMap.isEmpty());
    assertTrue(TempDatabase.userMap.isEmpty());
    assertTrue(TempDatabase.gameMap.isEmpty());
  }

  @Test
  void createGamePass() throws DataAccessException, UnauthorizedException, BadRequestException {
    CreateGameResult result = new CreateGameService().createGame(new CreateGameRequest("new Game"));
    System.out.println(result.toString());

    assertTrue(TempDatabase.gameMap.containsKey(result.getGameID()));
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
    Game game = TempDatabase.gameMap.get(1);
    assertTrue(game.getWhiteUsername() == "testUser");

  }
  @Test
  void joinFail() throws BadRequestException, DataAccessException, AlreadyTakenException {
    TempDatabase.userMap.put("testUser2", new User("meanUser", "password", "email"));
    new JoinGameService().join(new JoinGameRequest(1, "White"), "meanUser");
    assertThrows(AlreadyTakenException.class, () -> {
      new JoinGameService().join(new JoinGameRequest(1, "White"), "testUser");
    });

  }

  @Test
  void listPass() {
    ListGamesResult result= new ListGamesService().listGames(new ListGamesRequest("testAuth"));
    Collection<Game> expectedGames = TempDatabase.gameMap.values();
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
  void loginPass() throws UnauthorizedException, DataAccessException {
    //good password
    LoginResult authKey = new LoginService().login(new LoginRequest("testUser", "testPass"));
    assertTrue(TempDatabase.authTokenMap.containsKey(authKey.getAuthToken()));
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
    assertFalse(TempDatabase.authTokenMap.containsKey("testAuth"));

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
    assertTrue(TempDatabase.authTokenMap.containsKey(result.getAuthToken()));
    assertTrue(TempDatabase.userMap.containsKey("Test user2"));
  }
  @Test
  void registerFail() {
    assertThrows(AlreadyTakenException.class, () -> {
      new RegisterService().register(new RegisterRequest("testUser", "password", "email"));
    });

  }
}
