package dataaccess.handlers;

import com.google.gson.Gson;
import service.services.RegisterService;
import service.requests.RegisterRequest;
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
  }
}
