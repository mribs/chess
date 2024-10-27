package dataAccess.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import service.services.JoinGameService;
import service.requests.JoinGameRequest;
import service.results.ErrorResult;
import service.results.JoinGameResult;
import spark.Spark;

public class JoinGameHandler implements Handler{
  private final Gson gson = new Gson();

  @Override
  public void setupRoutes() {
    Spark.put("/game", (req, res) -> {
      String authTokenString = req.headers("authorization");
      Authorizer authorizer = new Authorizer();
      String username = authorizer.authorize(authTokenString);

      JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
      JoinGameService joinGame = new JoinGameService();
      JoinGameResult joinGameResult = joinGame.join(request, username);
      return joinGameResult;
    }, gson::toJson);
    Spark.exception(DataAccessException.class, (ex, req, res) -> {
      res.status(500);
      ErrorResult result = new ErrorResult("Error: " + ex.getMessage());
      res.body(gson.toJson(result));
    });
    Spark.exception(UnauthorizedException.class, (ex, req, res) -> {
      res.status(401);
      ErrorResult result = new ErrorResult("Error: " + ex.getMessage());
      res.body(gson.toJson(result));
    });
    Spark.exception(BadRequestException.class, (ex, req, res) -> {
      res.status(400);
      ErrorResult result = new ErrorResult("Error: " + ex.getMessage());
      res.body(gson.toJson(result));
    });
    Spark.exception(AlreadyTakenException.class, (ex, req, res) -> {
      res.status(403);
      ErrorResult result = new ErrorResult("Error: " + ex.getMessage());
      res.body(gson.toJson(result));
    });
  }
}
