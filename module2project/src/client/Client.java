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
import java.util.Arrays;
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
        System.out.println("Received command: " + text);
        if (!(text == null) && !text.equals("\n")) {
          checkWelcome(text);
          checkNames(text);
          checkTurn(text);
          checkNext(text);
          checkNew(text);
          checkKick(text);
          checkWinner(text);
        }
      }
    } catch (IOException e) {
      System.out.println("ÏOException");
    } catch (InvalidCommandException e) {
      System.out.println(e);
    }
  }
  
  public void checkNew(String text) {
    String[] command = text.split(" ");
    if (text.startsWith(Game.NEW) && command.length <= 8) {
      if (command.length == 2 && command[2].equals("empty")) {
        System.out.println("There are no tiles left in the pool.");
      } else {
        for (int i = 0; i < command.length - 1; i++) {
          String colour = command[i].substring(0, 1);
          String shape = command[i].substring(1, 2);
          Tile tile = new Tile(colour, shape);
          try {
            game.getPlayer().addToHand(tile);
            System.out.println("Added to hand: " + tile.toString() + ". Current hand: " + game.getPlayer().handToString());
          } catch (HandIsFullException e) {
            System.out.println(e);
          }
          game.setPool(game.getPool() - 1);
        }
      }
    }
  }
  
  public void checkKick(String text) {
    String[] command = text.split(" ");
    int tilesBackToPool = 0;
    int playerNumber = -1;
    int i = 0;
    if (text.startsWith(Game.KICK)) {
      try {
        playerNumber = Integer.parseInt(command[1]);
        tilesBackToPool = Integer.parseInt(command[2]);
      } catch (NumberFormatException e) {
        System.out.println("Not a number. KICK");
      }
      while (i < tilesBackToPool) {
        game.setPool(game.getPool() + 1);
        i++;
      }
      System.out.println(getPlayerName(playerNumber) +  " IS KICKED FOR THE FOLLOWING REASON:" + text.substring(7));
    }
  }
  
  public void checkWinner(String text) {
    String[] command = text.split(" ");
    int playerNumber = -1;
    if (text.startsWith(Game.WINNER)) {
      try {
        playerNumber = Integer.parseInt(command[1]);
      } catch (NumberFormatException e) {
        System.out.println("Not a number. WINNER.");
      }
      System.out.println("The winner is... " + getPlayerName(playerNumber) + "!");
      shutdown();
      //CLOSE GAME!
    }
  }
  
  public void checkTurn(String text) throws InvalidCommandException {
    String[] command = text.split(" ");
    int playerNumber = -1;
    List<Move> moves = new ArrayList<>();
    if (text.startsWith(Game.TURN)) {
      try {
        playerNumber = Integer.parseInt(command[2]);
      } catch (NumberFormatException e) {
        System.out.println("Not a number. TURN1");
      }
      if (command.length == 3 && command[3].equals("empty")) {
        System.out.println(getPlayerName(playerNumber) + " swapped." );
      } else if (command.length <= 20 && ((command.length - 2) % 3 == 0)) {
        for (int i = 0; i < ((command.length - 2) / 3); i++ ) {
          int row = 0;
          int column = 0;
          try {
            row = Integer.parseInt(command[(3 * i) + 2]);
            column = Integer.parseInt(command[(3 * i) + 3]);
          } catch (NumberFormatException e) {
            System.out.println("Not a number. TURN2");
          }
          String colour = command[(3 * i) + 2].substring(0, 1);
          String shape = command[(3 * i) + 2].substring(1, 2);
          moves.add(new Move(new Tile(colour, shape), row, column));
        }
      } else {
        throw new InvalidCommandException("(In TURN).");
      } 
      if (playerNumber != game.getPlayer().getPlayerNumber()) {
        game.opponentTurn(moves);
      }
    }  
  }
  
  public void checkNext(String text) throws InvalidCommandException {
    String[] command = text.split(" ");
    int playerNumber = -1;
    if (text.startsWith(Game.NEXT) && command.length == 2) {
      try {
        playerNumber = Integer.parseInt(command[1]);
      } catch (NumberFormatException e) {
        System.out.println("Not a number. NEXT");
      }
      if (playerNumber == game.getPlayer().getPlayerNumber()) {
        game.playerTurn();
      } else {
        String playerName = getPlayerName(playerNumber);
        System.out.println("playerName: " + playerName);
        System.out.println(playerName + "'s turn.");
      }
    }  
  }  
  
  public void checkNames(String text) throws InvalidCommandException {
    String[] command = text.split(" ");
    if (text.startsWith(Game.NAMES) 
        && (command.length == 6 || command.length == 8 || command.length == 10)) {
      int numberOfPlayers = (command.length - 2) / 2;
      for (int i = 0; i < numberOfPlayers; i++) {
        String name = command[(2 * i) + 1];
        String number = command[(2 * i) + 2];
        addPlayer(name, number);
      }
      System.out.println("Players participating: " + game.getPlayerList());
    }  
  }

  public void checkWelcome(String text) throws InvalidCommandException {
    String[] command = text.split(" ");
    int playerNumber = -1;
    if (text.startsWith(Game.WELCOME) && command.length == 3) {
      try {
        playerNumber = Integer.parseInt(command[2]);
      } catch (NumberFormatException e) {
        System.out.println("Not a number. WELCOME");
      }
      if (game.getPlayerType() == "h") {
        Player player = new HumanPlayer(command[1], playerNumber);
        game.setPlayer(player);
      }
      if (game.getPlayerType() == "b") {
        Player player = new ComputerPlayer(command[1], playerNumber, new NaiveStrategy());
        game.setPlayer(player);
      }
      System.out.println("Welcome message received.");
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
  
  /** send a message to a ClientHandler. */
  public void sendMessage(String msg) {
    try {
      out.write(msg);
      out.newLine();
      out.flush();
    } catch (IOException e) {
      System.out.println("lostuh connectionuh");
    }

  }
  /** close the socket connection. */
  public void shutdown() {
    System.out.println("Closing socket connection...");
    try {
      sock.close();
    } catch (IOException e) {
      System.out.println("could not close socket");
    }
  }

  /** returns the client name */
  public String getClientName() {
    return clientName;
  }

 
}
