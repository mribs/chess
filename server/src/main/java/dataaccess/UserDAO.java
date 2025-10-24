package dataaccess;

public interface UserDAO {

    public void createUser(String username, String password);

    public void getUser(String username);

    public void clearUsers();
}
