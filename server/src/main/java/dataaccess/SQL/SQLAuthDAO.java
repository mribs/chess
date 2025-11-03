package dataaccess.SQL;

import dataaccess.AuthDAO;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth(String username) {
        return null;
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
