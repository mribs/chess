package dataaccess.DAO;

import dataaccess.DataAccessException;
import dataaccess.Database;
import model.Game;

import java.util.*;

public class GameDAO {

  //inserts new game into database
  public Game insert(String gameName) throws DataAccessException {
    //create new game if gameName doesn't already exist
//    if (gameMap.containsKey(gameName)) throw new DataAccessException("bad request");

    Game game = new Game(gameName);
    Database.gameMap.put(game.getGameID(), game);

    return game;
  }

  //finds game by gameID
  public Game find(Integer gameID){
    Game game = Database.gameMap.get(gameID);
    return game;
  }

  //uses player's username to "claim" a spot in a game
  public void claimSpot(Integer gameID, Game game) throws DataAccessException {
    Database.gameMap.replace(gameID, game);
  }

  //updates game moves in database
  public  void updateGame(String gameID) {}

  //removes game from database
  public void remove(Integer gameID) throws DataAccessException {
    Database.gameMap.remove(gameID);
  }

  //clears database
  public void clearGames() {
    Database.gameMap.clear();
  }

  public ArrayList<Game> getGames() {
    Collection<Game> gameList = Database.gameMap.values();
    return new ArrayList<>(gameList);
  }
}
