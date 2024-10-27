package dataAccess;

import dataAccess.models.AuthToken;
import dataAccess.models.Game;
import dataAccess.models.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
  public static Map<String, User> userMap= new ConcurrentHashMap<>();
  public static Map<Integer, Game>  gameMap = new ConcurrentHashMap<>();
  public static Map<String, AuthToken> authTokenMap = new ConcurrentHashMap<>();

}
