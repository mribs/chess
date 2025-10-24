package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.request.LoginRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;

import java.util.Objects;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        String email = registerRequest.email();
        String password = registerRequest.password();
        if (username == null || email == null || password == null) {
            throw new DataAccessException("bad request");
        }
//        check if username already exists
        UserData user = userDAO.getUser(username);
        if (user != null) {
            throw new DataAccessException("already taken");
        }
//        create user, then toss into DAO
        user = new UserData(username, password, email);
        userDAO.createUser(user);
//        authData as well
        AuthData authData = authDAO.createAuth(username);
        return new LoginResult(authData.username(), authData.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        if (username == null || password == null) {
            throw new DataAccessException("bad request");
        }
        UserData user = userDAO.getUser(username);
        if (user == null || !Objects.equals(user.password(), password)) {
            throw new DataAccessException("unauthorized");
        }
        AuthData authData = authDAO.createAuth(username);
        return new LoginResult(authData.username(), authData.authToken());

    }

    //     public void logout(LogoutRequest logoutRequest) {
//     }
    public void clearUsers() {
        userDAO.clearUsers();

    }

}
