package ui;

import java.util.Scanner;

public class MainUI {
    private final UserUI user;

    public MainUI(String serverUrl) {
        user = new UserUI(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess!");
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.println(user.getHelp());

//        get and handle input
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE + "Enter Option >>");
            String line = scanner.nextLine();

            try {
                result = user.evalLine(line);
                String printResult = result;
                if (result.equals("quit")) {
                    printResult = "See ya real soon!";
                }
                System.out.println((EscapeSequences.SET_TEXT_COLOR_BLUE + printResult));
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println();
    }
}
