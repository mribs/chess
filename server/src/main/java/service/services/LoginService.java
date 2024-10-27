package service.services;


import dataAccess.DAO.AuthDAO;
import dataAccess.DAO.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedException;
import dataAccess.models.AuthToken;
import dataAccess.models.User;
import service.requests.LoginRequest;
import service.results.LoginResult;

public class LoginService {
  //return request result
  public LoginResult login(LoginRequest loginRequest) throws DataAccessException, UnauthorizedException {
    //pass userName into userDao, get back a user object if exists
    UserDAO userDAO = new UserDAO();
    User user = userDAO.readUser(loginRequest.getUsername());

    if (user == null || loginRequest.getPassword() == null) throw new UnauthorizedException();
    //match passwords if not match throw unauthorized
    if (!loginRequest.getPassword().equals(user.getPassword())) throw new UnauthorizedException();


    //make authToken
    AuthDAO authDAO = new AuthDAO();
    AuthToken authtoken = authDAO.createToken(user.getUsername());
    //into authDAO
    return new LoginResult(authtoken);
  }

}
