package dataaccess.dao.sql;

import dataaccess.*;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO extends DAO {
  //creates new user
  public void createUser(User user) throws AlreadyTakenException, BadRequestException, DataAccessException {
    if (user == null || user.getUsername() == null) throw new BadRequestException();
    //check to see if already exists
    User testUser = readUser(user.getUsername());
    if (testUser != null) {
      throw  new AlreadyTakenException();
    }

    var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
    executeUpdate(statement, user.getUsername(), user.getPassword(), user.getEmail());

  }

  //returns user information
  public User readUser(String userName) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, password, email FROM user WHERE username=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, userName);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readUserInfo(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("something went wrong");
    }
    return null;
  }
  private User readUserInfo(ResultSet rs) throws SQLException {
    var userName = rs.getString("username");
    var password = rs.getString("password");
    var email = rs.getString("email");
    return new User(userName, password, email);
  }

  //updates user information
//  void updateUser(User u) throws DataAccessException{
//  }

  //deletes user
  void deleteUser(User u) throws  DataAccessException {
    var statement = "DELETE FROM user WHERE username=?";
    executeUpdate(statement, u.getUsername());
  }

  //clear users
  public void clearUsers() {
    var statement = "TRUNCATE user";
    try {
      executeUpdate(statement);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
