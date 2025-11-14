package client;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import client.ServerFacade;

import java.util.Collection;
import java.util.List;

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
        facade = new ServerFacade(port);
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
    void register() {
        var authData = facade.register("player1", "pass", "email");
        assertNotEquals(authData.authToken(), null);
    }

    @Test
    void login() {
        var authData = facade.login("test", "pass");
        assertNotNull(authData);
    }

    @Test
    void logout() {
        assertDoesNotThrow(() ->
                facade.logout(authData.authToken())
        );
    }

    @Test
    void createGame() {
        GameData gameData = facade.createGame("TestGame", authData.authToken());
        assertNotNull(gameData);
    }

    @Test
    void listGames() {
        GameData gameData = facade.createGame("Test1", authData.authToken());
        List gameList = facade.listGames(authData.authToken());
        assertEquals(gameList.size(), 1);
    }

    @Test
    void joinGame() {
        GameData gameData = facade.createGame("Test1", authData.authToken());
        assertDoesNotThrow(() ->
                facade.joinGame("Test1", "white", authData.username(), authData.authToken())
        );
    }
}
