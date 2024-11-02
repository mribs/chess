package dataaccess;

import dataaccess.dao.sql.UserDAO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {
  UserDAO userDAO = new UserDAO();
  @BeforeEach
  void clearUserTable() {
    userDAO.clearUsers();
  }

  @Test
  void createUserTest() {
        try {
          userDAO.createUser(new User("testName", "testPass", "testEmail"));
        } catch (BadRequestException e) {
          throw new RuntimeException(e);
        } catch (AlreadyTakenException e) {
          throw new RuntimeException(e);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }

        try {
          User testUser = userDAO.readUser("testName");
          assertNotEquals(null, testUser);
        } catch (DataAccessException e) {
          throw new RuntimeException(e);
        }
  }
  @Test
  void dontCreateUserTest() {
    assertThrows(BadRequestException.class, () -> {
      userDAO.createUser(null);
    });
  }

  @Test
  void readUserTest() throws BadRequestException, AlreadyTakenException, DataAccessException {
    userDAO.createUser(new User("test", "pass", "email"));

    User user = userDAO.readUser("test");
    assertEquals("test", user.getUsername());
  }
  @Test
  void readnullUserTest() throws DataAccessException {
      assertNull(userDAO.readUser("i'm not real"));
  }

  @Test
  void clearUserTest() {
    assertDoesNotThrow(() -> userDAO.clearUsers());
  }



}