package service.requests;

public class JoinGameRequest {
  Integer gameID;
  String playerColor;

  public JoinGameRequest(Integer gameID, String playerColor) {
    this.gameID=gameID;
    this.playerColor=playerColor.toUpperCase();
  }
  public JoinGameRequest(Integer gameID) {
    this.gameID = gameID;
    this.playerColor = "OBSERVER";
  }

  public Integer getGameID() {
    return gameID;
  }

  public void setGameID(Integer gameID) {
    this.gameID=gameID;
  }

  public String getPlayerColor() {
    return playerColor;
  }

  public void setPlayerColor(String playerColor) {
    this.playerColor=playerColor;
  }
}

