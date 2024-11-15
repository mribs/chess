package model;

public class Join {
  int gameID;
  String playerColor;

  public Join(int gameID, String playerColor) {
    this.gameID=gameID;
    if (playerColor != null) {
      this.playerColor=playerColor.toUpperCase();
    }
  }
}
