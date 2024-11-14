package dataaccess.dao.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class DAO {
   int executeUpdate(String statement, Object... params) throws DataAccessException {
    try (var conn =DatabaseManager.getConnection()) {
      try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
        for (var i = 0; i < params.length; i++) {
          var param = params[i];
          if (param instanceof String p) {
            ps.setString(i + 1, p);
          }
          else if (param == null) {
            ps.setNull((i + 1), NULL);
          }
        }
        ps.executeUpdate();

        var rs = ps.getGeneratedKeys();
        if (rs.next()) {
          return rs.getInt(1);
        }
        return 0;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}