package dataAccess.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedException;
import service.services.LoginService;
import service.requests.LoginRequest;
import service.results.ErrorResult;
import service.results.LoginResult;
import spark.Spark;

public class LoginHandler implements Handler{
  private final Gson gson = new Gson();


  @Override
  public void setupRoutes() {
    Spark.post("/session", (req, res) -> {
      LoginService login = new LoginService();
      LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);
      LoginResult loginResult = login.login(request);
      return loginResult;
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
  }
}
