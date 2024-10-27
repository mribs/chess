package dataAccess.handlers;

import com.google.gson.Gson;
import dataAccess.*;
import service.services.ListGamesService;
import service.requests.ListGamesRequest;
import service.results.ErrorResult;
import service.results.ListGamesResult;
import spark.Spark;

public class ListGamesHandler implements Handler{
  private final Gson gson = new Gson();

  @Override
  public void setupRoutes() {
    Spark.get("/game", (req, res) -> {
      String authTokenString = req.headers("authorization");
      Authorizer authorizer = new Authorizer();
      authorizer.authorize(authTokenString);
      ListGamesRequest request = new ListGamesRequest(authTokenString);
      ListGamesService listGames = new ListGamesService();
      ListGamesResult listGamesResult = listGames.listGames(request);
      return listGamesResult;
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
    Spark.exception(AlreadyTakenException.class, (ex, req, res) -> {
      res.status(403);
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
