package dataaccess;

import dataaccess.SQL.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests {
    AuthDAO authDAO = new SQLAuthDAO();

    @Test
    void createAuthPass() {
        AuthData authData = authDAO.createAuth("username");
        assertNotNull(authData.authToken());
    }

    @Test
    void createAuthFail() {
        assertThrows(RuntimeException.class, () -> authDAO.createAuth(null));
    }

    @Test
    void getAuthPass() {
        AuthData authData = authDAO.createAuth("user");
        AuthData returnedAuth = authDAO.getAuth(authData.authToken());
        assertEquals("user", returnedAuth.username());
    }

    @Test
    void getAuthFail() {
        AuthData returnedAuth = authDAO.getAuth(null);
        assertNull(returnedAuth);
    }

    @Test
    void deleteAuthPass() {
        AuthData authData = authDAO.createAuth("username");
        authDAO.deleteAuth(authData.authToken());
        assertNull(authDAO.getAuth(authData.authToken()));
    }

    @Test
    void deleteAuthFail() {
//        lowkey it will just delete it again, won't double check to make sure something's there 'cause you're trying to get rid of it anyway
        AuthData authData = authDAO.createAuth("username");
        authDAO.deleteAuth(authData.authToken());
        assertDoesNotThrow(() -> authDAO.deleteAuth(authData.authToken()));
    }

    @Test
    void clearTest() {
        AuthData authData = authDAO.createAuth("username");
        AuthData authData2 = authDAO.createAuth("user2");
        authDAO.clearAuths();
        assertNull(authDAO.getAuth(authData.authToken()));
        assertNull(authDAO.getAuth(authData2.authToken()));
    }
}