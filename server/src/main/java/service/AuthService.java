package service;

import dataaccess.AuthDAO;

public class AuthService {

    private AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void clearAuths() {
        authDAO.clearAuths();
    }
}
