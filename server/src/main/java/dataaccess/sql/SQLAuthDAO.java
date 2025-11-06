package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData createAuth(String username) {
        String authTokenString = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authTokenString, username);
        var authStatement = "INSERT INTO authToken (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var statement = conn.prepareStatement(authStatement)) {
            statement.setString(1, authData.username());
            statement.setString(2, authData.authToken());

            statement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return authData;
    }

    @Override
    public AuthData getAuth(String authToken) {
        var statement = "SELECT username, authtoken FROM authToken WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(statement)) {
            prepStatement.setString(1, authToken);
            var result = prepStatement.executeQuery();
            if (result.next()) {
                String username = result.getString("username");
                return new AuthData(authToken, username);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("SQL statement probably wrong (get auth)");
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        var delStatement = "DELETE FROM authToken WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(delStatement)) {
            prepStatement.setString(1, authToken);
            prepStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearAuths() {
        var clearStatement = "TRUNCATE TABLE authToken";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(clearStatement)) {
            prepStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
