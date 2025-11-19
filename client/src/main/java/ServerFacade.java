package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.net.http.HttpClient;
import java.util.List;

public class ServerFacade {
    private final String serverUrl;
    private final HttpClient httpClient;
    private Gson gson;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public void clear() {
    }

    public AuthData register(String username, String password, String email) {
        return null;
    }

    public AuthData login(String username, String password) {
        return null;
    }

    public void logout(String authToken) {
    }

    public GameData createGame(String gameName, String authToken) {
        return null;
    }

    public List listGames(String authtoken) {
        return List.of();
    }

    public GameData joinGame(String test1, String white, String username, String s) {
        return null;
    }
}
