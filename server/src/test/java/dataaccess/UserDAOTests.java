package dataaccess;

import dataaccess.sql.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {
    UserDAO userDAO = new SQLUserDAO();
    UserData simpleUser = new UserData("username", "pass", "email");

    @BeforeEach
    void clearUserTable() {
        userDAO.clearUsers();
    }

    @Test
    void createUserPass() {
        userDAO.createUser(simpleUser);
        UserData userDataRetrieved = userDAO.getUser("username");
        assertNotNull(userDataRetrieved);
    }

    @Test
    void createUserFail() {
        assertThrows(RuntimeException.class, () -> {
            userDAO.createUser(new UserData(null, "pass", "email"));
        });
    }

    @Test
    void getUserPass() {
        userDAO.createUser(simpleUser);
        UserData user = userDAO.getUser("username");
        assertNotNull(user);
    }

    @Test
    void getUserFail() {
        assertNull(userDAO.getUser("ghost"));
    }

    @Test
    void verifyPasswordPass() {
        userDAO.createUser(simpleUser);
        UserData expectedPassUser = userDAO.getUser("username");
        assertTrue(userDAO.verifyPassword("pass", expectedPassUser.password()));
    }

    @Test
    void verifyPasswordFail() {
        userDAO.createUser(simpleUser);
        UserData expectedPassUser = userDAO.getUser("username");
        assertFalse(userDAO.verifyPassword("incorrect", expectedPassUser.password()));
    }

    @Test
    void clearUser() {
        userDAO.createUser(simpleUser);
        userDAO.clearUsers();
        assertNull(userDAO.getUser(simpleUser.username()));
    }
}
