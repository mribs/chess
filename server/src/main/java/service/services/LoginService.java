package service.services;


import dataaccess.*;
import dataaccess.dao.sql.AuthDAO;
import dataaccess.dao.sql.UserDAO;
import model.AuthToken;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import service.requests.LoginRequest;
import service.results.LoginResult;

public class LoginService {
  //return request result
  public LoginResult login(LoginRequest loginRequest) throws DataAccessException, UnauthorizedException, BadRequestException {
    //pass userName into userDao, get back a user object if exists
    UserDAO userDAO = new UserDAO();
    User user = userDAO.readUser(loginRequest.getUsername());
    if (user == null) throw new UnauthorizedException();
    String hashedPassword = user.getPassword();
    String enteredPassword =loginRequest.getPassword();

    if (user == null || loginRequest.getPassword() == null) throw new UnauthorizedException();
    //match passwords if not match throw unauthorized
    if (!BCrypt.checkpw(enteredPassword, hashedPassword)) throw new UnauthorizedException();

    //make authToken
    AuthDAO authDAO = new AuthDAO();
    AuthToken authtoken = authDAO.createToken(user.getUsername());
    //into authDAO
    return new LoginResult(authtoken);
  }

}
