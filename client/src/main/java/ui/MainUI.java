package ui;

public class MainUI {
    private final User user;

    public MainUI(String serverUrl) {
        user = new User(serverUrl);
    }
}
