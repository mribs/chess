package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.MemoryDatabase;
import model.AuthData;

import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    @Override
    public AuthData createAuth(String username) {
        String authTokenString = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authTokenString, username);
        MemoryDatabase.authMap.put(authTokenString, authData);
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteAuth(String authToken) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearAuths() {
        MemoryDatabase.authMap.clear();
    }

}
