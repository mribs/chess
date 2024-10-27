package service.results;

public class JoinGameResult {
 String playerColor;
 Integer gameID;

  public JoinGameResult(Integer gameID, String playerColor) {
    this.gameID = gameID;
    this.playerColor = playerColor;
  }

}
