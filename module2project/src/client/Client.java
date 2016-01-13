package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;

import exceptions.HandIsFullException;
import exceptions.InvalidCommandException;

/**
 * Client class for a simple client-server application.
 * 
 * @author Han
 */
public class Client extends Thread {
  private static final String USAGE = "usage: java week7.cmdchat.Client <name> <address> <port>";

  private String clientName;
  private Socket sock;
  private BufferedReader in;
  private BufferedWriter out;
  private Game game;
  

  /**
   * Constructs a Client-object and tries to make a socket connection
   */
  public Client(String name, InetAddress host, int port, Game game) throws IOException {
    clientName = name;
    sock = new Socket(host, port);
    this.game = game;
    in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
  }

  /**
   * Reads the messages in the socket connection. Each message will be forwarded to the MessageUI
   */
  public synchronized void run() {
    String text = "";
    try {
      while (text != null) {
        text = in.readLine();
        if (!(text == null) && !text.equals("\n")) {
          checkWelcome(text);
          checkNames(text);
          checkNext(text);
          checkTurn(text);
        }
      }
    } catch (IOException e) {
      System.out.println("ÏOException");
    } catch (InvalidCommandException e) {
      System.out.println(e);
    }
  }
  
  public void checkTurn(String text) throws InvalidCommandException {
    boolean validCommand = false;
    String[] command = text.split(" ");
    int playerNumber = -1;
    List<Move> moves = new ArrayList<>();
    if (text.startsWith(Game.TURN)) {
      if (command.length == 3 && command[3].equals("empty")) {
        System.out.println(getPlayerName(2) + " swapped." );
      } else if (command.length < 20 && ((command.length - 2) % 3 == 0)) {
        for (int i = 0; i < ((command.length - 2) / 3); i++ ) {
          int row = 0;
          int column = 0;
          try {
            row = Integer.parseInt(command[(3 * i) + 2]);
            column = Integer.parseInt(command[(3 * i) + 3]);
          } catch (NumberFormatException e) {
            System.out.println("Not a number.");
          }
          String colour = command[(3 * i) + 2].substring(0, 0);
          String shape = command[(3 * i) + 2].substring(1, 1);
          moves.add(new Move(new Tile(colour, shape), row, column));
        }
      } else {
        throw new InvalidCommandException("(In TURN).");
      }
    } else {
      throw new InvalidCommandException("(In TURN).");
    }
    game.opponentTurn(moves);
  }
  
  public void checkNext(String text) throws InvalidCommandException {
    boolean validCommand = false;
    String[] command = text.split(" ");
    int playerNumber = -1;
    if (text.startsWith(Game.NEXT) && command.length == 2) {
      try {
        playerNumber = Integer.parseInt(command[1]);
      } catch (NumberFormatException e) {
        System.out.println("Not a valid number.");
      }
      if (playerNumber == game.getPlayer().getPlayerNumber()) {
        game.playerTurn();
      } else {
        String playerName = getPlayerName(playerNumber);
        System.out.println(playerName + "'s turn.");
      }
    } else {
      throw new InvalidCommandException("(In NEXT).");
    }
  }  
  
  public String getPlayerName(int playerNumber) {
    String playerName = "";
    for (Player player : game.getPlayerList()) {
      if (playerNumber == player.getPlayerNumber()) {
        playerName = player.getName();
      }
    }
    return playerName;
  }
  
  public void checkNames(String text) throws InvalidCommandException {
    boolean validCommand = false;
    String[] command = text.split(" ");
    if (text.startsWith(Game.NAMES) 
        && (command.length == 6 || command.length == 8 || command.length == 10)) {
      int numberOfPlayers = (command.length - 2) / 2;
      for (int i = 0; i < numberOfPlayers; i++) {
        String name = command[(2 * i) + 1];
        String number = command[(2 * i) + 2];
        addPlayer(name, number);
      }
    } else {
      throw new InvalidCommandException("(In NAMES).");
    }
  }
  
  public void addPlayer(String name, String number) {
    int playerNumber = -1;
    try {
      playerNumber = Integer.parseInt(number);
    } catch (NumberFormatException e) {
      System.out.println("Not a valid number.");
    }
    Player player1 = new OpponentPlayer(name, playerNumber);
    game.addPlayerToList(player1);
  }
  
  public void checkWelcome(String text) throws InvalidCommandException {
    boolean validCommand = false;
    String[] command = text.split(" ");
    int playerNumber = -1;
    if (text.startsWith(Game.WELCOME) && command.length == 3) {
      try {
        playerNumber = Integer.parseInt(command[2]);
      } catch (NumberFormatException e) {
        System.out.println("Not a valid number.");
      }
      if (game.getPlayerType() == "h") {
        Player player = new HumanPlayer(command[1], playerNumber);
        game.setPlayer(player);
      }
      if (game.getPlayerType() == "b") {
        Player player = new ComputerPlayer(command[1], playerNumber, new NaiveStrategy());
        game.setPlayer(player);
      }
    } else {
      throw new InvalidCommandException("(In WELCOME).");
    }
  }
  
  /** send a message to a ClientHandler. */
  public void sendMessage(String msg) {
    try {
      out.write(msg);
      out.newLine();
      out.flush();
    } catch (IOException e) {
      print("lostuh connectionuh");
    }

  }
  /** close the socket connection. */
  public void shutdown() {
    print("Closing socket connection...");
    try {
      sock.close();
    } catch (IOException e) {
      print("could not close socket");
    }
  }

  /** returns the client name */
  public String getClientName() {
    return clientName;
  }

  private static void print(String message) {
    System.out.println(message);
  }

  public static String readString(String tekst) {
    System.out.print(tekst);
    String antw = null;
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      antw = in.readLine();
    } catch (IOException e) {
    }

    return (antw == null) ? "" : antw;
  }

 
}
