package ui;

import websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import java.util.Scanner;

public class GamePlayUI implements NotificationHandler {

  private GameClient gameClient;

  public GamePlayUI(GameBoard gameBoard, String playerColor, Player player) throws Exception {
    this.gameClient = new GameClient(gameBoard, playerColor, player, this);
  }

  public void run() {
    System.out.println("You've entered GamePlay Mode!");
    System.out.println(gameClient.help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      System.out.println(EscapeSequences.SET_TEXT_BLINKING + "Enter Option >>");
      String line = scanner.nextLine();

      try {
        result = gameClient.evalLine(line);
        String printResult = result;
        if (result.equals("quit")) {
          printResult = "Exiting game mode";
        }
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + printResult);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.println(msg);
      }
    }
    System.out.println();
  }

  @Override
  public void notify(ServerMessage message) {
    switch (message.getServerMessageType()) {
      case NOTIFICATION -> System.out.println(message.getMessage());
      case ERROR -> error(message);
      case LOAD_GAME -> gameClient.updateGame(message.getGame());
    }

  }

  private void error(ServerMessage errorMessage) {
    if (errorMessage.getErrorMessage() == null) {
      System.out.println("there was an error with the server");
      return;
    }
    System.out.println(errorMessage.getErrorMessage());
  }
}
