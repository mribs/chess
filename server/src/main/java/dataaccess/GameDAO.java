package dataaccess;

import service.result.CreateGameResult;

public interface GameDAO {

    //   FIXME:add correct returns and fix variable types
    public int createGame(String gameName);

    public void getGame(String gameID);

    public void listGames();

    public void updateGame(String gameID, String game);

    public void clearGames();

}
