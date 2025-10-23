package server;

import io.javalin.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import io.javalin.http.Context;


public class Server {

    private final Javalin javalin;
    private UserService userService;
    private AuthService authService;
    private GameService gameService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearDatabase);

        // Register your endpoints and exception handlers here.


    }

    private void clearDatabase(Context ctx) {
        userService.clearUsers();
        authService.clearAuths();
        gameService.clearGames();
        ctx.status(204);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
