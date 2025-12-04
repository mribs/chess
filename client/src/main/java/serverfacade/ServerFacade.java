package serverfacade;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.util.Collection;
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
        var path = "/db";
        this.makeRequest("DELETE", path);
    }

    public AuthData register(String username, String password, String email) {
        var path = "/user";
        UserData userData = new UserData(username, password, email);
        return this.makeRequest("POST", path, userData, AuthData.class);
    }

    public AuthData login(String username, String password) {
        var path = "/session";
        UserData userData = new UserData(username, password);
        return this.makeRequest("POST", path, userData, AuthData.class);
    }

    public void logout(String authToken) {
        var path = "/session";
        this.makeRequest("DELETE", path, null, authToken, null);
    }

    public GameData createGame(String gameName, String authToken) {
        GameData game = new GameData(0, gameName, null, null, new ChessGame());
        var path = "/game";
        return this.makeRequest("POST", path, game, authToken, GameData.class);
    }

    public List<GameData> listGames(String authToken) {
        var path = "/game";
        ListGamesResult result = this.makeRequest("GET", path, null, authToken, ListGamesResult.class);
        return result.games();
    }

    public GameData joinGame(int gameID, String playerColor, AuthData authData) {
        var path = "/game";
        playerColor = playerColor.toUpperCase();
        JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID);
        return this.makeRequest("PUT", path, joinGameRequest, authData.authToken(), GameData.class);
    }

    //    these are largely copied from when I last did this project because I don't want to re-write them
//    makeRequest no data
    private void makeRequest(String method, String path) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.connect();
            throwIfNotSuccessful(http);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    //    makeRequest no authorization
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    //make request with authorization
    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setRequestProperty("authorization", authToken);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new RuntimeException("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response;
        if (responseClass == null) {
            return null;
        }
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(respBody);
            response = new Gson().fromJson(reader, responseClass);
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
