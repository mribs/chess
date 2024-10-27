package dataAccess.handlers;

import com.google.gson.Gson;
import service.services.ClearService;
import service.results.ClearResult;
import spark.Spark;

public class ClearHandler implements Handler{
  private final Gson gson = new Gson();

  @Override
  public void setupRoutes() {
    Spark.delete("/db", (req, res) -> {
      ClearService clear = new ClearService();
      clear.clear();
      return new ClearResult("Database Cleared");
    }, gson::toJson);
  }
}
