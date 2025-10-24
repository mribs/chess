package dataaccess;

import model.AuthData;

public interface AuthDAO {

    //    TODO: correct return types and parameters
    public AuthData createAuth(String username);

    public AuthData getAuth(String authToken);

    public void deleteAuth(String authToken);

    public void clearAuths();
}
