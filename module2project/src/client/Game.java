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
  public static final String NAMES = "NAMES";
  public static final String NEXT = "NEXT";
  public static final String NEW = "NEW";
  public static final String TURN = "TURN";
  public static final String KICK = "KICK";
  public static final String WINNER = "WINNER";
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
      System.out.print("Starting client... ");
      client.start();
      System.out.println("Client started.");
      client.sendMessage(HELLO + " " + playerName);
    } catch (IOException e) {
      System.out.println("Client could not be created.");
    } catch (NullPointerException e) {
      System.out.println("Client could not be started.");
    }
    
  }
  
  
  
  
  
  
  
  
  public void addPlayerToList(Player player) {
    playerList.add(player);
  }
  public void setPlayer(Player player) {
    this.player = player;
  }
  public Player getPlayer() {
    return player;
  }
  
  
  
  
  public void playerTurn() {
    setPlayerTurn(true);
    System.out.println("It is your turn!");
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
    client.sendMessage(command);
    board.endTurn();
  }
  
  public void opponentTurn(List<Move> moves) {
    for (Move move : moves) {
      board.putTile(move);
    }
    board.endTurn();
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
