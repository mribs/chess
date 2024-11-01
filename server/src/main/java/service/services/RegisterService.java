package service.services;

import dataaccess.*;
import dataaccess.dao.*;
import model.*;
import service.requests.RegisterRequest;
import service.results.LoginResult;

public class RegisterService {
  //return request result
  public LoginResult register(RegisterRequest registerRequest) throws DataAccessException, BadRequestException, AlreadyTakenException {
    if (registerRequest.getUsername() == null || registerRequest.getEmail() == null || registerRequest.getPassword() == null) throw new BadRequestException();
    //create user object
    UserData user = new UserData(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
    //pass user into createUser in userDAO
    UserDAO userDAO = new UserDAO();
    userDAO.createUser(user);

    //make authToken
    AuthDAO authDAO = new AuthDAO();
    AuthToken authtoken = authDAO.createToken(user.getUsername());
    //into authDAO
    return new LoginResult(authtoken);
  }
}
