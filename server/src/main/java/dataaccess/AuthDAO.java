package dataaccess;

public interface AuthDAO {

    //    TODO: correct return types and parameters
    public void createAuth();

    public void getAuth(String authToken);

    public void deleteAuth(String authToken);

    public void clearAuths();
}
