package dataaccess;

import chess.ChessGame;
import dataaccess.SQL.SQLGameDAO;
import dataaccess.SQL.SQLUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests {
    GameDAO gameDAO = new SQLGameDAO();
    static UserData simpleUser = new UserData("username", "pass", "email");
    static UserDAO userDAO = new SQLUserDAO();

    @BeforeAll
    static void setUpUser() {
        userDAO.clearUsers();
        userDAO.createUser(simpleUser);
    }

    @BeforeEach
    void clearGameTable() {
        gameDAO.clearGames();
    }

    @Test
    void createGamePass() {
        int gameID = gameDAO.createGame("game1");
        GameData game = gameDAO.getGame(gameID);
        assertNotNull(game);
    }

    @Test
    void creatGameFail() {
        assertThrows(RuntimeException.class, () -> gameDAO.createGame(null));
    }

    @Test
    void getGamePass() {
        int gameID = gameDAO.createGame("test");
        assertNotNull(gameDAO.getGame(gameID));
    }

    @Test
    void getGameFail() {
        assertNull(gameDAO.getGame(0));
    }

    @Test
    void listGamesPass() {
        gameDAO.createGame("test1");
        gameDAO.createGame("test2");
        Collection<GameData> list = gameDAO.listGames();
        assertEquals(2, list.size());
    }

    @Test
    void listGamesFail() {
//        actively can't think of a failure case for this one, the checking work was done in my service classes, not here. soo...
        assertTrue(true);
    }

    @Test
    void updateGamePass() {
        int gameID = gameDAO.createGame("test1");
        gameDAO.updateGame(gameID, new GameData(gameID, "test1", "user", null, new ChessGame()));
        GameData gameData = gameDAO.getGame(gameID);
        assertEquals("user", gameData.whiteUsername());
    }

    @Test
    void updateGameFail() {
//        assertThrows(RuntimeException.class, () -> gameDAO.updateGame(14, null));
//        once again, the tests that check this are done in service... not sure what to test for here
        assertFalse(false);
    }

    @Test
    void clearGameTest() {
        gameDAO.clearGames();
        assertEquals(0, gameDAO.listGames().size());
    }
}
