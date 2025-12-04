package client;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Collection;
import java.util.List;

import serverfacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private AuthData authData;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clear() {
        facade.clear();
        authData = facade.register("test", "pass", "email");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void clearTest() {
        facade.clear();
        assertThrows(Exception.class, () -> {
            facade.login("test", "pass");
        });
    }

    @Test
    void register() {
        var authData = facade.register("player1", "pass", "email");
        assertNotEquals(authData.authToken(), null);
    }

    @Test
    void registerFail() {
        assertThrows(Exception.class, () -> {
            facade.register("test", "pass", "email");
        });
    }

    @Test
    void login() {
        var authData = facade.login("test", "pass");
        assertNotNull(authData);
    }

    @Test
    void loginFail() {
        assertThrows(Exception.class, () -> {
            facade.login("nonexistent", "pass");
        });
        assertThrows(Exception.class, () -> {
            facade.login("test", "incorrect");
        });
    }

    @Test
    void logout() {
        assertDoesNotThrow(() ->
                facade.logout(authData.authToken())
        );
    }

    @Test
    void logoutFail() {
//        would fail if invalid authtoken ig
        assertThrows(Exception.class, () -> {
            facade.logout("not real");
        });
    }

    @Test
    void createGame() {
        GameData gameData = facade.createGame("TestGame", authData.authToken());
        assertNotNull(gameData);
    }

    @Test
    void createGameFail() {
//        fails w/ invalid auth
        assertThrows(Exception.class, () -> {
            facade.createGame("test1", "not real");
        });
    }

    @Test
    void listGames() {
        GameData gameData = facade.createGame("Test1", authData.authToken());
        Collection<GameData> gameList = facade.listGames(authData.authToken());
        assertEquals(gameList.size(), 1);
    }

    @Test
    void listGamesFail() {
//        fails w/ invalid auth
        assertThrows(Exception.class, () -> {
            facade.listGames("nonexistent");
        });
    }

    @Test
    void joinGame() {
        GameData gameData = facade.createGame("Test1", authData.authToken());
        assertDoesNotThrow(() ->
                facade.joinGame(gameData.gameID(), "white", authData)
        );
        assertDoesNotThrow(() ->
                facade.joinGame(gameData.gameID(), "black", authData)
        );
        assertDoesNotThrow(() ->
                facade.joinGame(gameData.gameID(), "observe", authData)
        );
    }

    @Test
    void joinGameFail() {
        GameData gameData = facade.createGame("testGame", authData.authToken());
        facade.joinGame(gameData.gameID(), "white", authData);
        assertThrows(Exception.class, () -> {
            facade.joinGame(gameData.gameID(), "white", authData);
        });
    }
}
