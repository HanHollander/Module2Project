package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Server. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Server {
  private static final String USAGE = "usage: " + Server.class.getName() 
      + " <port> <numberOfPlayers(2,3,4)> <AITime>";
  
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
  private int aiTime;
  private boolean wokenByTimer;
  private boolean imReady;
  
  /** Constructs a new Server object. */
  public Server(int portArg, int numberOfPlayersArg, int aiTime) {
    port = portArg;
    numberOfPlayers = numberOfPlayersArg;
    threads = new HashMap<Integer, ClientHandler>();
    game = new Game(this);
    monitor = new Object();
    this.aiTime = aiTime;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println("Could not create server socket on port " + port);
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
    while (numberOfConnectingPlayer - 1 < numberOfPlayers) {
      try {
        System.out.println("Clients connected: [" + (numberOfConnectingPlayer - 1) 
            + " of " + numberOfPlayers + "]");
        ClientHandler ch = new ClientHandler(numberOfConnectingPlayer, 
            this, serverSocket.accept(), monitor);
        addHandler(numberOfConnectingPlayer, ch);
        ch.start();
        numberOfConnectingPlayer++;
      } catch (IOException e) {
        System.out.println("Could not connect to client " + numberOfConnectingPlayer);
      }
    }
    System.out.println("Clients connected: [" + (numberOfConnectingPlayer - 1) 
        + " of " + numberOfPlayers + "]");
    System.out.println("Waiting for everyone to send their name" + "\n");
    while (!allPlayerNamesAreKnown()) {
      synchronized (monitor) {
        try {
          monitor.wait();
        } catch (InterruptedException e) {
          System.out.println("Interupted while waiting for everyone to send their name");
        }
      }
    }
    Set<Integer> playerNrs = threads.keySet();
    System.out.println("\n" + "Everyone has send their name");
    String namesMsg = "NAMES";
    for (int number : playerNrs) {
      namesMsg = namesMsg + " " + game.getPlayer(number).getName() + " " + number;
    }
    broadcast(namesMsg + " " + aiTime);
    
    System.out.println("\n" + "Dealing tiles and making first move" + "\n");
    game.dealTiles();
    System.out.println("\n" + "Tiles dealt and first move made" + "\n");
    
    nextPlayerTurn();
    
    
    
    while (!game.isGameOver() && threads.size() > 1) {
      // Print game situation
//      playerNrs = threads.keySet();
//      System.out.println("\n" + "\n" + "\n" + "Score board:");
//      for (int number : playerNrs) {
//        System.out.println("Player-" + number + ": " + game.getPlayer(number).getScore());
//      }
//      System.out.println("\n" + game.getBoard().toString() + "\n" + "Tiles in pool: " 
//          + game.getPoolSize());
//      for (int number : playerNrs) {
//        System.out.println("Player-" + number + " hand: " + game.getPlayer(number).getHand());
//      }
//      System.out.println("");
      
      // Wait for a clientHandler to do something (make a move or kick)
      synchronized (monitor) {
        Timer timer = new Timer(aiTime, this);
        timer.start();
        try {
          //imReady = true;
          wokenByTimer = false;
          monitor.wait();
          //imReady = false;
        } catch (InterruptedException e) {
          System.out.println("Interupted while waiting for someone to make a move");
        }
        if (wokenByTimer) {
          kick(game.getCurrentPlayer(), "Did not make a move in time");
        } else {
          timer.stopTimer();
        }
        if (!game.isGameOver() && threads.size() > 1) {
          nextPlayerTurn();
        }
      }
      System.out.println(game.getPoolSize());
    }
    broadcast("WINNER " + game.getWinningPlayerNr());
    
  }
  
  /**
   * Checks if all connected players have send their name.
   * @return True of false whether all players have send their name or not.
   */
  private boolean allPlayerNamesAreKnown() {
    boolean result = true;
    Set<Integer> playerNrs = threads.keySet();
    Set<Integer> knownPlayers = game.getPlayerNrs();
    for (int playerNr : playerNrs) {
      result = result && knownPlayers.contains(playerNr);
    }
    return result;
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
  
  public ClientHandler getThread(int playerNr) {
    return threads.get(playerNr);
  }
  
  /**
   * Kicks the given player, removes all references to that player
   * in the game and broadcasts it to all players.
   * @param playerNr The number of the player that is being kicked.
   * @param reason A String with a message about the reason of the kick.
   */
  public void kick(int playerNr, String reason) {
    List<Tile> hand = game.getPlayer(playerNr).getHand();
    broadcast("KICK " + playerNr + " " + hand.size() + " " + reason);
    for (Tile tile : hand) {
      game.addTileToPool(tile);
    }
    threads.get(playerNr).shutdown();
    game.removePlayer(playerNr);
  }
  
  /**
   * Gives the turn to the next player and broadcasts it to all players.
   */
  public void nextPlayerTurn() {
    Set<Integer> playingPlayers = threads.keySet();
    int previousPlayer = game.getCurrentPlayer();
    int nextPlayer = (previousPlayer + 1) % 5;
    while (!playingPlayers.contains(nextPlayer)) {
      nextPlayer = (nextPlayer + 1) % 4;
    }
    game.setCurrentPlayer(nextPlayer);
    broadcast("NEXT " + nextPlayer);
  }
  
  /**
   * Broadcasts the given turn and the player who made that turn.
   * @param playerNr The player who made the turn.
   * @param turn The turn which was made.
   */
  public void sendTurn(int playerNr, List<Move> turn) {
    String msg = "TURN " + playerNr;
    
    for (int i = 0; i < turn.size(); i++) {
      msg = msg + " " + turn.get(i).toString();
    }
    
    broadcast(msg);
  }
  
  /**
   * Sends a player the given tiles
   * @param playerNr The player to whom the tiles need to be send to.
   * @param tiles The tiles that need to be send
   */
  public void giveTiles(int playerNr, List<Tile> tiles) {
    String msg;
    if (tiles.size() > 0) {
      msg = "NEW";
      for (Tile tile : tiles) {
        msg = msg + " " + tile.toString();
      }
    } else {
      msg = "NEW empty";
    }
    threads.get(playerNr).sendMessage(msg);
  }
  
  /**
   * A function that is only called by the timer. This function wakes
   * the server and makes sure that the server can tell that it is
   * woken by the timer.
   */
  public void timerWakesServer() {
    synchronized (monitor) {
      wokenByTimer = true;
      monitor.notifyAll();
    }
  }
}
