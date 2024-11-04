package dataaccess.dao.memory;

import dataaccess.*;
import model.User;


public class UserDAO {
  //creates new user
  public void createUser(User u) throws AlreadyTakenException, BadRequestException {
    if (u == null || u.getUsername() == null) throw new BadRequestException();
    if (Database.userMap.containsKey(u.getUsername()) ) throw new AlreadyTakenException();

    Database.userMap.put(u.getUsername(), u);
  }

  //returns user information
  public User readUser(String userName) throws DataAccessException {
    if (userName == null) throw new DataAccessException("userName must not be null");

    return Database.userMap.get(userName);
  }

  public Boolean verifyUser(User u, String password) {
    return (u.getPassword().equals(password));
  }


  //deletes user
//  void deleteUser(User u) throws  DataAccessException {
//    Database.userMap.remove(u);
//  }

  //clear users
  public void clearUsers() {
    Database.userMap.clear();
  }
}
