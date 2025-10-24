package dataaccess;


import model.UserData;

public interface UserDAO {

    public void createUser(UserData userData);

    public UserData getUser(String username);

    public void clearUsers();
}
