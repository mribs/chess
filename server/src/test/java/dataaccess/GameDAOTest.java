package dataaccess;

import dataaccess.dao.sql.GameDAO;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {
  GameDAO gameDAO = new GameDAO();

  @BeforeEach
  void clearGameTable() {
    gameDAO.clearGames();
  }

  @Test
  void createGameTest() {
    String gameName = "Test Game";

    assertDoesNotThrow(() -> gameDAO.insert(gameName));
  }
  @Test
  void dontCreateGameTest() {
    assertThrows(BadRequestException.class, () -> {
      gameDAO.insert(null);
    });
  }

  @Test
  void readGameTest() throws DataAccessException, BadRequestException {
    GameDAO gameDAO = new GameDAO();
    Game testGame = gameDAO.insert("testGame");

    Game readGame = assertDoesNotThrow(() -> gameDAO.find(testGame.getGameID()));
    assertEquals(testGame, readGame);
  }
  @Test
  void cantFindGameTest() throws DataAccessException {
    assertNull(gameDAO.find(0));
  }

  @Test
  void joinGameTest() throws BadRequestException, DataAccessException {
    Game game = gameDAO.insert("testGame");
    int id = game.getGameID();
    game.setWhiteUsername("testUser");

    assertDoesNotThrow(() -> gameDAO.claimSpot(id, game));
  }

  @Test
  void badJoinGameTest() {
    assertThrows(DataAccessException.class, () -> {
      gameDAO.claimSpot(0, new Game("doesn't matter"));
    });
  }

  @Test
  void clearGameTest() {
    assertDoesNotThrow(() -> gameDAO.clearGames());
  }

  @Test
  void listGamesTest() throws BadRequestException, DataAccessException {
    int numGames = 4;
    gameDAO.insert("1");
    gameDAO.insert("2");
    gameDAO.insert("3");
    gameDAO.insert("4");

    assertDoesNotThrow(() -> System.out.println((gameDAO.getGames())));
    assertEquals(numGames,gameDAO.getGames().size());
  }

  @Test
  void listEmptyGamesTest() {
    var games = gameDAO.getGames();
    assertEquals(0, games.size());
  }
}