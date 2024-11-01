package server;

import dataaccess.handlers.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Handler clearHandler = new ClearHandler();
        Handler createHandler = new CreateGameHandler();
        Handler joinHandler = new JoinGameHandler();
        Handler listHandler = new ListGamesHandler();
        Handler loginHandler = new LoginHandler();
        Handler logoutHandler = new LogoutHandler();
        Handler registerHandler = new RegisterHandler();

        clearHandler.setupRoutes();
        createHandler.setupRoutes();
        joinHandler.setupRoutes();
        listHandler.setupRoutes();
        loginHandler.setupRoutes();
        logoutHandler.setupRoutes();
        registerHandler.setupRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
