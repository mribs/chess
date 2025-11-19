package ui;

public class MainUI {
    private final UserUI user;

    public MainUI(String serverUrl) {
        user = new UserUI(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess!");
        System.out.println(user.getHelp());
    }
}
