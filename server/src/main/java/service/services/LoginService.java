package service.services;


import dataaccess.dao.sql.*;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import model.AuthToken;
import exceptions.DataAccessException;
import model.User;
import service.requests.LoginRequest;
import service.results.LoginResult;

public class LoginService {
  //return request result
  public LoginResult login(LoginRequest loginRequest) throws DataAccessException, UnauthorizedException, BadRequestException {
    //pass userName into userDao, get back a user object if exists
    UserDAO userDAO = new UserDAO();
    User user = userDAO.readUser(loginRequest.getUsername());
    if (user == null || loginRequest.getPassword() == null) {
      throw new UnauthorizedException();
    }
    //match passwords if not match throw unauthorized
    if (!userDAO.verifyUser(user, loginRequest.getPassword())) {
      throw new UnauthorizedException();
    }
    //make authToken
    AuthDAO authDAO = new AuthDAO();
    AuthToken authtoken = authDAO.createToken(user.getUsername());
    //into authDAO
    return new LoginResult(authtoken);
  }

}
