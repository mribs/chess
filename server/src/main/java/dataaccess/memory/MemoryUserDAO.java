package dataaccess.memory;

import dataaccess.MemoryDatabase;
import dataaccess.UserDAO;
import model.UserData;

import java.util.Objects;

public class MemoryUserDAO implements UserDAO {

    @Override
    public void createUser(UserData userData) {
        MemoryDatabase.userMap.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return MemoryDatabase.userMap.getOrDefault(username, null);
    }

    @Override
    public boolean verifyPassword(String password, String expectedPassword) {
        return Objects.equals(password, expectedPassword);
    }


    @Override
    public void clearUsers() {
        // clear users from database... which i don't have yet
        MemoryDatabase.userMap.clear();
    }

}
