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

/**
 * Main methods class.
 * @author Han
 */
public class Game {
     
  private Board board;
    
  private List<Player> playerList;
  private Player player;
  private String playerName;
  
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
  
  private int pool = 108;
  private boolean playerTurn;
  
  /**
   * Constructs a new game and a new client.
   * @param name name of client.
   * @param host server.
   * @param port port of server.
   * @param playerType type of player.
   */
  public Game(String name, InetAddress host, int port, String playerType) {
    this.board = new Board();
    playerList = new ArrayList<>();
    this.playerName = name;
    this.setPlayerType(playerType);
    //Try creating a client.
    try {
      System.out.println("Creating client... ");
      client = new Client(name, host, port, this);
      System.out.println("Client " + client.getClientName() + " created");
      System.out.println("Starting client... ");
      client.start();
      System.out.println("Client started.");
      System.out.println("Sending registration message... ");
      client.sendMessage(HELLO + " " + playerName);
      System.out.println("Registration message send.");
      System.out.println("Waiting for welcome message... ");
    } catch (IOException | NullPointerException e) {
      System.out.println("Client could not be created or started.");
    }
  }
  
  /**
   * Gets called when it is the turn of the player.
   */
  public void playerTurn() {
    setPlayerTurn(true);
    System.out.println("It is your turn!" + "\n");
    while (playerTurn) {
      System.out.println("Hand: " + player.handToString() + "\n");
      makeMove();
      System.out.println("\n" + board.toString());
    }
    //End the turn (after the END command).
    endTurn();
  }
  
  /**
   * Gets called when it is an opponents turn.
   * @param moves the moves the opponent did.
   */
  public void opponentTurn(List<Move> moves, Player player) {
    for (Move move : moves) {
      //Place all the moves on the board.
      board.putTile(move);
      setPool(getPool() - 1);
    }
    //Get score
    int score = board.getScoreCurrentTurn();
    player.setScore(player.getScore() + score);
    System.out.println("\n" + board.toString());
    printScores();
    //Reset board counters.
    board.endTurn();
  }

  /**
   * Gets called when it is the players turn. More than one move can be done in one turn.
   */
  public void makeMove() {
    Move move = null;
    try {
      //Get the user-input and convert it to a move;
      move = player.determineMove(board);
      //If the move is of the type MOVE put the tile on the board.
      if (move.getType().equals(Type.MOVE)) {
        board.putTile(move);
        //If it is of the type END, end the turn
      } else if (move.getType().equals(Type.END)) {
        setPlayerTurn(false);
      }
    } catch (InvalidMoveException e) {
      System.out.println(e);
    }
  }

  /**
   * Gets called when the turn is over.
   */
  public void endTurn() {
    String command;
    //Compile the command.
    if (player.getMoves().size() > 0) {
      String listType = player.getMoves().get(0).getType().toString();
      command = listType + " ";
      if (listType == MOVE) {
        for (Move move : player.getMoves()) {
          command = command + move.getTile().toString() 
              + " " + move.getRow() + " " + move.getColumn() + " ";
        }
      } else if (listType == SWAP) {
        for (Move move : player.getMoves()) {
          command = command + move.getTile().toString() + " ";
          setPool(getPool() + 1);
        }
      }
      player.setMoves(new ArrayList<Move>());
    } else {
      command = MOVE;
    }
    //Send the command to server.
    //System.out.println("Command sent to server: " + command);
    client.sendMessage(command);
    //Add score
    int score = board.getScoreCurrentTurn();
    player.setScore(player.getScore() + score);
    printScores();
    //Reset the board counters.
    board.endTurn();
  }
  
  public void printScores() {
    for (Player player : playerList) {
      System.out.println(player.getName() + "'s score is: " + player.getScore());
    }
  }
  
  public void addPlayerToList(Player player) {
    playerList.add(player);
  }

  public Player getPlayer() {
    return player;
  }
  
  /**
   * Get player with nr
   * @param nr nr
   * @return player
   */
  public Player getPlayerWithNumber(int nr) {
    Player result = null;
    for (Player player : playerList) {
      if (player.getPlayerNumber() == nr) {
        result = player;
      }
    }
    return result;
  }

  public void setPlayer(Player player) {
    this.player = player;
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
  
  public int getPool() {
    return pool;
  }

  public void setPool(int pool) {
    this.pool = pool;
  }

  public String getPlayerType() {
    return playerType;
  }

  public void setPlayerType(String playerType) {
    this.playerType = playerType;
  }

  public void setPlayerTurn(boolean bool) {
    playerTurn = bool;
  }

}
