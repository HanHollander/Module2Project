package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientHandler extends Thread {
  
  public static final List<String> COLOURS = Arrays.asList("R", "O", "B", "Y", "G", "P");
  public static final List<String> SHAPES = Arrays.asList("o", "d", "s", "c", "x", "*");
  
  private Server server;
  private Socket socket;
  private BufferedReader in;
  private BufferedWriter out;
  private String clientName;
  private int playerNr;
  private Object monitor;
  
  /**
   * Constructor for clienthandler.
   * @param serverArg server
   * @param sockArg socket
   * @throws IOException if not able to create in or out
   */
  public ClientHandler(int playerNr, Server serverArg, Socket sockArg, Object monitor)
      throws IOException {
    server = serverArg;
    socket = sockArg;
    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    this.playerNr = playerNr;
    this.monitor = monitor;
  }
  
  /**
   * CLIENT -> server.
   */
  public void run() {
    String text = "";
    // Starting procedure
    try {
      text = in.readLine();
      if (isValidStartMessage(text)) {
        String[] textParts = text.split(" ");
        clientName = textParts[1];
        System.out.println("Received from client " + playerNr + ": " + text);
        sendMessage("WELCOME " + clientName + " " + playerNr);
        server.getGame().addPlayer(playerNr, clientName);
        synchronized (monitor) {
          monitor.notifyAll();
        }
      } else {
        server.kick(playerNr, "Did not recieve valid start message");
      }
    } catch (IOException e) {
      shutdown();
    }
    
    while (server.getGame().getPoolSize() > 0) {
      try {
        text = in.readLine();
        System.out.println("Received from player-" + playerNr + ": " + text);
        if (server.getGame().getCurrentPlayer() == playerNr) {
          if (isValidMoveTurn(text)) {
            List<Move> turn = convertStringToMoveTurn(text);
            server.getGame().applyMoveTurn(server.getGame().getPlayer(playerNr), turn, false);
            synchronized (monitor) {
              monitor.notifyAll();
            }
          } else if (isValidSwapTurn(text)) {
            List<Tile> turn = convertStringToSwapTurn(text);
            server.getGame().applySwapTurn(turn, server .getGame().getPlayer(playerNr));
            synchronized (monitor) {
              monitor.notifyAll();
            }
          } else {
            server.kick(playerNr, "made a invalid turn");
          }
        } else {
          server.kick(playerNr, "spoke before his/her turn");
        }
      } catch (IOException e) {
        server.kick(playerNr, "lost connection");
      }
    }
  }
  
  private List<Tile> convertStringToSwapTurn(String text) {
    String[] swapTextParts = text.substring(5).split(" ");
    List<Tile> turn = new ArrayList<Tile>();
    for (String tileText : swapTextParts) {
      turn.add(new Tile(tileText.substring(0, 1), tileText.substring(1, 2)));
    }
    return turn;
  }

  private List<Move> convertStringToMoveTurn(String text) {
    String[] moveTextParts = text.substring(5).split(" ");
    List<Move> turn = new ArrayList<Move>();
    for (int i = 0; i < moveTextParts.length; i += 3) {
      String colour = Character.toString(moveTextParts[i].charAt(0));
      String shape = Character.toString(moveTextParts[i].charAt(1));
      int row = Integer.parseInt(moveTextParts[i + 1]);
      int column = Integer.parseInt(moveTextParts[i + 2]);
      turn.add(new Move(new Tile(colour, shape), row, column));
    }
    return turn;
  }

  private boolean isValidSwapTurn(String text) {
    boolean result = true;
    String[] swapTextParts = null;
    if (text.startsWith("SWAP ")) {
      String swapText = text.substring(5);
      swapTextParts = swapText.split(" ");
      if (swapTextParts.length > 0 && swapTextParts.length < 7) {
        for (String tile : swapTextParts) {
          if (tile.length() == 2) {
            if (COLOURS.contains(tile.substring(0,1)) 
                && SHAPES.contains(tile.substring(1,2))) {
              result = result && true;
            }
          } else {
            result = false;
            break;
          }
        }
      } else {
        result = false;
      }
    } else {
      result = false;
    }
    
    //Check if tiles are in the hand of the player
    if (result) {
      List<Tile> turn = convertStringToSwapTurn(text);
      result = result && server.getGame().checkSwapTurn(turn, server.getGame().getPlayer(playerNr));
    }
    return result;
  }

  private boolean isValidMoveTurn(String text) {
    boolean result = true;
    String[] moveTextParts = null;
    if (text.startsWith("MOVE ")) {
      String moveText = text.substring(5);
      moveTextParts = moveText.split(" ");
      if ((moveTextParts.length % 3) == 0) {
        for (int i = 0; i < moveTextParts.length; i++) {
          // Check if the tiles are valid tiles
          if ((i % 3) == 0) {
            String tileText = moveTextParts[i];
            if (tileText.length() == 2) {
              if (COLOURS.contains(tileText.substring(0,1)) 
                  && SHAPES.contains(tileText.substring(1,2))) {
                result = result && true;
              }
            } else {
              result = false;
              break;
            }
          } else {
            int coordinate;
            try {
              coordinate = Integer.parseInt(moveTextParts[i]);
            } catch (NumberFormatException e) {
              result = false;
              break;
            }
            if (coordinate >= 0 && coordinate <= 182) {
              result = result && true;
            } else {
              result = false;
              break;
            }
          }
        }
      } else {
        result = false;
      }
    } else {
      result = false;
    }
    
    //Check if the move is valid in the game
    if (result) {
      List<Move> turn = convertStringToMoveTurn(text);
      result = result && server.getGame().checkMoveTurn(turn, server.getGame().getPlayer(playerNr));
    }
    return result;
  }

  private Boolean isValidStartMessage(String text) {
    List<String> allowedChars = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
        "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", 
        "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
        "S", "T", "U", "V", "W", "X", "Y", "Z");
    Boolean result;
    result = text.startsWith("HELLO ");
    if (result) {
      String name = text.substring(6);
      result = result && name.length() < 17;
      for (int i = 0; i < name.length(); i++) {
        result = result && allowedChars.contains(name.substring(i, i + 1));
      }
    }
    return result;
  }

  /**
   * SERVER -> client.
   * @param msg message
   */
  public void sendMessage(String msg) {
    try {
      out.write(msg);
      out.newLine();
      out.flush();
    } catch (IOException e) {
      shutdown();
    }
    System.out.println("Send to player-" + playerNr + ": " +  msg);
  }
  
  public String getClientName() {
    return clientName;
  }
  
  /**
   * Closes the socket of this clientHandler.
   */
  public void shutdown() {
    try {
      socket.close();
    } catch (IOException e) {
      System.out.println("Could not close socket in the shutdown procedure.");
    }
    server.removeHandler(playerNr);
  }

}
