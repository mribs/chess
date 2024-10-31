package service.results;

import model.Game;

import java.util.ArrayList;

public class ListGamesResult {
  ArrayList<Game> games = new ArrayList<>();

  public ListGamesResult(ArrayList<Game> games) {
    this.games=games;
  }

  public ArrayList<Game> getGames() {
    return games;
  }
}
