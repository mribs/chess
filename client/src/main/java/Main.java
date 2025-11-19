import chess.*;
import ui.MainUI;

public class Main {
    public static void main(String[] args) {
//        Probably shouldn't hardcode this, but...
        String serverUrl = "http://localhost:8080";
//       to pass in the url
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new MainUI(serverUrl).run();
    }
}