package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import server.Move.Type;

/**
 * Server. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Server {
  private static final String USAGE = "usage: " + Server.class.getName() + " <port> <numberOfPlayers(2,3,4)> <AITime>";
  
  /** Start een Server-applicatie op. */
  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println(USAGE);
      System.exit(0);
    }
    
    int portInt = 0;
    int numberOfPlayers = 0;
    int aiTime = 0;
    
    try {
      portInt = Integer.parseInt(args[0]);
      numberOfPlayers = Integer.parseInt(args[1]);
      aiTime = Integer.parseInt(args[2]);
    } catch (NumberFormatException e) {
      System.out.println(USAGE);
      System.exit(0);
    }
    
    if (numberOfPlayers < 2 && numberOfPlayers > 4) {
      System.out.println(USAGE);
      System.exit(0);
    }
    
    Server server = new Server(portInt, numberOfPlayers, aiTime);
    server.run();

  }


  private int port;
  private HashMap<Integer, ClientHandler> threads;
  private ServerSocket serverSocket;
  private int numberOfPlayers;
  private Game game;
  private Object monitor;
  
  /** Constructs a new Server object. */
  public Server(int portArg, int numberOfPlayersArg, int aiTime) {
    port = portArg;
    numberOfPlayers = numberOfPlayersArg;
    threads = new HashMap<Integer, ClientHandler>();
    game = new Game(this);
    monitor = new Object();
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      print("Could not create server socket on port " + port);
    }
  }

  /**
   * Listens to a port of this Server if there are any Clients that 
   * would like to connect. For every new socket connection a new
   * ClientHandler thread is started that takes care of the further
   * communication with the Client.
   */
  public void run() {
    System.out.println("Waiting for clients to connect");
    int numberOfConnectingPlayer = 1;
    while (numberOfConnectingPlayer < numberOfPlayers) {
      try {
        System.out.println("Clienents connected: [" + (numberOfConnectingPlayer - 1) + " of " + numberOfPlayers + "]");
        ClientHandler ch = new ClientHandler(numberOfConnectingPlayer, this, serverSocket.accept(), monitor);
        addHandler(numberOfConnectingPlayer, ch);
        ch.start();
        numberOfPlayers++;
      } catch (IOException e) {
        System.out.println("Could not connect to client " + numberOfConnectingPlayer);
      }
    }
    System.out.println("Clienents connected: [" + (numberOfConnectingPlayer - 1) + " of " + numberOfPlayers + "]");
    System.out.println("Waiting for everyone to send their name");
    while (!allPlayerNamesAreKnown()) {
      synchronized (monitor) {
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          System.out.println("Interupted while waiting for everyone to send their name");
        }
      }
    }
    System.out.println("Everyone has send their name");
    System.out.println("Dealing tiles");
    game.dealTiles();
    System.out.println("Calculating who may start");
    int playerNrWithTheBestHand = game.getPlayerNrWithTheBestHand();
    game.setCurrentPlayer(playerNrWithTheBestHand);
    System.out.println("Player-" + playerNrWithTheBestHand + " may start");
    broadcast("NEXT " + playerNrWithTheBestHand);
    
    while (game.getPoolSize() > 0) {
      synchronized (monitor) {
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          System.out.println("Interupted while waiting for everyone to send their name");
        }
      }
      nextPlayerTurn();
      broadcast("NEXT " + playerNrWithTheBestHand);
    }
    
    
  }

  private boolean allPlayerNamesAreKnown() {
    boolean result = true;
    Set<Integer> playerNrs = threads.keySet();
    Set<Integer> knownPlayers = game.getPlayerNrs();
    for (int playerNr : playerNrs) {
      result = result && knownPlayers.contains(playerNr);
    }
    return result;
  }

  public void print(String message) {
    System.out.println(message);
  }

  /**
   * Sends a message using the collection of connected ClientHandlers
   * to all connected Clients.
   * @param msg message that is send
   */
  public void broadcast(String msg) {
    for (Map.Entry<Integer, ClientHandler> entry : threads.entrySet()) {
      entry.getValue().sendMessage(msg);
    }
    System.out.println("Broadcast: " + msg);
  }

  /**
   * Add a ClientHandler to the collection of ClientHandlers.
   * @param handler ClientHandler that will be added
   */
  public void addHandler(int playerNr, ClientHandler handler) {
    threads.put(playerNr, handler);
  }

  /**
   * Remove a ClientHandler from the collection of ClientHanlders. 
   * @param handler ClientHandler that will be removed
   */
  public void removeHandler(int playerNr) {
    threads.remove(playerNr);
  }
  
  public Game getGame() {
    return game;
  }
  
  public void kick(int playerNr, String reason) {
    List<Tile> hand = game.getPlayer(playerNr).getHand();
    broadcast("KICK " + playerNr + " " + hand.size() + " " + reason);
    for (Tile tile : hand) {
      game.addTileToPool(tile);
    }
    threads.get(playerNr).shutdown();
    game.removePlayer(playerNr);
  }
  
  public void nextPlayerTurn() {
    Set<Integer> playingPlayers = threads.keySet();
    int previousPlayer = game.getCurrentPlayer();
    int nextPlayer = (previousPlayer + 1) % 4;
    while (!playingPlayers.contains(nextPlayer)) {
      nextPlayer = (nextPlayer + 1) % 4;
    }
    game.setCurrentPlayer(nextPlayer);
    broadcast("NEXT " + nextPlayer);
  }
  
  public void sendTurn(int playerNr, List<Move> turn) {
    String msg = "TURN ";
    if (turn.get(0).getType() == Type.MOVE) {
      for (int i = 0; i < turn.size(); i++) {
        msg = msg + turn.get(i).toString() + " ";
      }
      msg = msg.substring(0, msg.length() - 2);
    } else {
      msg = msg + "empty";
    }
    broadcast(msg);
  }
  
  public void giveTiles(int playerNr, List<Tile> tiles) {
    String msg = "NEW";
    for (Tile tile : tiles) {
      msg = msg + " " + tile.toString();
    }
    threads.get(playerNr).sendMessage(msg);
  }
}
