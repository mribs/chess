package service.results;

import dataAccess.models.Game;

import java.util.ArrayList;
import java.util.List;

public class ListGamesResult {
  ArrayList<Game> games = new ArrayList<>();

  public ListGamesResult(ArrayList<Game> games) {
    this.games=games;
  }

  public ArrayList<Game> getGames() {
    return games;
  }
}
