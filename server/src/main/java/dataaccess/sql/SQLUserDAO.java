package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {
    @Override
    public void createUser(UserData userData) {
        var createStatement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(createStatement)) {
            prepStatement.setString(1, userData.username());
            String hashedPass = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            prepStatement.setString(2, hashedPass);
            prepStatement.setString(3, userData.email());

            prepStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserData getUser(String username) {
        var getStatement = "SELECT username, password, email FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(getStatement)) {
            prepStatement.setString(1, username);
            var result = prepStatement.executeQuery();
            if (result.next()) {
                return new UserData(result.getString("username"), result.getString("password"), result.getString("email"));
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean verifyPassword(String password, String expectedPassword) {
        return BCrypt.checkpw(password, expectedPassword);
    }

    @Override
    public void clearUsers() {
        var clearStatement = "TRUNCATE TABLE user";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(clearStatement)) {
            prepStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}