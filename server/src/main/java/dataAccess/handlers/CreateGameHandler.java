package dataAccess.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import service.services.CreateGameService;
import service.requests.CreateGameRequest;
import service.results.ErrorResult;
import spark.Spark;

public class CreateGameHandler implements Handler{
  private final Gson gson = new Gson();

  @Override
  public void setupRoutes() {
    Spark.post("/game", (req, res) -> {
      CreateGameService createGame = new CreateGameService();
      String authTokenString = req.headers("authorization");
      Authorizer authorizer = new Authorizer();
      authorizer.authorize(authTokenString);

      CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
      return createGame.createGame(request);
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
  }
}
