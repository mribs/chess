package dataaccess.dao.sql;

import dataaccess.*;
import exceptions.BadRequestException;
import model.AuthToken;
import exceptions.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthDAO extends DAO{
  //creates a new authToken object
  public AuthToken createToken(String username) throws DataAccessException, BadRequestException {
    if (username == null) {
      throw new BadRequestException();
    }
    String authTokenString = UUID.randomUUID().toString();
    AuthToken newAuthToken = new AuthToken(username, authTokenString);

    if (newAuthToken == null) {
      throw new DataAccessException("auth token didn't go");
    }

    var statement = "INSERT INTO authToken (username, authToken) VALUES (?, ?)";
    executeUpdate(statement, newAuthToken.getUsername(), newAuthToken.getAuthToken());

    return newAuthToken;
  }

  //reads an authToken object
  public AuthToken readToken(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "SELECT username, authtoken FROM authToken WHERE authToken=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, authToken);
        try (var rs = ps.executeQuery()) {
          if (rs.next()) {
            return readTokenInfo(rs);
          }
        }
      }
    } catch (Exception e) {
      throw new DataAccessException("something went wrong");
    }
    return null;
  }

  private AuthToken readTokenInfo(ResultSet rs) throws SQLException {
    var userName = rs.getString("username");
    var authToken = rs.getString("authtoken");
    return new AuthToken(userName, authToken);
  }
  //delete an authToken object
  public void deleteToken(String authToken) throws DataAccessException {
    try (var conn = DatabaseManager.getConnection()) {
      var statement = "DELETE FROM authToken WHERE authToken=?";
      try (var ps = conn.prepareStatement(statement)) {
        ps.setString(1, authToken);
        ps.executeUpdate();
      }
    } catch (Exception e) {
      throw new DataAccessException("something went wrong");
    }
  }

  public void clearTokens() {
    var statement = "TRUNCATE authToken";
    try {
      executeUpdate(statement);
    } catch (DataAccessException e) {
      throw new RuntimeException(e);
    }
  }
}