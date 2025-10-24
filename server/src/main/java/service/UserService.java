package service;

import dataaccess.UserDAO;

public class UserService {

    UserDAO userDAO;

    // public LoginResult register(RegisterRequest registerRequest) {
    //     return null;
    // }
    // public LoginResult login(LoginRequest loginRequest) {
    //     return null;
    // }
    // public void logout(LogoutRequest logoutRequest) {
    // }
    public void clearUsers() {
        userDAO.clearUsers();

    }

}
