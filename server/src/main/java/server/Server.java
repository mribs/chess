package server;

//imports lifted from web-api instruction

import com.google.gson.Gson;

import dataaccess.DataAccessException;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import io.javalin.*;
import io.javalin.http.Context;

import service.AuthService;
import service.GameService;
import service.UserService;
import service.request.*;
import service.result.*;

import java.util.Map;
import java.util.Objects;

public class Server {

    private final Javalin javalin;
    private UserService userService;
    private AuthService authService;
    private GameService gameService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.exception(Exception.class, this::exceptionHandler);
        javalin.delete("/db", this::clearDatabase);
//        user paths
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);
        javalin.delete("/session", this::logoutUser);
//        game paths
        javalin.post("/game", this::createGame);

        userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        authService = new AuthService(new MemoryAuthDAO());
        gameService = new GameService(new MemoryGameDAO());

    }

    //questionable way of dealing :\
    private void exceptionHandler(Exception e, Context ctx) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        if (Objects.equals(e.getMessage(), "bad request")) {
            ctx.status(400);
        } else if (Objects.equals(e.getMessage(), "already taken")) {
            ctx.status(403);
        } else if (Objects.equals(e.getMessage(), "unauthorized")) {
            ctx.status(401);
        } else {
            ctx.status(500);
        }
        ctx.json(body);
    }

    private void clearDatabase(Context ctx) {
        userService.clearUsers();
        authService.clearAuths();
        gameService.clearGames();
        ctx.status(200);
    }

    private void registerUser(Context ctx) throws DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        LoginResult loginResult = userService.register(registerRequest);
        ctx.json(new Gson().toJson(loginResult));
    }

    private void loginUser(Context ctx) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(ctx.body(), LoginRequest.class);
        LoginResult loginResult = userService.login(loginRequest);
        ctx.json(new Gson().toJson(loginResult));
    }

    private void logoutUser(Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        authService.authorize(authToken);
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        userService.logout(logoutRequest);
        ctx.status(200);
    }

    private void createGame(Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        authService.authorize(authToken);
        CreateGameRequest createGameRequest = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        ctx.json((new Gson().toJson(createGameResult)));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
