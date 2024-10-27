package dataAccess.handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedException;
import service.services.LogoutService;
import service.results.ClearResult;
import service.results.ErrorResult;
import spark.Spark;

public class LogoutHandler implements Handler{
  private final Gson gson = new Gson();
  @Override
  public void setupRoutes() {
    Spark.delete("/session", (req, res) -> {
      LogoutService logout = new LogoutService();
      String authTokenString = req.headers("authorization");
      if (authTokenString == null) throw new UnauthorizedException();
      logout.logOut(authTokenString);
      return new ClearResult("User Logged Out");
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
