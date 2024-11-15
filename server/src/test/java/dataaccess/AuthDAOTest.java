package dataaccess;

import dataaccess.dao.sql.AuthDAO;
import model.AuthToken;
import model.DataAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {
  @BeforeEach
  void clearAuthtokenTable() {
    AuthDAO authDAO = new AuthDAO();
    authDAO.clearTokens();
  }

  @Test
  void createTokenTest() {
    AuthDAO authDAO = new AuthDAO();
    String userName = "TestUserName";

    assertDoesNotThrow(() -> authDAO.createToken(userName));
  }

  @Test
  void failCreateTokenTest() {
    AuthDAO authDAO = new AuthDAO();
    assertThrows(BadRequestException.class, () -> {
      authDAO.createToken(null);
    });
  }

  @Test
  void readTokenTest() throws DataAccessException, BadRequestException {
    AuthDAO authDAO = new AuthDAO();
    AuthToken authToken = authDAO.createToken("testUserName");

    assertNotNull(authDAO.readToken(authToken.getAuthToken()));
  }
  @Test
  void failReadTokenTest() throws DataAccessException {
    AuthDAO authDAO = new AuthDAO();
    assertNull(authDAO.readToken("doesn'tExist"));
  }

  @Test
  void clearTokenTest() {
    AuthDAO authDAO = new AuthDAO();
    assertDoesNotThrow(() -> authDAO.clearTokens());
  }

  @Test
  void removeTokenTest() throws BadRequestException, DataAccessException {
    AuthDAO authDAO = new AuthDAO();
    AuthToken testToken = authDAO.createToken("testUser");

    assertDoesNotThrow(() -> authDAO.deleteToken(testToken.getAuthToken()));
  }

}