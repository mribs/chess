package ui;

import java.util.Scanner;

public class MainUI {
  private final Player player;

  public MainUI(String serverUrl) {
    player = new Player(serverUrl);
  }

  public void run() {
    System.out.println("Welcome to Chess!");
    System.out.println(player.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      //TODO: align this better
      System.out.println(EscapeSequences.SET_TEXT_BLINKING + "Enter Option >>");
      String line = scanner.nextLine();

      try {
        result = player.evalLine(line);
        String printResult = result;
        if (result.equals("quit")) {
          printResult = "Thanks for playing!";
        }
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + printResult);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.println(msg);
      }
    }
    System.out.println();
  }


}
