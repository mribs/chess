package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDatabase;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.request.*;
import service.result.CreateGameResult;
import service.result.ListGamesResult;
import service.result.LoginResult;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    static UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    static AuthService authService = new AuthService(new MemoryAuthDAO());
    static GameService gameService = new GameService(new MemoryGameDAO());
    String testUserAuth;
    int gameID;

    @BeforeEach
    void setUp() throws DataAccessException {
//        add user
        LoginResult loginResult = userService.register(new RegisterRequest("testS", "test", "t"));
        testUserAuth = loginResult.authToken();
//        add game
        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest("test1"));
        gameID = createGameResult.gameID();
    }

    @AfterEach
    void takeDown() {
        userService.clearUsers();
        authService.clearAuths();
        gameService.clearGames();

    }

    @Test
    void registerPass() throws DataAccessException {
        LoginResult loginResult = userService.register(new RegisterRequest("test", "test", "test"));
        assertTrue(MemoryDatabase.authMap.containsKey(loginResult.authToken()));
        assertTrue(MemoryDatabase.userMap.containsKey("test"));
    }

    @Test
    void registerFail() {
        assertThrows(DataAccessException.class, () -> {
            userService.register(new RegisterRequest("testS", "test", "t"));
        });
    }

    @Test
    void loginPass() throws DataAccessException {
        LoginResult loginResult = userService.login(new LoginRequest("testS", "test"));
        assertTrue(MemoryDatabase.authMap.containsKey(loginResult.authToken()));
    }

    @Test
    void loginFail() {
        assertThrows(DataAccessException.class, () -> {
            userService.login(new LoginRequest("testS", "wrong"));
        });
    }

    @Test
    void logoutPass() throws DataAccessException {
        userService.logout(new LogoutRequest(testUserAuth));
        assertFalse(MemoryDatabase.authMap.containsKey(testUserAuth));
    }

    @Test
    void logoutFail() {
        assertThrows(DataAccessException.class, () -> {
            userService.logout(new LogoutRequest(null));
        });
    }

    //    gameService tests
    @Test
    void createGamePass() throws DataAccessException {
        CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest("test2"));
        assertTrue(MemoryDatabase.gameMap.containsKey(createGameResult.gameID()));
    }

    @Test
    void createGameFail() {
        assertThrows(DataAccessException.class, () -> {
            gameService.createGame(new CreateGameRequest(null));
        });
    }

    @Test
    void joinPass() throws DataAccessException {
        gameService.joinGame(new JoinGameRequest("WHITE", gameID), "testS");
        GameData gameData = MemoryDatabase.gameMap.get(gameID);
        assertEquals("testS", gameData.whiteUsername());
    }

    @Test
    void joinFail() throws DataAccessException {
        userService.register(new RegisterRequest("test2", "test", "t"));
        gameService.joinGame(new JoinGameRequest("WHITE", gameID), "test2");
        assertThrows(DataAccessException.class, () -> {
            gameService.joinGame(new JoinGameRequest("WHITE", gameID), "testS");
        });
    }

    @Test
    void listPass() {
        ListGamesResult listGamesResult = gameService.listGames();
        Collection<GameData> expected = MemoryDatabase.gameMap.values();
        Collection<GameData> actual = listGamesResult.games();
        assertEquals(expected, actual);
    }

    @Test
    void listFail() {
//        failure would be in authorization... not sure how to test list games specifically
        assertThrows(DataAccessException.class, () -> {
            authService.authorize("random token");
        });
    }

    @Test
    void clear() {
        userService.clearUsers();
        authService.clearAuths();
        gameService.clearGames();

        assertTrue(MemoryDatabase.authMap.isEmpty());
        assertTrue(MemoryDatabase.gameMap.isEmpty());
        assertTrue(MemoryDatabase.userMap.isEmpty());
    }


}
