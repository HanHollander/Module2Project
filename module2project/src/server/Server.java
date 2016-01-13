package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Server. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Server {
  private static final String USAGE = "usage: " + Server.class.getName() + " <port> " + "<numberOfPlayers(2,3,4)>";
  
  /** Start een Server-applicatie op. */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println(USAGE);
      System.exit(0);
    }
    
    int portInt = 0;
    int numberOfPlayers = 0;
    
    try {
      portInt = Integer.parseInt(args[0]);
      numberOfPlayers = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.println(USAGE);
      System.exit(0);
    }
    
    if (numberOfPlayers < 2 && numberOfPlayers > 4) {
      System.out.println(USAGE);
      System.exit(0);
    }
    
    Server server = new Server(portInt, numberOfPlayers);
    server.run();

  }


  private int port;
  private HashMap<Integer, ClientHandler> threads;
  private ServerSocket serverSocket;
  private int numberOfPlayers;
  private Game game;
  /** Constructs a new Server object. */
  public Server(int portArg, int numberOfPlayersArg) {
    port = portArg;
    numberOfPlayers = numberOfPlayersArg;
    threads = new HashMap<Integer, ClientHandler>();
    game = new Game(this);
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
    int numberOfConnectingPlayer = 1;
    while (numberOfPlayers < numberOfPlayers) {
      try {
        ClientHandler ch = new ClientHandler(numberOfConnectingPlayer, this, serverSocket.accept());
        addHandler(numberOfConnectingPlayer, ch);
        ch.start();
        numberOfPlayers++;
      } catch (IOException e) {
        System.out.println("");
      }
    }
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
}
