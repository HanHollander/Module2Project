package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import client.Move.Type;
import exceptions.HandIsFullException;
import exceptions.InvalidMoveException;
import exceptions.TileNotInHandException;

public class Game {
    
  //Fields\\
  
  private Board board;
    
  private List<Player> playerList;
  private Player player;
  private String playerName;
  
  private boolean playerTurn = false;
  public static final String MOVE = "MOVE";
  public static final String SWAP = "SWAP";
  public static final String END = "END";
  public static final String HELLO = "HELLO";
  public static final String WELCOME = "WELCOME";
  private Client client;
  private String playerType;
  
  //Constructor\\
  
  public Game(String name, InetAddress host, int port, String playerType) {
    this.board = new Board();
    playerList = new ArrayList<>();
    this.playerName = name;
    this.setPlayerType(playerType);
    try {
      System.out.print("Creating client... ");
      client = new Client(name, host, port, this);
      System.out.println("Client "+ client.getClientName() + " created");
    } catch (IOException e) {
      System.out.println("Client could not be created.");
    }
    System.out.print("Starting client... ");
    client.start();
    System.out.println("Client started.");
    start();
  }
  
  
  
  public synchronized void start() {
    client.sendMessage(HELLO + " " + playerName);
  }
  
  
  
  
  
  
  
  public void addPlayerToList(Player player) {
    playerList.add(player);
  }
  public void setPlayer(Player player) {
    this.player = player;
  }
  
  public void playerTurn() {
    setPlayerTurn(true);
    while (playerTurn) {
      System.out.println(player.handToString());
      makeMove(board);
      System.out.println(board.toString());
    }
    endTurn();
  }
  
  public void endTurn() {
    String command;
    if (player.getMoves().size() > 0) {
      String listType = player.getMoves().get(0).getType().toString();
      command = listType + " ";
      if (listType == MOVE) {
        for (Move move : player.getMoves()) {
          command = command + move.getTile().toString() 
              + " " + move.getRow() + " " + move.getColumn() + " ";
        }
        System.out.println("Score :" + board.getScoreCurrentTurn());
      } else if (listType == SWAP) {
        for (Move move : player.getMoves()) {
          command = command + move.getTile().toString() + " ";
        }
      }
      player.setMoves(new ArrayList<Move>());
    } else {
      command = MOVE;
    }
    System.out.println("Command: " + command);
    //send command
    board.endTurn();
    opponentTurn();
  }
  
  public void opponentTurn() {
    
    
    
    playerTurn();
  }

  public void makeMove(Board board) {
    Move move = null;
    try {
      move = player.determineMove(board);
      if (move.getType().equals(Type.MOVE)) {
        board.putTile(move);
      } else if (move.getType().equals(Type.SWAP)) {
        //Send command
      } else if (move.getType().equals(Type.END)) {
        setPlayerTurn(false);
      }
    } catch (InvalidMoveException e) {
      System.out.println(e);
    }
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  public void setPlayerList(List<Player> playerList) {
    this.playerList = playerList;
  }
  
  public void setPlayerTurn(boolean bool) {
    playerTurn = bool;
  }



  public String getPlayerType() {
    return playerType;
  }



  public void setPlayerType(String playerType) {
    this.playerType = playerType;
  }



 
 
    
    

}
