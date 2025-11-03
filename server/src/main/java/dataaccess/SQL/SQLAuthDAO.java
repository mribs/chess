package dataaccess.SQL;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth(String username) {
        String authTokenString = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authTokenString, username);
        
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clearAuths() {

    }
}
