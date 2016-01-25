package client.controller;

import client.model.Board;
import client.model.HumanPlayer;
import client.model.Move;
import client.model.Move.Type;
import client.model.NaiveStrategy;
import client.model.Player;
import client.view.Printer;
import exceptions.InvalidMoveException;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * The main controller class. Controls the players, board and client.
 * @author Han Hollander
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
  
  private NaiveStrategy hintGen;
  
  /**
   * Constructs a new game and a new client.
   * @param name name of client.
   * @param host server.
   * @param port port of server.
   */
  public Game(String name, InetAddress host, int port) {
    this.board = new Board();
    playerList = new ArrayList<>();
    this.playerName = name;
    this.setPlayerType(Qwirkle.getPlayerType());
    setHintGen(new NaiveStrategy());
    //Try creating a client.
    try {
      Printer.print("Creating client... ");
      client = new Client(name, host, port, this);
      Printer.print("Client " + client.getClientName() + " created");
      Printer.print("Starting client... ");
      client.start();
      Printer.print("Client started.");
      Printer.print("Sending registration message... ");
      client.sendMessage(HELLO + " " + playerName);
      Printer.print("Registration message send.");
      Printer.print("Waiting for welcome message... ");
    } catch (IOException | NullPointerException e) {
      Printer.print("Client could not be created or started, trying again.\n");
      String[] args = new String[0];
      Qwirkle.main(args);
    }
  }

  /**
   * Gets called when it is the turn of the player.
   */
  public void playerTurn() {
    Printer.printBoard(this);
    //Get one single hint, only at start of turn.
    if (getPlayer() instanceof HumanPlayer && getPlayer().getHand().size() != 0) {
      Printer.print("\nHint: " + getHint().colourToString());
    }
    setPlayerTurn(true);
    Printer.print("\nIt is your turn!");
    while (playerTurn) {
      Printer.print("\nWhat is your action?");
      //Get user input.
      makeMove();
      Printer.printBoard(this);
    }
    //Update score.
    int score = 0;
    if (board.getMoveList().size() != 0) {
      score = board.getScoreCurrentTurn();
    } 
    //Since the player in the list is different than the player field:
    getPlayerWithNumber(player.getPlayerNumber())
      .setScore(getPlayerWithNumber(player.getPlayerNumber()).getScore() + score);
    //End the turn (after the END command).
    endTurn();
  }
  
  /**
   * Gets called when it is an opponents turn.
   * @param moves The moves the opponent made.
   */
  public void opponentTurn(List<Move> moves, Player player) {
    for (Move move : moves) {
      //Place all the moves on the board.
      board.putTile(move);
      if (getPool() > 0) {
        setPool(getPool() - 1);
      }
    }
    //Get score
    int score = 0;
    if (board.getMoveList().size() != 0) {
      score = board.getScoreCurrentTurn();
    } 
    player.setScore(player.getScore() + score);
    Printer.printBoard(this);
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
      Printer.print(e);
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
    //Printer.print("Command sent to server: " + command);
    client.sendMessage(command);
    //Reset the board counters.
    board.endTurn();
  }
  
  /**
   * Print the scores.
   */
  public void printScores() {
    for (Player player : playerList) {
      Printer.print(player.getName() + "'s score is: " + player.getScore());
    }
  }
  
  public void addPlayerToList(Player player) {
    playerList.add(player);
  }

  public Player getPlayer() {
    return player;
  }
  
  /**
   * Get player with specific ID.
   * @param nr The player ID.
   * @return The player.
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
  
  /**
   * Determines a hint for the human player.
   */
  public Move getHint() {
    return hintGen.getHint(board, player.getHand());
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

  public NaiveStrategy getHintGen() {
    return hintGen;
  }

  public void setHintGen(NaiveStrategy hintGen) {
    this.hintGen = hintGen;
  }


}
