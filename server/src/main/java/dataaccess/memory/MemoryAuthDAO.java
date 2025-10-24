package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.MemoryDatabase;

public class MemoryAuthDAO implements AuthDAO {

    @Override
    public void createAuth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getAuth(String authToken) {
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
