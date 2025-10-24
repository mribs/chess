package server;

//imports lifted from web-api instruction
import com.google.gson.Gson;

import io.javalin.*;
import io.javalin.http.Context;
import model.UserData;

import service.AuthService;
import service.GameService;
import service.UserService;
import service.request.*;

public class Server {

    private final Javalin javalin;
    private UserService userService;
    private AuthService authService;
    private GameService gameService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", this::clearDatabase);
        javalin.post("/user", this::registerUser);

    }

    private void clearDatabase(Context ctx) {
        userService.clearUsers();
        authService.clearAuths();
        gameService.clearGames();
        ctx.status(204);
    }

    private void registerUser(Context ctx) {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        user = userService.addUser(user);
        ctx.json(new Gson().toJson(pet));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
