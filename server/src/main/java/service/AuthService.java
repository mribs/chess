package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

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

    public void clearAuths() {
        authDAO.clearAuths();
    }
}
