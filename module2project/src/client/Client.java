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
        }
      }
    } catch (IOException e) {
      System.out.println("ÏOException");
    }
  }
  
  
  public synchronized void checkWelcome(String text) {
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
        game.addPlayerToList(player);
        game.setPlayer(player);
      }
      if (game.getPlayerType() == "b") {
        Player player = new ComputerPlayer(command[1], playerNumber, new NaiveStrategy());
        game.addPlayerToList(player);
        game.setPlayer(player);
      }
      System.out.println(game.getPlayerList());
      for (Player player : game.getPlayerList()) {
        try {
          player.addToHand(new Tile("R", "o"));
          player.addToHand(new Tile("B", "o"));
          player.addToHand(new Tile("Y", "o"));
          player.addToHand(new Tile("P", "o"));
        } catch (HandIsFullException e) {
          System.out.println("Hand is full.");
        }
      }
      game.playerTurn();
    }
  }
  
  
  public boolean validWelcomeCommand(String[] command) throws InvalidCommandException {
    boolean validCommand = false;
    
    validCommand = true;
    return validCommand;
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
