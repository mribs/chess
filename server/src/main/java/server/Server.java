package server;

//imports lifted from web-api instruction

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;

import service.AuthService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    private UserService userService;
    private AuthService authService;
    private GameService gameService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

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
