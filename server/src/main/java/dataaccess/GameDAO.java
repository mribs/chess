package dataaccess;

import model.GameData;
import service.result.CreateGameResult;

import java.util.Collection;

public interface GameDAO {

    public int createGame(String gameName);

    public GameData getGame(int gameID);

    public Collection<GameData> listGames();

    public void updateGame(int gameID, GameData gameUpdate);

    public void clearGames();

}
