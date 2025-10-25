package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class AuthService {

    private AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void authorize(String authToken) throws DataAccessException {
        if (authDAO.getAuth(authToken) == null) {
            throw new DataAccessException("unauthorized");
        }
    }

    public String getUsername(String authToken) {
        AuthData authData = authDAO.getAuth(authToken);
        return authData.username();
    }

    public void clearAuths() {
        authDAO.clearAuths();
    }
}
