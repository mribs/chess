package dataaccess;


import model.DataAccessException;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME= props.getProperty("db.name");
                USER= props.getProperty("db.user");
                PASSWORD= props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL= String.format("jdbc:mysql://%s:%d", host, port);

                configureDatabase();
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var useStatement = "USE " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(useStatement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw new DataAccessException(e.getMessage());
        }
    }
    static void createTables() {
        try (var conn = DatabaseManager.getConnection()){
            var createUserTable="""
                    CREATE TABLE IF NOT EXISTS user (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    PRIMARY KEY (username)
                    )""";
            var createAuthTokenTable="""
                    CREATE TABLE IF NOT EXISTS authToken (
                    username VARCHAR(255) NOT NULL,
                    authToken VARCHAR(255) NOT NULL,
                    PRIMARY KEY (authToken)
                    )""";
            var createGameTable="""
                    CREATE TABLE IF NOT EXISTS game (
                    gameID INT NOT NULL,
                    gameName VARCHAR(255) NOT NULL,
                    game TEXT NOT NULL,
                    whiteUsername VARCHAR(255) DEFAULT NULL,
                    blackUsername VARCHAR(255) DEFAULT NULL,
                    observers TEXT DEFAULT NULL,
                    PRIMARY KEY (gameID)
                    )""";
            try (var createUserTableStatement=conn.prepareStatement(createUserTable)) {
                createUserTableStatement.executeUpdate();
            }
            try (var createAuthTableStatement=conn.prepareStatement(createAuthTokenTable)) {
                createAuthTableStatement.executeUpdate();
            }
            try (var createGameTableStatement=conn.prepareStatement(createGameTable)) {
                createGameTableStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            createTables();
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
