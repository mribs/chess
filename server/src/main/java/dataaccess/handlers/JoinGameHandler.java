package dataaccess.handlers;

import com.google.gson.Gson;
import dataaccess.*;
import model.DataAccessException;
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
  }
}
