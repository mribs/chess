package service.services;

import dataAccess.AlreadyTakenException;
import dataAccess.BadRequestException;
import dataAccess.DAO.AuthDAO;
import dataAccess.DAO.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.models.AuthToken;
import dataAccess.models.User;
import service.requests.RegisterRequest;
import service.results.LoginResult;

public class RegisterService {
  //return request result
  public LoginResult register(RegisterRequest registerRequest) throws DataAccessException, BadRequestException, AlreadyTakenException {
    if (registerRequest.getUsername() == null || registerRequest.getEmail() == null || registerRequest.getPassword() == null) throw new BadRequestException();
    //create user object
    User user = new User(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
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
