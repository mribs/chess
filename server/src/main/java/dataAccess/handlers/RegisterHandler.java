package dataAccess.handlers;

import com.google.gson.Gson;
import dataAccess.AlreadyTakenException;
import dataAccess.BadRequestException;
import dataAccess.DataAccessException;
import dataAccess.UnauthorizedException;
import service.services.RegisterService;
import service.requests.RegisterRequest;
import service.results.ErrorResult;
import service.results.LoginResult;
import spark.Spark;

public class RegisterHandler implements Handler {
  private final Gson gson = new Gson();

  @Override
  public void setupRoutes() {
    Spark.post("/user", (req, res) -> {
      RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
      RegisterService register = new RegisterService();

      LoginResult registerResult=register.register(request);
      return registerResult;

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
