package dataaccess;

import model.AuthToken;
import model.Game;
import model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
  public static Map<String, User> userMap= new ConcurrentHashMap<>();
  public static Map<Integer, Game>  gameMap = new ConcurrentHashMap<>();
  public static Map<String, AuthToken> authTokenMap = new ConcurrentHashMap<>();

}
