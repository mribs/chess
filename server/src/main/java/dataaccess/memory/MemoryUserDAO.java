package dataaccess.memory;

import dataaccess.UserDAO;

public class MemoryUserDAO implements UserDAO {

    @Override
    public void createUser(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void getUser(String username) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearUsers() {
        throw new UnsupportedOperationException("Not supported yet.");
        // clear users from database... which i don't have yet
    }

}
