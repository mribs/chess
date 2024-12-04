package client;


import dataaccess.dao.sql.*;
import model.AuthToken;
import model.DataAccessException;
import model.User;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }
    @AfterEach
    public void clear() {
        new AuthDAO().clearTokens();
        new UserDAO().clearUsers();
        new GameDAO().clearGames();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void regPassTest() {
        assertDoesNotThrow(() ->
                facade.registerUser(new User("testname", "testPass", "testemail"))
        );
    }
    @Test
    public void regFailTest() {
        assertThrows(DataAccessException.class, () -> {
            facade.registerUser(new User("name", "pass"));
        });
    }

    @Test
    public void loginPassTest() {
        assertDoesNotThrow(() ->
                facade.registerUser(new User("testname", "testPass", "testemail"))
        );
        assertDoesNotThrow(() ->
                facade.login(new User("testname", "testPass"))
        );
    }
    @Test
    public void loginFailTest() {
        assertThrows(DataAccessException.class, () -> {
            facade.login(new User("name", "pass"));
        });
    }

    @Test
    public void logoutPassTEst() {
        AuthToken token=null;
        try {
            token=facade.registerUser(new User("testname", "testPass", "testemail"));
        } catch (DataAccessException e) {
            System.out.println("register failed: " + e.getMessage());
        }
        AuthToken finalToken=token;
        assertDoesNotThrow(() ->
                facade.logout(finalToken.getAuthToken())
        );
    }
    @Test
    public void logoutFailTest() {
        assertThrows(DataAccessException.class, () -> {
            facade.logout("anytoken");
        });
    }

    @Test
    public void createPassTest() {
        AuthToken token=null;
        try {
            token=facade.registerUser(new User("testname", "testPass", "testemail"));
        } catch (DataAccessException e) {
            System.out.println("register failed: " + e.getMessage());
        }
        AuthToken finalToken=token;
        assertDoesNotThrow(() ->
                facade.createGame(finalToken.getAuthToken(), "TestGame")
        );
    }

    @Test
    public void createFailTest() {
        assertThrows(DataAccessException.class, () -> {
            facade.createGame("anytoken", "anyName");
        });
    }

    @Test
    public void listPassTest() {
        AuthToken token=null;
        try {
            token=facade.registerUser(new User("testname", "testPass", "testemail"));
            facade.createGame(token.getAuthToken(), "TestGame");
            assertNotNull(facade.listGames(token.getAuthToken()));
        } catch (DataAccessException e) {
            System.out.println("register failed: " + e.getMessage());
        }
        AuthToken finalToken=token;
        assertDoesNotThrow(() ->
                facade.listGames(finalToken.getAuthToken())
        );
    }
    @Test
    public void listFailTest() {
        assertThrows(DataAccessException.class, () -> {
            facade.listGames("anytoken");
        });
    }

    @Test
    public void joinPassTest() {
        AuthToken token=null;
        int gameID=0;
        try {
            token=facade.registerUser(new User("testname", "testPass", "testemail"));
            gameID=facade.createGame(token.getAuthToken(), "TestGame");
        } catch (DataAccessException e) {
            System.out.println("register failed: " + e.getMessage());
        }
        AuthToken finalToken=token;
        int finalGameID=gameID;
        assertDoesNotThrow(() ->
                facade.joinGame(finalGameID, "white", "testname" ,finalToken.getAuthToken())
        );
    }
    @Test
    public void observePassTest() {
        AuthToken token=null;
        int gameID=0;
        try {
            token=facade.registerUser(new User("testname", "testPass", "testemail"));
            gameID=facade.createGame(token.getAuthToken(), "TestGame");
        } catch (DataAccessException e) {
            System.out.println("register failed: " + e.getMessage());
        }
        AuthToken finalToken=token;
        int finalGameID=gameID;
        assertDoesNotThrow(() ->
                facade.joinGame(finalGameID, "OBSERVE", "testname" ,finalToken.getAuthToken())
        );
    }
    @Test
    public void joinFailTest() {
        assertThrows(DataAccessException.class, () -> {
            facade.joinGame(1, "white", "username", "badtoken");
        });
    }

}
